package org.hibernate.test;

import java.io.File;
import java.nio.charset.Charset;
import java.util.StringTokenizer;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class TestLoader {
	private static Logger logger = Logger.getLogger(TestLoader.class);

	public static void main(String[] argc) {
		try {
			@SuppressWarnings("deprecation")
			SessionFactory factory = new Configuration().configure()
					.buildSessionFactory();
			Session session = factory.openSession();

			File csvData = new File(".\\src\\org\\hibernate\\test\\prod.csv");
			CSVParser parser;

			parser = CSVParser.parse(csvData, Charset.defaultCharset(),
					CSVFormat.RFC4180);
			session.beginTransaction();
			for (CSVRecord csvRecord : parser) {
				Proudct p = new Proudct();
				System.out.println(csvRecord);
				p.setProductId(csvRecord.get(0));
				p.setTitle(csvRecord.get(1));
				p.setImageUrlStr(csvRecord.get(2));
				p.setMrp(parseValue(csvRecord.get(3)));
				p.setPrice(parseValue(csvRecord.get(4)));
				p.setProductUrl(csvRecord.get(5));
				p.setCategories(csvRecord.get(6));
				p.setProductBrand(csvRecord.get(7));
				p.setDeliveryTime(csvRecord.get(8));
				p.setInStock(Boolean.valueOf(csvRecord.get(9)));
				p.setCodAvailable(csvRecord.get(10));
				p.setEmiAvailable(csvRecord.get(11));
				p.setOffers(csvRecord.get(12));
				p.setDiscount(parseValue(csvRecord.get(13)));
				session.save(p);
			}
			session.getTransaction().commit();
			parser.close();
			session.close();
			factory.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Error!!");
		}

	}

	private static double parseValue(String valueStr) {
		StringTokenizer tokenizer = new StringTokenizer(valueStr, ",");
		return Double.valueOf(tokenizer.nextToken());
	}
}
