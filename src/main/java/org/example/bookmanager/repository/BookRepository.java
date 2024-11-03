package org.example.bookmanager.repository;

import org.example.bookmanager.entity.Book;
import org.springframework.data.repository.ListCrudRepository;

public interface BookRepository extends ListCrudRepository<Book, Long> {

}
