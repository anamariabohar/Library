package org.sda.librarymanagement.repository;

import org.sda.librarymanagement.entity.BookCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCategoryRepository extends CrudRepository<BookCategory, Long> {

}