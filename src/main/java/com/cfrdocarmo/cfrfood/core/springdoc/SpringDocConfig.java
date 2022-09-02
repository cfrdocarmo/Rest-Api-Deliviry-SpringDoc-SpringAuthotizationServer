package com.cfrdocarmo.cfrfood.core.springdoc;

import com.cfrdocarmo.cfrfood.api.exceptionHandler.Problem;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
@SecurityScheme(name = "security_auth",
        type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(authorizationCode = @OAuthFlow(
                authorizationUrl = "${springdoc.oAuthFlow.authorizationUrl}",
                tokenUrl = "${springdoc.oAuthFlow.tokenUrl}",
                scopes = {
                        @OAuthScope(name = "READ", description = "read scope"),
                        @OAuthScope(name = "WRITE", description = "write scope")
                }
        ))
    )
public class SpringDocConfig {

    private static String badRequestResponse = "BadRequestResponse";
    private static String notFoundResponse = "NotFoundResponse";
    private static String notAcceptableResponse = "NotAcceptableResponse";
    private static String internalServerErrorResponse = "InternalServerErrorResponse";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("CFRdoCarmoFood API")
                        .version("v1")
                        .description("REST API do CFRdoCarmoFood")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.com"))
                ).externalDocs(new ExternalDocumentation()
                        .description("CFRdoCarmo")
                        .url("https://cfrdocarmo.com.br")
                ).tags(Arrays.asList(
                        new Tag().name("Cidades").description("Gerencia as cidades"),
                        new Tag().name("Grupos").description("Gerencia os grupos"),
                        new Tag().name("Cozinhas").description("Gerencia as cozinhas"),
                        new Tag().name("Formas de Pagemento").description("Gerencia as formas de pagamento"),
                        new Tag().name("Pedidos").description("Gerencia os pedidos"),
                        new Tag().name("Restaurante").description("Gerencia os pedidos"),
                        new Tag().name("Estados").description("Gerencia os estados"),
                        new Tag().name("Produtos").description("Gerencia os produtos"),
                        new Tag().name("Permissões").description("Gerencia as permissões"),
                        new Tag().name("Estatisticas").description("Gerencia os usuários")

                )).components(new Components()
                        .schemas(gerarSchemas())
                        .responses(gerarResponse())
                );
    }



    @Bean
    public OpenApiCustomiser openApiCustomiser() {
        return openAPI -> {
            openAPI.getPaths()
                    .values()
                    .forEach(pathItem -> pathItem.readOperationsMap()
                            .forEach((httpMethod, operation) -> {
                                ApiResponses responses = operation.getResponses();
                                switch (httpMethod) {
                                    case GET:
                                        responses.addApiResponse("406", new ApiResponse().$ref(notAcceptableResponse));
                                        responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
                                        break;
                                    case POST:
                                        responses.addApiResponse("400", new ApiResponse().$ref(badRequestResponse));
                                        responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
                                        break;
                                    case PUT:
                                        responses.addApiResponse("400", new ApiResponse().$ref(badRequestResponse));
                                        responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
                                        break;
                                    case DELETE:
                                        responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
                                        break;
                                    default:
                                        responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
                                        break;
                                }
                            }));
        };
    }

    private Map<String, Schema> gerarSchemas() {
        final Map<String, Schema> schemaMap = new HashMap<>();

        Map<String, Schema> problemSchema = ModelConverters.getInstance().read(Problem.class);
        Map<String, Schema> problemObjectSchema = ModelConverters.getInstance().read(Problem.Object.class);

        schemaMap.putAll(problemSchema);
        schemaMap.putAll(problemObjectSchema);

        return schemaMap;
    }

    private Map<String, ApiResponse> gerarResponse() {
        final Map<String, ApiResponse> apiResponseMap = new HashMap<>();

        Content content = new Content()
                .addMediaType(APPLICATION_JSON_VALUE, new MediaType().schema(new Schema<Problem>().$ref("Problema")));

        apiResponseMap.put(badRequestResponse, new ApiResponse()
                .description("Requisição inválida")
                .content(content));

        apiResponseMap.put(notFoundResponse, new ApiResponse()
                .description("Recurso não encontrado")
                .content(content));

        apiResponseMap.put(notAcceptableResponse, new ApiResponse()
                .description("Recurso não possui representação que poderia ser aceita pelo consumidor")
                .content(content));

        apiResponseMap.put(internalServerErrorResponse, new ApiResponse()
                .description("Erro interno no servidor")
                .content(content));

        return apiResponseMap;

    }

}
