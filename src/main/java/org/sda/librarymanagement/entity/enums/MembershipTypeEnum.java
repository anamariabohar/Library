package org.sda.librarymanagement.entity.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MembershipTypeEnum {
	@JsonProperty("BASIC")
	BASIC(5,30),
	@JsonProperty("PREMIUM")
	PREMIUM(50,45),
	@JsonProperty("LUXURY")
	LUXURY(100,110);

	private final int price;
	private final int days;

	private MembershipTypeEnum(int price, int days) {
		this.price = price;
		this.days=days;
	}

	public int getPrice() {
		return price;
	}
	
	public int getDays() {
		return days;
	}

}