package org.sda.librarymanagement.entity.dto;

import java.util.List;

import lombok.Data;

@Data
public class BookCategoryDTO {

	private Long categoryId;
	private List<Long> books;
	private String categoryName;
}
