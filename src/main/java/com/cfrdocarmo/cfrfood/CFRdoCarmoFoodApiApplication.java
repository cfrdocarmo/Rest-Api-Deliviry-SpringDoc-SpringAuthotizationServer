package com.cfrdocarmo.cfrfood;

import java.util.TimeZone;

import com.cfrdocarmo.cfrfood.core.io.Base64ProtocoloResolver;
import com.cfrdocarmo.cfrfood.infrastructure.repository.CustomJpaRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomJpaRepositoryImpl.class)
public class CFRdoCarmoFoodApiApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		var app = new SpringApplication(CFRdoCarmoFoodApiApplication.class);
		app.addListeners(new Base64ProtocoloResolver());
		app.run(args);
	}

}

