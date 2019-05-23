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
import org.sda.librarymanagement.entity.dto.MembershipDTO;
import org.sda.librarymanagement.entity.enums.BorrowingPeriodEnum;
import org.sda.librarymanagement.entity.enums.MembershipTypeEnum;
import org.sda.librarymanagement.repository.MembershipRepository;
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
public class MembershipService {
	@Autowired
	private MembershipRepository membershipRepository;

	@Autowired
	private ClientService clientService;

	@Autowired
	private EntityManager entityManager;

	@Transactional
	public List<Membership> getAllMemberships() {
		return (List<Membership>) membershipRepository.findAll();
	}

	@Transactional
	public void saveMembership(@RequestBody Membership membership) {
		membershipRepository.save(membership);
	}

	@Transactional
	public Membership getOneMembershipById(@PathVariable Long id) {
		return entityManager.find(Membership.class, id);
	}

	@Transactional
	public Membership updateMembership(@PathVariable Long id, @RequestBody Membership membership) {
		Membership existingMembership = entityManager.find(Membership.class, id);
		BeanUtils.copyProperties(membership, existingMembership);
		return membershipRepository.save(existingMembership);
	}

	@Transactional
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
		LocalDate startDate = LocalDate.parse(membershipDTO.getStartDate(), formatter);

		membership.setStartDate(startDate);

		membership.setClient(client);

		return membership;
	}

	protected Membership getAvailableMembership(Client client, Book book)
			throws NotActiveOrInexistentMembershipException {
		client = clientService.getOneClientById(client.getClientId());
		Membership membership = client.getMembership();
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

	protected void setDueDateOfTheBorrowing(Membership membership, BorrowingRegistration borrowingRegistration)
			throws NotActiveOrInexistentMembershipException,
			BorrowingCannotBeDoneBeforeOrAfterAvailabilityOfTheMembershipException,
			BorrowingAndReturnDateConflictException {
		switch (membership.getMembershipType()) {
		case BASIC:
			borrowingRegistration.setBorrowingPeriod(BorrowingPeriodEnum.TWO_WEEKS);
			borrowingRegistration.setDueDate(borrowingRegistration.getBorrowingDate()
					.plusDays(borrowingRegistration.getBorrowingPeriod().getDays()));
			break;
		case PREMIUM:
			borrowingRegistration.setBorrowingPeriod(BorrowingPeriodEnum.ONE_MONTH);
			borrowingRegistration.setDueDate(borrowingRegistration.getBorrowingDate()
					.plusDays(borrowingRegistration.getBorrowingPeriod().getDays()));
			break;
		case LUXURY:
			borrowingRegistration.setBorrowingPeriod(BorrowingPeriodEnum.TWO_MONTHS);
			borrowingRegistration.setDueDate(borrowingRegistration.getBorrowingDate()
					.plusDays(borrowingRegistration.getBorrowingPeriod().getDays()));
			break;
		default:
			break;
		}
		if (borrowingRegistration.getBorrowingDate().isBefore(membership.getStartDate())
				|| borrowingRegistration.getDueDate().isAfter(membership.getEndDate()))
			throw new BorrowingCannotBeDoneBeforeOrAfterAvailabilityOfTheMembershipException(
					"Borrowing period is before or after than availability of the membership!");
		if (borrowingRegistration.getReturnDate() != null
				&& (borrowingRegistration.getReturnDate().isBefore(borrowingRegistration.getBorrowingDate())
						|| borrowingRegistration.getBorrowingDate().isAfter(borrowingRegistration.getReturnDate())))
			throw new BorrowingAndReturnDateConflictException(
					"Borrowing date is in conflict with return date of the book!");
	}

}