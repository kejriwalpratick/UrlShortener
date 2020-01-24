package Pratick.urlShortener.models;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Entity
@Table (name = "urls", indexes = @Index(columnList = "original_url"))
public class UrlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "modified_at", nullable = false)
    @Setter
    private Timestamp modifiedAt;

    @Column(name = "original_url", nullable = false, unique = true)
    private String originalUrl;

    @Column(name = "shortened_url", nullable = false, unique = true)
    @Setter
    private String shortenedUrl;

    @Builder
    public UrlEntity(Long id, Timestamp createdAt, Timestamp modifiedAt,String originalUrl, String shortenedUrl){
        this.id=id;
        this.createdAt=createdAt;
        this.modifiedAt=modifiedAt;
        this.originalUrl=originalUrl;
        this.shortenedUrl=shortenedUrl;
    }
}
