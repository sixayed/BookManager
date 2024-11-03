package org.example.bookmanager.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestDto {
    @NotNull(message = "{books.errors.title_is_null}")
    @Size(min = 3, max = 100, message = "{books.errors.title_invalid_length}")
    private String title;

    @NotNull(message = "{books.errors.author_is_null}")
    @Size(min = 5, max = 50, message = "{books.errors.author_invalid_length}")
    private String author;
}
