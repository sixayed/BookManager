package org.example.bookmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bookmanager.controller.BookController;
import org.example.bookmanager.convertor.ModelConvertor;
import org.example.bookmanager.dto.BookRequestDto;
import org.example.bookmanager.dto.BookResponseDto;
import org.example.bookmanager.entity.Book;
import org.example.bookmanager.exception.BookNotFoundException;
import org.example.bookmanager.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private ModelConvertor modelConvertor;

    @Autowired
    private ObjectMapper objectMapper;

    private BookRequestDto bookRequestDto;
    private Book book;
    private BookResponseDto bookResponseDto;
    private final String errorMessage = "Book not found";

    @BeforeEach
    void setUp() {
        String title = "Effective Java";
        String author = "Joshua Block";
        bookRequestDto = new BookRequestDto(title, author);
        book = new Book(1L, title, author, LocalDate.now());
        bookResponseDto = new BookResponseDto(1L, title, author, LocalDate.now());
    }

    @Test
    @DisplayName("addBook creates a new book when request is valid")
    void addBook_ValidRequest_ReturnsCreatedEntity() throws Exception {
        // arrange
        Mockito.when(modelConvertor.convertToEntity(any())).thenReturn(book);
        Mockito.when(modelConvertor.convertToDto(any())).thenReturn(bookResponseDto);
        Mockito.when(bookService.addBook(any(Book.class))).thenReturn(book);

        // act & assert
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(bookResponseDto.getId()));
    }

    @Test
    @DisplayName("addBook returns BadRequest on invalid request")
    void addBook_InvalidRequest_ReturnsBadRequest() throws Exception {
        // arrange
        BookRequestDto invalidRequestDto = new BookRequestDto(null, null);

        // act & assert
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("getBookById returns an existing book")
    void getBookById_ExistingId_ReturnsBook() throws Exception {
        // arrange
        Mockito.when(modelConvertor.convertToDto(any())).thenReturn(bookResponseDto);
        Mockito.when(bookService.getBookById(book.getId())).thenReturn(book);

        // act & assert
        mockMvc.perform(get("/api/books/{id}", book.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookResponseDto.getId()));
    }

    @Test
    @DisplayName("getBookById throw not found exception on non-existing book")
    void getBookById_NonExistingId_ReturnsNotFound() throws Exception {
        // arrange
        Mockito.when(bookService.getBookById(any())).thenThrow(new BookNotFoundException(errorMessage));

        // act & assert
        mockMvc.perform(get("/api/books/{id}", 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value(errorMessage));
    }

    @Test
    @DisplayName("getAllBook returns list of all books")
    void getAllBook_ReturnsAllBooks() throws Exception {
        // arrange
        List<Book> books = List.of(book, new Book(2L, "Spring in Action", "Craig Walls", LocalDate.now()));

        Mockito.when(bookService.getAllBooks()).thenReturn(books);

        // act & assert
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("deleteBook deletes book by id")
    void deleteBookById_ReturnsNoContent() throws Exception {
        // act & assert
        mockMvc.perform(delete("/api/books/{id}", book.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("updateBook update an existing book")
    void updateBook_ExistingId_ReturnsUpdatedBook() throws Exception {
        // arrange
        String newTitle = "updated title";
        String newAuthor = "updated author";
        BookRequestDto updatedRequestDto = new BookRequestDto(newTitle, newAuthor);
        Book updatedBook = new Book(book.getId(), newTitle, newAuthor, LocalDate.now());
        BookResponseDto updatedResponseDto = new BookResponseDto(book.getId(), newTitle, newAuthor, LocalDate.now());

        Mockito.when(modelConvertor.convertToEntity(any())).thenReturn(updatedBook);
        Mockito.when(modelConvertor.convertToDto(any())).thenReturn(updatedResponseDto);
        Mockito.when(bookService.updateBook(any(), any())).thenReturn(updatedBook);

        // act & assert
        mockMvc.perform(put("/api/books/{id}", book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(updatedResponseDto.getTitle()))
                .andExpect(jsonPath("$.author").value(updatedResponseDto.getAuthor()));
    }

    @Test
    @DisplayName("updateBook throw not found exception on non-existing book")
    void updateBook_NonExistingId_ReturnsNotFound() throws Exception {
        // arrange
        BookRequestDto updatedRequestDto = new BookRequestDto("title", "author");

        Mockito.when(bookService.updateBook(any(), any())).thenThrow(new BookNotFoundException(errorMessage));

        // act & assert
        mockMvc.perform(put("/api/books/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRequestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value(errorMessage));
    }
}
