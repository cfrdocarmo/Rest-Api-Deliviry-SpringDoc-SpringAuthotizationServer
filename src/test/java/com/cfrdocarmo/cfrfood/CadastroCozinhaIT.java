package com.cfrdocarmo.cfrfood;

import com.cfrdocarmo.cfrfood.domain.model.Cozinha;
import com.cfrdocarmo.cfrfood.domain.repository.CozinhaRepository;
import com.cfrdocarmo.cfrfood.util.ResourceUtils;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import com.cfrdocarmo.cfrfood.util.DatabaseCleaner;
import io.restassured.RestAssured;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroCozinhaIT {

	public static final int COZINHA_ID_INEXISTENTE = 100;

	private Cozinha cozinhaAmericana;
	private String jsonCozinhaChinesa;
	private int quantidadeDeCozinhasCadastradas;

	@LocalServerPort
	private int port;

	@Autowired
	private DatabaseCleaner databaseCleaner;

	@Autowired
	private CozinhaRepository cozinhaRepository;

	@BeforeEach
	public void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/cozinhas";

		databaseCleaner.clearTables();
		prepararDados();

		jsonCozinhaChinesa = ResourceUtils.getContentFromResource("/json/cozinhaChinesa.json");
	}

	private void prepararDados() {
		Cozinha cozinhaTailandesa = new Cozinha();
		cozinhaTailandesa.setNome("Tailandesa");
		cozinhaRepository.save(cozinhaTailandesa);

		cozinhaAmericana = new Cozinha();
		cozinhaAmericana.setNome("Americana");
		cozinhaRepository.save(cozinhaAmericana);

		quantidadeDeCozinhasCadastradas = (int) cozinhaRepository.count();
	}

	@Test
	public void deveRetornar200_QuandoConsultarCozinhas() {

				given()
					.accept(ContentType.JSON)
				.when()
					.get()
				.then()
					.statusCode(HttpStatus.OK.value());
	}

	@Test
	public void deveRetornarAQuantidadeCorretaDeCozinhas_QuandoConsultarCozinhas() {

				given()
					.accept(ContentType.JSON)
				.when()
					.get()
				.then()
					.body("", hasSize(quantidadeDeCozinhasCadastradas))
					.body("nome", hasItems("Americana", "Tailandesa"));
	}

	@Test
	public void deveRetornarStatus201_QuandoCadastrarCozinha() {
				given()
					.body(jsonCozinhaChinesa)
					.contentType(ContentType.JSON)
					.accept(ContentType.JSON)
				.when()
					.post()
				.then()
					.statusCode(HttpStatus.CREATED.value());
	}

	@Test
	public void deveRetornarRespostaEStatusCorretos_QuandoConsultarCozinhaExistente() {
				given()
					.pathParam("cozinhaId", cozinhaAmericana.getId())
					.accept(ContentType.JSON)
				.when()
					.get("/{cozinhaId}")
				.then()
					.statusCode(HttpStatus.OK.value())
					.body("nome", equalTo(cozinhaAmericana.getNome()));

	}

	@Test
	public void deveRetornarRespostaEStatusCorretos404_QuandoConsultarCozinhaInexistente() {
		        given()
				    .pathParam("cozinhaId", COZINHA_ID_INEXISTENTE)
				    .accept(ContentType.JSON)
				.when()
				    .get("/{cozinhaId}")
				.then()
				    .statusCode(HttpStatus.NOT_FOUND.value());
	}

}
