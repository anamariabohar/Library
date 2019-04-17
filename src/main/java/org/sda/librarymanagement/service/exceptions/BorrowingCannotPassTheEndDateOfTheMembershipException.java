package org.sda.librarymanagement.service.exceptions;

public class BorrowingCannotPassTheEndDateOfTheMembershipException extends Exception {

	public BorrowingCannotPassTheEndDateOfTheMembershipException(String errorMessage) {
		super(errorMessage);
	}
}
