package org.sda.librarymanagement.service.exceptions;

public class BorrowingCannotBeDoneBeforeOrAfterAvailabilityOfTheMembershipException extends Exception {

	public BorrowingCannotBeDoneBeforeOrAfterAvailabilityOfTheMembershipException(String errorMessage) {
		super(errorMessage);
	}
}
