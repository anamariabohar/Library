package org.sda.librarymanagement.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.sda.librarymanagement.entity.Client;
import org.sda.librarymanagement.entity.Membership;
import org.sda.librarymanagement.entity.dto.MembershipDTO;
import org.sda.librarymanagement.entity.enums.MembershipTypeEnum;
import org.sda.librarymanagement.service.ClientService;
import org.sda.librarymanagement.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/library")
public class MembershipController {

	@Autowired
	private MembershipService membershipService;

	@Autowired
	private ClientService clientService;

	@PostMapping("/membership")
	public ResponseEntity<Void> addMembership(@RequestBody Membership membership, UriComponentsBuilder builder) {
		membershipService.saveMembership(membership);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder.path("/membership").buildAndExpand(membership.getMembershipID()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	@GetMapping("/membership/{id}")
	public ResponseEntity<Membership> getMembershipById(@PathVariable("id") Long id) {
		Membership membership = membershipService.getOneMembershipById(id);
		return new ResponseEntity<Membership>(membership, HttpStatus.OK);
	}

	@GetMapping("/memberships")
	public ResponseEntity<List<Membership>> getAllMemberships() {
		List<Membership> membership = membershipService.getAllMemberships();
		return new ResponseEntity<List<Membership>>(membership, HttpStatus.OK);
	}

	@PutMapping("/membership/{id}")
	public ResponseEntity<Membership> updateMembership(@PathVariable Long id, @RequestBody Membership membership) {
		membershipService.updateMembership(id, membership);
		return new ResponseEntity<Membership>(membership, HttpStatus.OK);
	}

	@DeleteMapping("/membership/{id}")
	public ResponseEntity<Void> deleteMembership(@PathVariable("id") Long id) {
		membershipService.deleteMembership(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	private MembershipDTO convertFromEntityToDTO(Membership membership) {
		MembershipDTO membershipDTO = new MembershipDTO();
		Client client = clientService.getOneClientById(membership.getClient().getClientId());
		String membershipType = membership.getMembershipType().name();

		membershipDTO.setMembershipID(membership.getMembershipID());
		membershipDTO.setMembershipType(membershipType);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate start = membership.getStartDate();
		LocalDate end = membership.getEndDate();
		String startDate = start.format(formatter);
		String endDate = end.format(formatter);

		membershipDTO.setStartDate(startDate);
		membershipDTO.setEndDate(endDate);

		membershipDTO.setClientId(client.getClientId());
		return membershipDTO;
	}

	private Membership convertFromDTOToEntity(MembershipDTO membershipDTO) {
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
