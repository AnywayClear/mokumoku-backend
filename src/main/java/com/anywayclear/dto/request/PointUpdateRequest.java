package com.anywayclear.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PointUpdateRequest {
    @NotNull
    private int balance;
}
