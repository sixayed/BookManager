package org.example.bookmanager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookmanager.convertor.ModelConvertor;
import org.example.bookmanager.dto.BookRequestDto;
import org.example.bookmanager.dto.BookResponseDto;
import org.example.bookmanager.entity.Book;
import org.example.bookmanager.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final ModelConvertor modelConvertor;

    @GetMapping
    public List<BookResponseDto> getAllBooks() {
        return bookService.getAllBooks().stream()
                .map(modelConvertor::convertToDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(modelConvertor.convertToDto(book));
    }

    @PostMapping
    public ResponseEntity<BookResponseDto> addBook(
            @Valid @RequestBody BookRequestDto requestDto,
            BindingResult bindingResult
    ) throws BindException {

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        } else {
            Book book = modelConvertor.convertToEntity(requestDto);
            Book addedBook = bookService.addBook(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(modelConvertor.convertToDto(addedBook));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDto> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequestDto requestDto) {
        Book book = modelConvertor.convertToEntity(requestDto);
        Book updatedBook = bookService.updateBook(id, book);
        return ResponseEntity.ok(modelConvertor.convertToDto(updatedBook));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
