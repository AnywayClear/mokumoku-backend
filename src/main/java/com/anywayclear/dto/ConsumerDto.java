package com.anywayclear.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ConsumerDto {

    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("image")
    private String image;

    @JsonProperty("email_address")
    private String emailAddress;


}
