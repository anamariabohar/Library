package org.sda.librarymanagement.entity.enums;

public enum MembershipTypeEnum {

	BASIC(5), PREMIUM(50), LUXURY(100);

	private final int price;

	private MembershipTypeEnum(int price) {
		this.price = price;
	}

	public int getPrice() {
		return price;
	}

}