package com.choosesmart.bo.utils;

import java.util.Map;

import com.choosesmart.bo.Category;
import com.choosesmart.bo.Merchant;

public class DataLoaderConfig {
	Merchant merchant;
	Map<Category,String> csvMap;
	public Merchant getMerchant() {
		return merchant;
	}
	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}
	public Map<Category, String> getCsvMap() {
		return csvMap;
	}
	public void setCsvMap(Map<Category, String> csvMap) {
		this.csvMap = csvMap;
	}
	public DataLoaderConfig(Merchant merchant, Map<Category, String> csvMap) {
		super();
		this.merchant = merchant;
		this.csvMap = csvMap;
	}
	public DataLoaderConfig(){
		
	}
}
