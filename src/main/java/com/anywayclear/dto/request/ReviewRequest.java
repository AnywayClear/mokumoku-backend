package com.anywayclear.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class ReviewRequest {


    @NotBlank
    private String comment;

    @NotNull
    private int score;

    @Builder
    public ReviewRequest(String comment, int score) {
        this.comment = comment;
        this.score = score;
    }
}
