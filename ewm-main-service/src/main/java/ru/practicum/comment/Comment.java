package ru.practicum.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "text", nullable = false)
    private String text;
    @Column(name = "event_id")
    private Integer event;
    @Column(name = "author_id", nullable = false)
    private Integer author;
    @Column(name = "created", nullable = false)
    LocalDateTime created;
}
