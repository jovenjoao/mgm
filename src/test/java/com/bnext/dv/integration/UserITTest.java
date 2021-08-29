package com.bnext.dv.integration;

import com.bnext.dv.controller.request.UserRequest;
import com.bnext.dv.model.Contact;
import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

@Testcontainers
@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserITTest {

    @Container
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.0");

    private static EmbeddedServer embeddedServer;
    private static ApplicationContext context;


    @BeforeAll
    void initialDataSetup() {
        mongoDBContainer.start();
        embeddedServer = ApplicationContext.run(EmbeddedServer.class,
                Map.of("mongodb.uri", String.format("mongodb://%s:%s", mongoDBContainer.getContainerIpAddress(), mongoDBContainer.getMappedPort(27017)))
        );
        context = embeddedServer.getApplicationContext();
    }

    @Test
    public void createUser() throws MalformedURLException {
        HttpClient client = HttpClient.create(new URL("http://" + embeddedServer.getHost() + ":" + embeddedServer.getPort()));
        HttpRequest<UserRequest> request = HttpRequest.POST("/user", new UserRequest ("u1"));
        String body = client.toBlocking().retrieve(request);
        assertNotNull(body);
        assertEquals("{\"user\":\"u1\",\"id\":\"u1\"}", body);
    }


    @Test
    public void createUser_error() throws MalformedURLException {
        HttpClient client = HttpClient.create(new URL("http://" + embeddedServer.getHost() + ":" + embeddedServer.getPort()));
        HttpRequest<UserRequest> request = HttpRequest.POST("/user", new UserRequest ());
        assertThrows(HttpClientException.class, () ->  client.toBlocking().retrieve(request));
    }

}
