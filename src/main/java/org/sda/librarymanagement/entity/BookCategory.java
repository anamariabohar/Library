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

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Data
@Table(name = "Category")
public class BookCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private Long categoryId;
	@Column(name = "category_name")
	private String categoryName;
	@JsonBackReference
	@ManyToMany(mappedBy = "bookCategories")
	private List<Book> books = new ArrayList<Book>();

	@Override
	public String toString() {
		return "BookCategory [categoryId=" + categoryId + ", categoryName=" + categoryName + "]";
	}

}