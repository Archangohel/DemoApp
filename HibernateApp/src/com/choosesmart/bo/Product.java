package com.choosesmart.bo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Product {
	@Id
	@GeneratedValue
	private int id;
	@Column(nullable = false)
	private String title;
	@Column(length = 2000)
	private String imageUrlStr;
	private Double mrp;
	@OneToOne
	private Category category;
	private String productBrand;
	@OneToMany(mappedBy = "product", cascade = { CascadeType.ALL })
	private Set<ExternalProduct> externalProducts = new HashSet<ExternalProduct>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageUrlStr() {
		return imageUrlStr;
	}

	public void setImageUrlStr(String imageUrlStr) {
		this.imageUrlStr = imageUrlStr;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getProductBrand() {
		return productBrand;
	}

	public void setProductBrand(String productBrand) {
		this.productBrand = productBrand;
	}

	public Set<ExternalProduct> getExternalProducts() {
		return externalProducts;
	}

	public void setExternalProducts(Set<ExternalProduct> externalProducts) {
		this.externalProducts = externalProducts;
	}

}
