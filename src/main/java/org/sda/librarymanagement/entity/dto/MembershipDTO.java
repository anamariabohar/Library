package org.sda.librarymanagement.entity.dto;

import lombok.Data;

@Data
public class MembershipDTO {
	private int membershipID;
	private String membershipType;
	private String startDate;
	private String endDate;
	private int clientId;
}
