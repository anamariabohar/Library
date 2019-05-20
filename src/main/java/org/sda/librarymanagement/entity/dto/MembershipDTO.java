package org.sda.librarymanagement.entity.dto;

import lombok.Data;

@Data
public class MembershipDTO {
	private Long membershipID;
	private String membershipType;
	private String startDate;
	private String endDate;
	private Long clientId;
}
