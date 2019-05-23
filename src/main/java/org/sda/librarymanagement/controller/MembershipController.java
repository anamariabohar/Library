package org.sda.librarymanagement.controller;

import java.util.List;

import org.sda.librarymanagement.entity.Membership;
import org.sda.librarymanagement.entity.dto.MembershipDTO;
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

	@PostMapping("/membership")
	public ResponseEntity<Void> addMembership(@RequestBody MembershipDTO membershipDTO, UriComponentsBuilder builder) {
		Membership membership = membershipService.convertFromDTOToEntity(membershipDTO);
		membership.setEndDate(membership.getStartDate().plusDays(membership.getMembershipType().getDays()));
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
		List<Membership> membership = (List<Membership>) membershipService.getAllMemberships();
		return new ResponseEntity<List<Membership>>(membership, HttpStatus.OK);
	}

	@PutMapping("/membership/{id}")
	public ResponseEntity<Membership> updateMembership(@PathVariable Long id,
			@RequestBody MembershipDTO membershipDTO) {
		Membership membership = membershipService.convertFromDTOToEntity(membershipDTO);
		membershipService.updateMembership(id, membership);
		return new ResponseEntity<Membership>(membership, HttpStatus.OK);
	}

	@DeleteMapping("/membership/{id}")
	public ResponseEntity<Void> deleteMembership(@PathVariable("id") Long id) {
		membershipService.deleteMembership(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

}
