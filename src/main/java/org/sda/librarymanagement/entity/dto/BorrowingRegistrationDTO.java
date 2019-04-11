package org.sda.librarymanagement.entity.dto;

import lombok.Data;

@Data
public class BorrowingRegistrationDTO {

	private Long borrowingRegistrationId;
	private Long client;
	private Long book;
	private String borrowingDate;
	private String returnDate;
}
