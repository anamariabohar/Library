package org.sda.librarymanagement.entity.dto;

import java.util.List;

import lombok.Data;

@Data
public class BookCategoryDTO {

	private Long categoryId;
	private String categoryName;
	private List<Long> books;
}
