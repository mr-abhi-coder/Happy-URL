package url.shortener.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "urlmapping")
public class UrlMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "urlmapping_id")
    private Long id;

    private String originalUrl;
    private String shortUrl;
    private int clickCount;
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "urlMapping", cascade = CascadeType.ALL)
    private List<ClickEvent> clickEventList;
}
