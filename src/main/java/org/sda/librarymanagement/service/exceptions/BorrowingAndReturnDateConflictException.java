package org.sda.librarymanagement.service.exceptions;

public class BorrowingAndReturnDateConflictException extends Exception {

	public BorrowingAndReturnDateConflictException(String errorMessage) {
		super(errorMessage);
	}

}
