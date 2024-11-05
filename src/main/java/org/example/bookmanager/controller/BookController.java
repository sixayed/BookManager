package org.example.bookmanager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookmanager.convertor.ModelConvertor;
import org.example.bookmanager.dto.BookRequestDto;
import org.example.bookmanager.dto.BookResponseDto;
import org.example.bookmanager.messages.AddMessage;
import org.example.bookmanager.messages.DeleteMessage;
import org.example.bookmanager.messages.UpdateMessage;
import org.example.bookmanager.entity.Book;
import org.example.bookmanager.service.BookMessageProducer;
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
    private final BookMessageProducer bookMessageProducer;

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
    public ResponseEntity<Void> addBook(
            @Valid @RequestBody BookRequestDto requestDto,
            BindingResult bindingResult
    ) throws BindException {

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        } else {
            AddMessage addMessage = new AddMessage(requestDto.getTitle(), requestDto.getAuthor());
            bookMessageProducer.sendMessage(addMessage);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequestDto requestDto) {
        UpdateMessage updateMessage = new UpdateMessage(id, requestDto.getTitle(), requestDto.getAuthor());
        bookMessageProducer.sendMessage(updateMessage);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        DeleteMessage deleteMessage = new DeleteMessage(id);
        bookMessageProducer.sendMessage(deleteMessage);
        return ResponseEntity.noContent().build();
    }
}
