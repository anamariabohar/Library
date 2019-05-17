package org.sda.librarymanagement.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.EntityManager;

import org.sda.librarymanagement.entity.Book;
import org.sda.librarymanagement.entity.Client;
import org.sda.librarymanagement.entity.Membership;
import org.sda.librarymanagement.entity.dto.MembershipDTO;
import org.sda.librarymanagement.entity.enums.BorrowingPeriodEnum;
import org.sda.librarymanagement.entity.enums.MembershipTypeEnum;
import org.sda.librarymanagement.repository.MembershipRepository;
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

	public Iterable<Membership> getAllMemberships() {
		return membershipRepository.findAll();
	}

	public void saveMembership(Membership membership) {
		membershipRepository.save(membership);
	}

	public Membership getOneMembershipById(Long id) {
		return entityManager.find(Membership.class, id);
	}

	public Iterable<Membership> getMembershipsByIds(List<Long> ids) {
		return membershipRepository.findAllById(ids);
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

	private Membership verifyIfTheClientHasActiveMembership(Client client)
			throws NotActiveOrInexistentMembershipException {
		client = clientService.getOneClientById(client.getClientId());
		List<Membership> memberships = client.getMemberships();
		Membership membership = memberships.stream().reduce((first, second) -> second).orElse(null);
		if (membership != null & membership.getEndDate().isBefore(LocalDate.now())) {
			return membership;
		} else {
			if (membership == null || membership.getEndDate().isAfter(LocalDate.now())) {
				throw new NotActiveOrInexistentMembershipException(
						"The membership expired or the client does not have a membership!");
			}
		}
		return null;
	}

	private BorrowingPeriodEnum getBorrowingPeriodByMembership(Membership membership, Book book) {
		BorrowingPeriodEnum borrowingPeriodByMembership = book.getBorrowingPeriod();
		switch (membership.getMembershipType()) {
		case BASIC:
			borrowingPeriodByMembership = BorrowingPeriodEnum.TWO_WEEKS;
			break;
		case PREMIUM:
			borrowingPeriodByMembership = BorrowingPeriodEnum.ONE_MONTH;
			break;
		case LUXURY:
			borrowingPeriodByMembership = BorrowingPeriodEnum.TWO_MONTHS;
			break;
		default:
			break;
		}
		return borrowingPeriodByMembership;
	}

	public boolean hasBorrowingPeriodPassedTheEndDateOfTheMembership(Client client, Membership membership, Book book)
			throws NotActiveOrInexistentMembershipException {
		Membership actualMembershipOfTheClient = verifyIfTheClientHasActiveMembership(client);
		BorrowingPeriodEnum borrowingPeriod = getBorrowingPeriodByMembership(membership, book);
		LocalDate borrowingPeriodOfThePlusStartDateOfTheMembership = actualMembershipOfTheClient.getStartDate()
				.plusDays(borrowingPeriod.getDays());
		LocalDate endDaDateOfTheMembership = actualMembershipOfTheClient.getEndDate();
		if (borrowingPeriodOfThePlusStartDateOfTheMembership.isAfter(endDaDateOfTheMembership))
			return true;
		return false;
	}

	public Membership convertFromDTOToEntity(MembershipDTO membershipDTO) {
		Membership membership = new Membership();
		Client client = clientService.getOneClientById(membershipDTO.getClientId());
		MembershipTypeEnum membershipTypes = MembershipTypeEnum.valueOf(membershipDTO.getMembershipType());

		membership.setMembershipID(membershipDTO.getMembershipID());
		membership.setMembershipType(membershipTypes);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String start = membershipDTO.getStartDate();
		String end = membershipDTO.getEndDate();
		LocalDate startDate = LocalDate.parse(start, formatter);
		LocalDate endDate = LocalDate.parse(end, formatter);

		membership.setStartDate(startDate);
		membership.setEndDate(endDate);

		membership.setClient(client);
		return membership;
	}

}