package com.cfrdocarmo.cfrfood.infrastructure.email;

import com.cfrdocarmo.cfrfood.domain.service.EnvioEmailService.Mensagem;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import freemarker.template.Configuration;
import freemarker.template.Template;

@Component
public class ProcessadorEmailTemplate {

    @Autowired
    private Configuration freemarkerConfig;

    protected String processarTemplate(Mensagem mensagem) {
        try {
            Template template = freemarkerConfig.getTemplate(mensagem.getCorpo());

            return FreeMarkerTemplateUtils.processTemplateIntoString(
                    template, mensagem.getVariaveis());
        } catch (Exception e) {
            throw new EmailException("Não foi possível montar o template do e-mail", e);
        }
    }

}