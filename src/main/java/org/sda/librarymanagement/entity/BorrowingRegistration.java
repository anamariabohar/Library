package org.sda.librarymanagement.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.sda.librarymanagement.entity.enums.BorrowingPeriodEnum;

import lombok.Data;

@Entity
@Data
@Table(name = "Borrowing_Registrations")
public class BorrowingRegistration {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "registration_id")
	private Long borrowingRegistrationId;
	@ManyToOne
	@JoinColumn(name = "client_id")
	private Client client;
	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;
	@Column(name = "borrowing_date")
	private LocalDate borrowingDate;
	@Column(name = "due_date")
	private LocalDate dueDate;
	@Column(name = "return_date", nullable = true)
	private LocalDate returnDate;
	@Column(name = "borrowing_period")
	@Enumerated(EnumType.STRING)
	private BorrowingPeriodEnum borrowingPeriod;
}