package com.bnext.dv.provaider;

import com.bnext.dv.config.NeutrinoPhoneProviderConfig;
import com.bnext.dv.provaider.model.ValidateResult;

import java.net.URI;

import com.bnext.dv.provaider.model.request.PhoneValidationRequest;
import com.bnext.dv.provaider.model.response.PhoneValidatorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import jakarta.inject.Singleton;
import org.dozer.DozerBeanMapper;
import reactor.core.publisher.Mono;


@Singleton
public class PhoneValidatorNeutrinoProvider implements PhoneValidatorProvider {


    private final HttpClient httpClient;
    private final String userId;
    private final String apiKey;
    private final String host;
    private final String phoneValidatorPaht;
    private final DozerBeanMapper dozerMapper;
    private final ObjectMapper objectMapper;

    public PhoneValidatorNeutrinoProvider(@Client("${neutrino.host}")HttpClient httpClient, NeutrinoPhoneProviderConfig neutrinoConfig, DozerBeanMapper dozerMapper, ObjectMapper objectMapper) {

        this.userId = neutrinoConfig.getUserId();
        this.apiKey = neutrinoConfig.getApiKey();
        this.host = neutrinoConfig.getHost();
        this.httpClient = httpClient;
        this.phoneValidatorPaht = neutrinoConfig.getValidatePhonePath();
        this.dozerMapper = dozerMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public ValidateResult validatePhoneNumber(String phoneNumber, String country) {

        URI uri = UriBuilder.of(phoneValidatorPaht)
                //.path(phoneValidatorPaht)
                .build();

        try {
            PhoneValidationRequest requestBody = new PhoneValidationRequest(phoneNumber, country);
            HttpRequest
                    <?> request = HttpRequest.POST(uri, objectMapper.writeValueAsString(requestBody))
                    .header("user-id", userId)
                    .header("api-key", apiKey)
                    .header("Content-Type", "application/json");
            PhoneValidatorResponse validateResult = Mono.from(httpClient.retrieve(request, PhoneValidatorResponse.class)).block();

            return dozerMapper.map(validateResult, ValidateResult.class);
        }catch (JsonProcessingException e){
            return null;
        }
    }



/*
 @Singleton
class GithubLowLevelClient(@param:Client(GithubConfiguration.GITHUB_API_URL) private val httpClient: HttpClient,
                           configuration: GithubConfiguration) {
    private val uri: URI = UriBuilder.of("/repos")
        .path(configuration.organization)
        .path(configuration.repo)
        .path("releases")
        .build()

    fun fetchReleases(): Mono<List<GithubRelease>> {
        val req: HttpRequest<*> = HttpRequest.GET<Any>(uri)
            .header(USER_AGENT, "Micronaut HTTP Client")
            .header(ACCEPT, "application/vnd.github.v3+json, application/json")
        return Mono.from(httpClient.retrieve(req, Argument.listOf(GithubRelease::class.java)))
    }
}    }*/
}





