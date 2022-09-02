package com.cfrdocarmo.cfrfood.core.security.authorizationserver;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Configuration
@Getter
@Setter
@Validated
@ConfigurationProperties("cfrdocarmofood.auth")
public class CFRdoCarmoFoodSecutiryProperties {

    @NotBlank
    private String providerUrl;

}
