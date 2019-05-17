package org.sda.librarymanagement.entity.dto;

import lombok.Data;

@Data
public class BorrowingRegistrationDTO {

	private Long borrowingRegistrationId;
	private Long clientId;
	private Long bookId;
	private String borrowingDate;
	private String dueDate;
	private String returnDate;
}
