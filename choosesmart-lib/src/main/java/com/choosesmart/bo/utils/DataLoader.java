package com.choosesmart.bo.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.choosesmart.bo.Category;
import com.choosesmart.bo.ExternalProduct;
import com.choosesmart.bo.Merchant;
import com.choosesmart.bo.Product;

public class DataLoader implements Runnable {

	private static Logger logger = Logger.getLogger(DataLoader.class);
	private String csvPath;
	private Category category;
	private Merchant merchant;
	@SuppressWarnings("deprecation")
	private SessionFactory factory;

	public static void main(String[] argc) throws IOException {

		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"dataloader-config-beans.xml");
		DataLoaderConfig flipKartDataLoaderConfig = (DataLoaderConfig) applicationContext
				.getBean("flipKartDataLoaderConfig");
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		SessionFactory factory = new Configuration().configure()
				.buildSessionFactory();

		Session session = factory.openSession();
		session.beginTransaction();

		session.saveOrUpdate(flipKartDataLoaderConfig.getMerchant());
		for (Entry<Category, String> configEntry : flipKartDataLoaderConfig.csvMap
				.entrySet()) {
			session.saveOrUpdate(configEntry.getKey());
		}
		session.getTransaction().commit();
		session.close();

		for (Entry<Category, String> configEntry : flipKartDataLoaderConfig.csvMap
				.entrySet()) {
			System.out.println(configEntry.getKey().getName());
			System.out.println(configEntry.getValue());
			DataLoader dataLoader = new DataLoader();
			dataLoader.setFactory(factory);
			dataLoader.setCsvPath(configEntry.getValue());
			dataLoader.setCategory(configEntry.getKey());
			dataLoader.setMerchant(flipKartDataLoaderConfig.getMerchant());
			executorService.execute(dataLoader);
		}
		executorService.shutdown();
		try {
			executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
		} catch (InterruptedException e) {

		} finally {
			factory.close();
		}
	}

	public SessionFactory getFactory() {
		return factory;
	}

	public void setFactory(SessionFactory factory) {
		this.factory = factory;
	}

	private static double parseValue(String valueStr) {
		StringTokenizer tokenizer = new StringTokenizer(valueStr, ",");
		return Double.valueOf(tokenizer.nextToken());
	}

	public String getCsvPath() {
		return csvPath;
	}

	public void setCsvPath(String csvPath) {
		this.csvPath = csvPath;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}

	//@Override
	public void run() {
		CSVParser parser = null;
		Session session;

		session = getFactory().openSession();
		try {
			File csvData = new File(this.getCsvPath());
			parser = CSVParser.parse(csvData, Charset.defaultCharset(),
					CSVFormat.RFC4180);
			session.beginTransaction();
			int recordCount = 0;
			for (CSVRecord csvRecord : parser) {
				System.out.println(csvRecord);

				ExternalProduct externalProduct = new ExternalProduct();
				// set external product parms
				externalProduct.setProductId(csvRecord.get(0));
				externalProduct.setPrice(parseValue(csvRecord.get(4)));
				externalProduct.setProductUrl(csvRecord.get(5));
				externalProduct.setDeliveryTime(csvRecord.get(8));
				externalProduct.setInStock(Boolean.valueOf(csvRecord.get(9)));
				externalProduct.setCodAvailable(csvRecord.get(10));
				externalProduct.setEmiAvailable(csvRecord.get(11));
				externalProduct.setOffers(csvRecord.get(12));
				externalProduct.setDiscount(parseValue(csvRecord.get(13)));
				externalProduct.setMerchant(this.getMerchant());
				externalProduct.setMrp(parseValue(csvRecord.get(3)));
				Product product = new Product();
				externalProduct.setProduct(product);

				// set product params
				product.setTitle(csvRecord.get(1));
				product.setProductBrand(csvRecord.get(7));
				product.setImageUrlStr(csvRecord.get(2));
				product.setMrp(parseValue(csvRecord.get(3)));
				product.setCategory(this.getCategory());
				product.getExternalProducts().add(externalProduct);
				session.save(product);
				if (++recordCount % 50 == 0) {
					session.flush();
					session.clear();
				}
			}
			session.getTransaction().commit();

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Error!!");
		} finally {
			try {
				parser.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (session.isOpen()) {
				session.close();
			}
		}
	}
}
