package org.sda.librarymanagement.service;

import java.util.List;

import org.sda.librarymanagement.entity.Membership;
import org.sda.librarymanagement.repository.MembershipRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class MembershipService {
	@Autowired
	private MembershipRepository membershipRepository;

	public List<Membership> getAllMemberships() {
		return membershipRepository.findAll();
	}

	public void saveMembership(Membership membership) {
		membershipRepository.saveAndFlush(membership);
	}

	public Membership getOneClientById(Long id) {
		return membershipRepository.getOne(id);
	}

	public Membership updateBook(@PathVariable Long id, @RequestBody Membership client) {
		Membership existingMembership = membershipRepository.getOne(id);
		BeanUtils.copyProperties(client, existingMembership);
		return membershipRepository.saveAndFlush(existingMembership);
	}

	public Membership deleteBook(@PathVariable Long id) {
		Membership existingMembership = membershipRepository.getOne(id);
		membershipRepository.delete(existingMembership);
		return existingMembership;
	}
}