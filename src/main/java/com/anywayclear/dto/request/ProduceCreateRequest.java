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
    private String desc;
    private String image;
    @NotNull
    private int startPrice;
    @NotNull
    private int kg;
    @NotNull
    private int ea;
    @NotNull
//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") // 기본 형태는 자동 바인딩
    private LocalDateTime startDate;
    @NotNull
//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;

    @Builder
    public ProduceCreateRequest(String name, String desc, String image, int startPrice, int kg, int ea, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.desc = desc;
        this.image = image;
        this.startPrice = startPrice;
        this.kg = kg;
        this.ea = ea;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
