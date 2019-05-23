package org.sda.librarymanagement.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.sda.librarymanagement.entity.Book;
import org.sda.librarymanagement.entity.BorrowingRegistration;
import org.sda.librarymanagement.entity.Client;
import org.sda.librarymanagement.entity.Membership;
import org.sda.librarymanagement.entity.dto.BorrowingRegistrationDTO;
import org.sda.librarymanagement.repository.BorrowingRegistrationRepository;
import org.sda.librarymanagement.service.exceptions.BorrowingAndReturnDateConflictException;
import org.sda.librarymanagement.service.exceptions.BorrowingCannotBeDoneBeforeOrAfterAvailabilityOfTheMembershipException;
import org.sda.librarymanagement.service.exceptions.NotActiveOrInexistentMembershipException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@Transactional
public class BorrowingRegistrationService {

	@Autowired
	private BorrowingRegistrationRepository borrowingRegistrationRepository;

	@Autowired
	private ClientService clientService;

	@Autowired
	private BookService bookService;

	@Autowired
	private MembershipService membershipService;

	@Autowired
	private EntityManager entityManager;

	@Transactional
	public List<BorrowingRegistration> getAllBorrowingRegistrations() {
		return (List<BorrowingRegistration>) borrowingRegistrationRepository.findAll();
	}

	@Transactional
	public BorrowingRegistration getOneBorrowingRegistrationById(@PathVariable Long id) {
		return entityManager.find(BorrowingRegistration.class, id);
	}

	@Transactional
	public void saveBorrowingRegistration(@RequestBody BorrowingRegistration borrowingRegistration) {
		borrowingRegistrationRepository.save(borrowingRegistration);
	}

	@Transactional
	public BorrowingRegistration updateBorrowingRegistration(@PathVariable Long id,
			@RequestBody BorrowingRegistration borrowingRegistration) {
		BorrowingRegistration existingBorrowingRegistration = entityManager.find(BorrowingRegistration.class, id);
		BeanUtils.copyProperties(borrowingRegistration, existingBorrowingRegistration);
		return borrowingRegistrationRepository.save(existingBorrowingRegistration);
	}

	@Transactional
	public BorrowingRegistration deleteBorrowingRegistration(@PathVariable Long id) {
		BorrowingRegistration existingBorrowingRegistration = entityManager.find(BorrowingRegistration.class, id);
		borrowingRegistrationRepository.delete(existingBorrowingRegistration);
		return existingBorrowingRegistration;
	}

	@Transactional
	public BorrowingRegistration convertFromDTOToEntity(BorrowingRegistrationDTO borrowingRegistrationDTO) {

		BorrowingRegistration borrowingRegistration = new BorrowingRegistration();
		Client client = clientService.getOneClientById(borrowingRegistrationDTO.getClientId());
		Book book = bookService.getOneBookById(borrowingRegistrationDTO.getBookId());

		borrowingRegistration.setBorrowingRegistrationId(borrowingRegistrationDTO.getBorrowingRegistrationId());

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate borrowingDate = LocalDate.parse(borrowingRegistrationDTO.getBorrowingDate(), dateTimeFormatter);
		borrowingRegistration.setBorrowingDate(borrowingDate);

		try {
			if (book.isBorrowingTypeAtHome() == false) {
				borrowingRegistration.setReturnDate(borrowingRegistration.getBorrowingDate());
			} else {
				LocalDate returnDate = LocalDate.parse(borrowingRegistrationDTO.getReturnDate(), dateTimeFormatter);
				borrowingRegistration.setReturnDate(returnDate);
			}

		} catch (NullPointerException e) {
			// do nothing
		} finally {
			borrowingRegistration.setBook(book);
			borrowingRegistration.setClient(client);
		}

		Membership membership = borrowingRegistration.getClient().getMembership();
		borrowingRegistration.getClient().setMembership(membership);

		try {
			membershipService.getAvailableMembership(borrowingRegistration.getClient(),
					borrowingRegistration.getBook());

			membershipService.setDueDateOfTheBorrowing(borrowingRegistration.getClient().getMembership(),
					borrowingRegistration);

		} catch (NotActiveOrInexistentMembershipException e) {
			e.printStackTrace();
		} catch (BorrowingCannotBeDoneBeforeOrAfterAvailabilityOfTheMembershipException e) {
			e.printStackTrace();
		} catch (BorrowingAndReturnDateConflictException e) {
			e.printStackTrace();
		}

		return borrowingRegistration;
	}

}
