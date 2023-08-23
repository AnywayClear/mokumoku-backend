package com.anywayclear.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ProduceCreateRequest {

    @NotBlank
    private String name;
    private String description;
    private String image;
    @NotNull
    private int startPrice;
    @NotNull
    private int kg;
    @NotNull
    private int ea;
    @NotNull
    private LocalDateTime startDate;

    @Builder
    public ProduceCreateRequest(String name, String description, String image, int startPrice, int kg, int ea, LocalDateTime startDate) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.startPrice = startPrice;
        this.kg = kg;
        this.ea = ea;
        this.startDate = startDate;
    }
}
