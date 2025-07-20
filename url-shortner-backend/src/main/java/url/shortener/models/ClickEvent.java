package url.shortener.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "clickevent")
public class ClickEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clickevent_id")
    private Long id;

    private LocalDateTime clickDate;

    @ManyToOne
    @JoinColumn(name = "urlmapping_id")
    private UrlMapping urlMapping;
}
