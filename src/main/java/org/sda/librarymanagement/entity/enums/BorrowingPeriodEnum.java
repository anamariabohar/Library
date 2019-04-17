package org.sda.librarymanagement.entity.enums;

public enum BorrowingPeriodEnum {

	TWO_WEEKS(14), ONE_MONTH(30), TWO_MONTHS(60);

	private final int days;

	private BorrowingPeriodEnum(int days) {
		this.days = days;
	}

	public int getDays() {
		return days;
	}

}