package org.sda.librarymanagement.repository;

import org.sda.librarymanagement.entity.BorrowingRegistration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowingRegistrationRepository extends CrudRepository<BorrowingRegistration, Long> {

}
