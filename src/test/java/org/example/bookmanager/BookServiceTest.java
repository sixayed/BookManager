package org.example.bookmanager;

import org.example.bookmanager.entity.Book;
import org.example.bookmanager.exception.BookNotFoundException;
import org.example.bookmanager.repository.BookRepository;
import org.example.bookmanager.service.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;

    @BeforeEach
    void setUp() {
        String title = "Effective Java";
        String author = "Joshua Bloch";
        book = new Book(1L, title, author, LocalDate.now());
    }

    @Test
    @DisplayName("getAllBooks returns all books")
    void getAllBooks_ReturnsAllBooks() {
        // arrange
        List<Book> books = List.of(
                new Book(1L, "Effective Java", "Joshua Bloch", LocalDate.now()),
                new Book(2L, "Spring in Action", "Craig Walls", LocalDate.now())
        );
        when(bookRepository.findAll()).thenReturn(books);

        // act
        List<Book> result = bookService.getAllBooks();

        // assert
        assertEquals(books, result);
        verify(bookRepository).findAll();
    }

    @Test
    @DisplayName("getBookById returns an existing book")
    void getBookById_ExistingId_ReturnsBook() {
        // arrange
        when(bookRepository.findById(any())).thenReturn(Optional.of(book));

        // act
        Book result = bookService.getBookById(1L);

        // assert
        assertEquals(book, result);
        verify(bookRepository).findById(1L);
    }

    @Test
    @DisplayName("getBookById throw not found exception on non-existing book")
    void getBookById_NonExistingId_ThrowsException() {
        // arrange
        when(bookRepository.findById(any())).thenReturn(Optional.empty());

        // act & assert
        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(1L));
        verify(bookRepository).findById(1L);
    }

    @Test
    @DisplayName("addBook saves a new valid book successfully")
    void addBook_ValidRequest_ReturnsCreatedBook() {
        // arrange
        when(bookRepository.save(any())).thenReturn(book);

        // act
        Book result = bookService.addBook(book);

        // arrange
        assertEquals(book, result);
        verify(bookRepository).save(any());
    }

    @Test
    @DisplayName("updateBook update book with correct data successfully")
    void updateBook_ValidRequest_ReturnsUpdatedBook() {
        // assert
        Book updatedBook = new Book(1L, "updated title", "updated author", LocalDate.now());
        when(bookRepository.findById(any())).thenReturn(Optional.of(book));
        when(bookRepository.save(any())).thenReturn(book);

        // act
        Book result = bookService.updateBook(book.getId(), updatedBook);

        // assert
        assertEquals(updatedBook, result);
        verify(bookRepository).save(any());
    }

    @Test
    @DisplayName("updateBook throws exception on non-existing book")
    void updateBook_NonExistingId_ThrowsException() {
        // arrange
        when(bookRepository.findById(any())).thenReturn(Optional.empty());

        //act & assert
        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(book.getId(), book));
        verify(bookRepository).findById(any());
        verify(bookRepository, never()).save(any());
    }

    @Test
    @DisplayName("deleteBook delete book by id")
    void deleteBook_ReturnsVoid() {
        // act
        bookService.deleteBook(1L);

        // assert
        verify(bookRepository).deleteById(1L);
    }
}
