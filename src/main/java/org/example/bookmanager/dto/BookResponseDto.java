package org.example.bookmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDto {
    private Long id;
    private String title;
    private String author;
    private LocalDate publishedDate;
}
