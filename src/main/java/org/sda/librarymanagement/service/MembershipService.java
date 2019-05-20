package org.sda.librarymanagement.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.EntityManager;

import org.sda.librarymanagement.entity.Book;
import org.sda.librarymanagement.entity.BorrowingRegistration;
import org.sda.librarymanagement.entity.Client;
import org.sda.librarymanagement.entity.Membership;
import org.sda.librarymanagement.entity.dto.MembershipDTO;
import org.sda.librarymanagement.entity.enums.BorrowingPeriodEnum;
import org.sda.librarymanagement.entity.enums.MembershipTypeEnum;
import org.sda.librarymanagement.repository.MembershipRepository;
import org.sda.librarymanagement.service.exceptions.BorrowingCannotPassTheEndDateOfTheMembershipException;
import org.sda.librarymanagement.service.exceptions.NotActiveOrInexistentMembershipException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class MembershipService {
	@Autowired
	private MembershipRepository membershipRepository;

	@Autowired
	private ClientService clientService;

	@Autowired
	private EntityManager entityManager;

	public List<Membership> getAllMemberships() {
		return (List<Membership>) membershipRepository.findAll();
	}

	public void saveMembership(@RequestBody Membership membership) {
		membershipRepository.save(membership);
	}

	public Membership getOneMembershipById(@PathVariable Long id) {
		return entityManager.find(Membership.class, id);
	}

	public Membership updateMembership(@PathVariable Long id, @RequestBody Membership membership) {
		Membership existingMembership = entityManager.find(Membership.class, id);
		BeanUtils.copyProperties(membership, existingMembership);
		return membershipRepository.save(existingMembership);
	}

	public Membership deleteMembership(@PathVariable Long id) {
		Membership existingMembership = entityManager.find(Membership.class, id);
		membershipRepository.delete(existingMembership);
		return existingMembership;
	}

	public Membership convertFromDTOToEntity(MembershipDTO membershipDTO) {
		Membership membership = new Membership();
		Client client = clientService.getOneClientById(membershipDTO.getClientId());
		MembershipTypeEnum membershipTypes = MembershipTypeEnum.valueOf(membershipDTO.getMembershipType());

		membership.setMembershipID(membershipDTO.getMembershipID());
		membership.setMembershipType(membershipTypes);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String start = membershipDTO.getStartDate();
		LocalDate startDate = LocalDate.parse(start, formatter);

		membership.setStartDate(startDate);
		membership.setClient(client);

		return membership;
	}

	protected Membership getAvailableMembership(Client client, Book book)
			throws NotActiveOrInexistentMembershipException {
		client = clientService.getOneClientById(client.getClientId());
		Membership membership = client.getMembership();
		membership.setEndDate(membership.getStartDate().plusDays(membership.getMembershipType().getDays()));
		if (membership != null && membership.getEndDate().isBefore(LocalDate.now())) {
			return membership;
		} else {
			if (membership == null || membership.getEndDate()
					.isAfter(LocalDate.now().plusDays(membership.getMembershipType().getDays()))) {
				throw new NotActiveOrInexistentMembershipException(
						"The membership expired or the client doesn't have a membership!");
			}
		}
		return null;
	}

	protected void setStartDateAndDueDateOfTheBorrowing(Membership membership, Book book,
			BorrowingRegistration borrowingRegistration)
			throws NotActiveOrInexistentMembershipException, BorrowingCannotPassTheEndDateOfTheMembershipException {
		borrowingRegistration.setBorrowingDate(LocalDate.now());
		switch (membership.getMembershipType()) {
		case BASIC:
			book.setBorrowingPeriod(BorrowingPeriodEnum.TWO_WEEKS);
			borrowingRegistration
					.setDueDate(borrowingRegistration.getBorrowingDate().plusDays(book.getBorrowingPeriod().getDays()));
			break;
		case PREMIUM:
			book.setBorrowingPeriod(BorrowingPeriodEnum.ONE_MONTH);
			borrowingRegistration
					.setDueDate(borrowingRegistration.getBorrowingDate().plusDays(book.getBorrowingPeriod().getDays()));
			break;
		case LUXURY:
			book.setBorrowingPeriod(BorrowingPeriodEnum.TWO_MONTHS);
			borrowingRegistration
					.setDueDate(borrowingRegistration.getBorrowingDate().plusDays(book.getBorrowingPeriod().getDays()));
			break;
		default:
			break;
		}
		if (borrowingRegistration.getDueDate().isAfter(membership.getEndDate()))
			throw new BorrowingCannotPassTheEndDateOfTheMembershipException(
					"Borrowing period is longer than availability of the membership!");
	}

}