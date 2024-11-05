package org.example.bookmanager.messages;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteMessage implements BookMessage {
    private Long id;
}
