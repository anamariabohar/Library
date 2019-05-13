package org.sda.librarymanagement.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Entity
@Data
@Table(name = "Clients")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })

public class Client {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "client_id")
	private Long clientId;
	@Column(name = "client_username")
	private String username;
	@Column(name = "client_password")
	private String password;
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	@Column(name = "phone")
	private String phone;
	@Column(name = "email")
	private String email;
	@OneToMany(mappedBy = "client")
	private List<Membership> memberships;

	@Override
	public String toString() {
		return "Client [clientId=" + clientId + ", username=" + username + ", password=" + password + ", firstName="
				+ firstName + ", lastName=" + lastName + ", phone=" + phone + ", email=" + email + "]";
	}

}