package org.sda.librarymanagement.repository;

import org.sda.librarymanagement.entity.Membership;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipRepository extends CrudRepository<Membership, Long> {

}