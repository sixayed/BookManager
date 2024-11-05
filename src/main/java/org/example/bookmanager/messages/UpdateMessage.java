package org.example.bookmanager.messages;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMessage implements BookMessage {
    private Long id;
    private String title;
    private String author;
}
