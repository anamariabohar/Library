package org.sda.librarymanagement.entity.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MembershipTypeEnum {
	@JsonProperty("BASIC")
	BASIC(5),
	@JsonProperty("PREMIUM")
	PREMIUM(50),
	@JsonProperty("LUXURY")
	LUXURY(100);

	private final int price;

	private MembershipTypeEnum(int price) {
		this.price = price;
	}

	public int getPrice() {
		return price;
	}
}