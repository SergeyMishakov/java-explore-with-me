package ru.practicum.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.enums.RequestState;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "participation_requests")
public class ParticipationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "event_id", nullable = false)
    private int event;
    @Column(name = "requester_id", nullable = false)
    private int requester;
    @Column(name = "created", nullable = false)
    private LocalDateTime created;
    @Column(name = "status", nullable = false)
    private RequestState status;
}
