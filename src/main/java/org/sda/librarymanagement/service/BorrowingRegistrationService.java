package org.sda.librarymanagement.service;

import java.util.List;

import javax.persistence.EntityManager;

import org.sda.librarymanagement.entity.Book;
import org.sda.librarymanagement.entity.BorrowingRegistration;
import org.sda.librarymanagement.entity.Client;
import org.sda.librarymanagement.entity.Membership;
import org.sda.librarymanagement.entity.dto.BorrowingRegistrationDTO;
import org.sda.librarymanagement.repository.BorrowingRegistrationRepository;
import org.sda.librarymanagement.service.exceptions.BorrowingCannotPassTheEndDateOfTheMembershipException;
import org.sda.librarymanagement.service.exceptions.NotActiveOrInexistentMembershipException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
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

	public List<BorrowingRegistration> getAllBorrowingRegistrations() {
		return (List<BorrowingRegistration>) borrowingRegistrationRepository.findAll();
	}

	public BorrowingRegistration getOneBorrowingRegistrationById(@PathVariable Long id) {
		return entityManager.find(BorrowingRegistration.class, id);
	}

	public void saveBorrowingRegistration(@RequestBody BorrowingRegistration borrowingRegistration) {
		borrowingRegistrationRepository.save(borrowingRegistration);
	}

	public BorrowingRegistration updateBorrowingRegistration(@PathVariable Long id,
			@RequestBody BorrowingRegistration borrowingRegistration) {
		BorrowingRegistration existingBorrowingRegistration = entityManager.find(BorrowingRegistration.class, id);
		BeanUtils.copyProperties(borrowingRegistration, existingBorrowingRegistration);
		return borrowingRegistrationRepository.save(existingBorrowingRegistration);
	}

	public BorrowingRegistration deleteBorrowingRegistration(@PathVariable Long id) {
		BorrowingRegistration existingBorrowingRegistration = entityManager.find(BorrowingRegistration.class, id);
		borrowingRegistrationRepository.delete(existingBorrowingRegistration);
		return existingBorrowingRegistration;
	}

	public BorrowingRegistration convertFromDTOToEntity(BorrowingRegistrationDTO borrowingRegistrationDTO) {

		BorrowingRegistration borrowingRegistration = new BorrowingRegistration();
		Client client = clientService.getOneClientById(borrowingRegistrationDTO.getClientId());
		Book book = bookService.getOneBookById(borrowingRegistrationDTO.getBookId());

		borrowingRegistration.setBorrowingRegistrationId(borrowingRegistrationDTO.getBorrowingRegistrationId());

		borrowingRegistration.setBook(book);
		borrowingRegistration.setClient(client);

		Membership membership = borrowingRegistration.getClient().getMembership();
		borrowingRegistration.getClient().setMembership(membership);

		try {
			membershipService.getAvailableMembership(borrowingRegistration.getClient(),
					borrowingRegistration.getBook());

			membershipService.setStartDateAndDueDateOfTheBorrowing(borrowingRegistration.getClient().getMembership(),
					borrowingRegistration.getBook(), borrowingRegistration);

		} catch (NotActiveOrInexistentMembershipException e) {
			e.printStackTrace();
		} catch (BorrowingCannotPassTheEndDateOfTheMembershipException e) {
			e.printStackTrace();
		}

		return borrowingRegistration;
	}

}
