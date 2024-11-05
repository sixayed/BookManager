package org.example.bookmanager.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookmanager.entity.Book;
import org.example.bookmanager.exception.BookNotFoundException;
import org.example.bookmanager.repository.BookRepository;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final MessageSource messageSource;

    @Override
    public List<Book> getAllBooks() {
        log.info("Fetching all books");
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(@NonNull Long id) {
        log.info("Fetching book with id={}", id);
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(messageSource.getMessage(
                        "manager.errors.book.not_found",
                        new Object[]{id},
                        Locale.getDefault())
                ));
    }

    @Override
    @Transactional
    public Book addBook(@NonNull Book book) {
        book.setPublishedDate(LocalDate.now());
        log.info("Adding a new book: {}", book);
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book updateBook(@NonNull Long id, @NonNull Book updatedBook) {
        log.info("Updating book with id={} to {}", id, updatedBook);

        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(updatedBook.getTitle());
                    book.setAuthor(updatedBook.getAuthor());
                    return bookRepository.save(book);
                })
                .orElseThrow(() -> new BookNotFoundException(messageSource.getMessage(
                        "manager.errors.book.not_found",
                        new Object[]{id},
                        Locale.getDefault())
                ));

    }

    @Override
    @Transactional
    public void deleteBook(@NonNull Long id) {
        log.info("Deleting book with id={}", id);
        bookRepository.deleteById(id);
    }
}
