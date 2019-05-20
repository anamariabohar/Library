package org.sda.librarymanagement.entity.dto;

import lombok.Data;

@Data
public class ClientDTO {

	private Long clientId;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String phone;
	private String email;
	private Long membership;

}
