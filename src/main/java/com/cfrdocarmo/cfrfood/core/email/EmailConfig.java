package com.cfrdocarmo.cfrfood.core.email;

import com.cfrdocarmo.cfrfood.domain.service.EnvioEmailService;
import com.cfrdocarmo.cfrfood.infrastructure.email.FakeEnvioEmailService;
import com.cfrdocarmo.cfrfood.infrastructure.email.SandboxEnvioEmailService;
import com.cfrdocarmo.cfrfood.infrastructure.email.SmtpEnvioEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class EmailConfig {

    @Autowired
    private EmailProperties emailProperties;

    @Bean
    public EnvioEmailService envioEmailService() {
        switch (emailProperties.getImpl()) {
            case FAKE:
                return new FakeEnvioEmailService();
            case SMTP:
                return new SmtpEnvioEmailService();
            case SANDBOX:
                return new SandboxEnvioEmailService();
            default:
                return null;
        }
    }

}
