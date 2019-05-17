package org.sda.librarymanagement.entity.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum BorrowingPeriodEnum {

	@JsonProperty("TWO_WEEKS")
	TWO_WEEKS(14),
	@JsonProperty("ONE_MONTH")
	ONE_MONTH(30),
	@JsonProperty("TWO_MONTHS")
	TWO_MONTHS(60);

	private final int days;

	private BorrowingPeriodEnum(int days) {
		this.days = days;
	}

	public int getDays() {
		return days;
	}

}