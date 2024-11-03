package org.example.bookmanager.convertor;

import org.example.bookmanager.dto.BookRequestDto;
import org.example.bookmanager.dto.BookResponseDto;
import org.example.bookmanager.entity.Book;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ModelConvertor {

    private final ModelMapper modelMapper = new ModelMapper();

    public Book convertToEntity(BookRequestDto requestDto) {
        return modelMapper.map(requestDto, Book.class);
    }

    public BookResponseDto convertToDto(Book entity) {
        return modelMapper.map(entity, BookResponseDto.class);
    }
}
