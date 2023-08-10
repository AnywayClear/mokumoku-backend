package com.anywayclear.dto.request;

import com.anywayclear.entity.Auction;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewCreateRequest {


    @NotBlank
    private String comment;

    @NotNull
    private int score;

    @Builder
    public ReviewCreateRequest(String comment, int score) {
        this.comment = comment;
        this.score = score;
    }
}
