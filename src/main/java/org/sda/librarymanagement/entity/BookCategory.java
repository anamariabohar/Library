package org.sda.librarymanagement.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Entity
@Data
@Table(name = "Category")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class BookCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private Long categoryId;
	@ManyToMany(mappedBy = "bookCategories")
	private List<Book> books = new ArrayList<Book>();
	@Column(name = "category_name")
	private String categoryName;

	@Override
	public String toString() {
		return "BookCategory [categoryId=" + categoryId + ", books=" + books + ", categoryName=" + categoryName + "]";
	}

}