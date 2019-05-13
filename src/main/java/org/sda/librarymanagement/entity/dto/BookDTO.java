package org.sda.librarymanagement.entity.dto;

import java.util.List;

import org.sda.librarymanagement.entity.enums.BorrowingPeriodEnum;

import lombok.Data;

@Data
public class BookDTO {

	private Long bookId;
	private String bookName;
	private String authorName;
	private boolean borrowingTypeAtHome;
	private BorrowingPeriodEnum borrowingPeriod;
	List<Long> bookCategories;
}
