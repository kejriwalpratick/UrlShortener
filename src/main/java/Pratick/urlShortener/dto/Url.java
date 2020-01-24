package Pratick.urlShortener.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.sql.Timestamp;

@Builder
@Getter
public class Url {

    @Null(message = "Id should be null")
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Timestamp createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Timestamp modifiedAt;

    @NotNull(message = "Original url should not be null")
    private String originalUrl;

    @Null(message = "shortened url should be null")
    private String shortenedUrl;
}
