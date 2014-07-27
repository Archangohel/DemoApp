package com.choosesmart.bo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Category {
	@Id
	//@GeneratedValue
	private int id;
	private String name;
	private Boolean topLevel;
	@ManyToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "category_id")
	private Category linkedCategory;
	@OneToMany(mappedBy = "linkedCategory", cascade = { CascadeType.ALL })
	private Set<Category> linkedCategories = new HashSet<Category>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getTopLevel() {
		return topLevel;
	}

	public void setTopLevel(Boolean topLevel) {
		this.topLevel = topLevel;
	}

	public Category getLinkedCategory() {
		return linkedCategory;
	}

	public void setLinkedCategory(Category linkedCategory) {
		this.linkedCategory = linkedCategory;
	}

	public Set<Category> getLinkedCategories() {
		return linkedCategories;
	}

	public void setLinkedCategories(Set<Category> linkedCategories) {
		this.linkedCategories = linkedCategories;
	}

}
