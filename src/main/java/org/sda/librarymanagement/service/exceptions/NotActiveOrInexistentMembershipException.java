package org.sda.librarymanagement.service.exceptions;

public class NotActiveOrInexistentMembershipException extends Exception {
	public NotActiveOrInexistentMembershipException(String errorMessage) {
		super(errorMessage);
	}
}