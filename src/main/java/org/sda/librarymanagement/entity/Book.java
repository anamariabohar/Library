package org.sda.librarymanagement.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.sda.librarymanagement.entity.enums.BorrowingPeriodEnum;

import lombok.Data;

@Entity
@Data
@Table(name = "Books")
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "book_id")
	private Long bookId;
	@Column(name = "book_name")
	private String bookName;
	@Column(name = "author_name")
	private String authorName;
	@Column(name = "borrowing_type")
	private boolean borrowingTypeAtHome;
	@Column(name = "borrowing_period")
	@Enumerated(EnumType.STRING)
	private BorrowingPeriodEnum borrowingPeriod;
	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "book_category", joinColumns = { @JoinColumn(name = "book_id") }, inverseJoinColumns = {
			@JoinColumn(name = "category_id") })
	List<BookCategory> bookCategories = new ArrayList<BookCategory>();
}
