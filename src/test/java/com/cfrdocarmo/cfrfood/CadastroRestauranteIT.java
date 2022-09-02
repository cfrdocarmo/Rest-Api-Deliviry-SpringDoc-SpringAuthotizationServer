package com.cfrdocarmo.cfrfood;

import com.cfrdocarmo.cfrfood.domain.model.Cozinha;
import com.cfrdocarmo.cfrfood.domain.model.Restaurante;
import com.cfrdocarmo.cfrfood.domain.repository.CozinhaRepository;
import com.cfrdocarmo.cfrfood.domain.repository.RestauranteRepository;
import com.cfrdocarmo.cfrfood.util.DatabaseCleaner;
import com.cfrdocarmo.cfrfood.util.ResourceUtils;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import io.restassured.RestAssured;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroRestauranteIT {

	public static final int RESTAURANTE_ID_INEXISTENTE = 100;

	private Restaurante restauranteCozinhaMineira;
	private String jsonRestauranteCozinhaBrasileira;
	private String jsonRestauranteCozinhaInexistente;
	private String jsonRestauranteSemTaxaFrete;
	private String jsonRestauranteSemCozinha;

	private int quantidadeDeRestaurantesCadastrados;

	@LocalServerPort
	private int port;

	@Autowired
	private DatabaseCleaner databaseCleaner;

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private CozinhaRepository cozinhaRepository;

	@BeforeEach
	public void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/restaurantes";

		databaseCleaner.clearTables();
		prepararDados();

		jsonRestauranteCozinhaBrasileira = ResourceUtils.getContentFromResource("/json/restauranteCozinhaBrasileira.json");
		jsonRestauranteCozinhaInexistente = ResourceUtils.getContentFromResource("/json/restauranteCozinhaBrasileiraCozinhaInexistente.json");
		jsonRestauranteSemCozinha = ResourceUtils.getContentFromResource("/json/restauranteSemCozinha.json");
		jsonRestauranteSemTaxaFrete = ResourceUtils.getContentFromResource("/json/restauranteSemTaxaFrete.json");

	}

	private void prepararDados() {
		Cozinha cozinhaItaliana = new Cozinha();
		cozinhaItaliana.setNome("Italiana");
		cozinhaRepository.save(cozinhaItaliana);

		Cozinha cozinhaMineira = new Cozinha();
		cozinhaMineira.setNome("Mineira");
		cozinhaRepository.save(cozinhaMineira);

		Restaurante restauranteItaliano = new Restaurante();
		restauranteItaliano.setNome("Cozinha Italiana");
		restauranteItaliano.setTaxaFrete(new BigDecimal("5.00"));
		restauranteItaliano.setCozinha(cozinhaItaliana);
		restauranteRepository.save(restauranteItaliano);

		restauranteCozinhaMineira = new Restaurante();
		restauranteCozinhaMineira.setNome("Cozinha Mineira");
		restauranteCozinhaMineira.setCozinha(cozinhaMineira);
		restauranteCozinhaMineira.setTaxaFrete(new BigDecimal("13.00"));
		restauranteRepository.save(restauranteCozinhaMineira);

		quantidadeDeRestaurantesCadastrados = (int) restauranteRepository.count();
	}

	@Test
	public void deveRetornar200_QuandoConsultarRestaurantes() {

				given()
					.accept(ContentType.JSON)
				.when()
					.get()
				.then()
					.statusCode(HttpStatus.OK.value());
	}

	@Test
	public void deveRetornarAQuantidadeCorretaDeRestaurantes_QuandoConsultarRestaurantes() {

				given()
					.accept(ContentType.JSON)
				.when()
					.get()
				.then()
					.body("", hasSize(quantidadeDeRestaurantesCadastrados))
					.body("nome", hasItems("Cozinha Italiana", "Cozinha Mineira"));
	}

	@Test
	public void deveRetornarRespostaEStatusCorretos_QuandoConsultarRestauranteExistente() {
				given()
					.pathParam("restauranteId", restauranteCozinhaMineira.getId())
					.accept(ContentType.JSON)
				.when()
					.get("/{restauranteId}")
				.then()
					.statusCode(HttpStatus.OK.value())
					.body("nome", equalTo(restauranteCozinhaMineira.getNome()));

	}

	@Test
	public void deveRetornarStatus201_QuandoCadastrarRestaurante() {
		given()
				.body(jsonRestauranteCozinhaBrasileira)
					.contentType(ContentType.JSON)
					.accept(ContentType.JSON)
				.when()
					.post()
				.then()
					.statusCode(HttpStatus.CREATED.value());
	}

	@Test
	public void deveRetornarRespostaEStatus400_QuandoCadastrarRestauranteComCozinhaInexistente() {
				given()
					.body(jsonRestauranteCozinhaInexistente)
						.contentType(ContentType.JSON)
					.accept(ContentType.JSON)
				.when()
					.post()
				.then()
					.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	public void deveRetornarRespostaEStatus400_QuandoCadastrarRestauranteSemCozinha() {
		given()
				.body(jsonRestauranteSemCozinha)
					.contentType(ContentType.JSON)
					.accept(ContentType.JSON)
				.when()
					.post()
				.then()
					.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	public void deveRetornarRespostaEStatus400_QuandoCadastrarRestauranteSemTaxaFrete() {
				given()
					.body(jsonRestauranteSemCozinha)
					.contentType(ContentType.JSON)
					.accept(ContentType.JSON)
				.when()
					.post()
				.then()
					.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	public void deveRetornarRespostaEStatusCorretos404_QuandoConsultarRestauranteInexistente() {
		        given()
				    .pathParam("restauranteId", RESTAURANTE_ID_INEXISTENTE)
				    .accept(ContentType.JSON)
				.when()
				    .get("/{restauranteId}")
				.then()
				    .statusCode(HttpStatus.NOT_FOUND.value());
	}

}
