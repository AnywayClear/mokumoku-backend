package com.anywayclear.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SellerDto {

    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("image")
    private String image;

    @JsonProperty("email_address")
    private String emailAddress;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("desc")
    private String desc;

    @JsonProperty("company_registration_number")
    private String companyRegistrationNumber;

    @JsonProperty("company_address")
    private String companyAddress;
}
