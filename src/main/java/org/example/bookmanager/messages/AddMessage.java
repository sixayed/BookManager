package org.example.bookmanager.messages;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddMessage implements BookMessage {
    private String title;
    private String author;
}
