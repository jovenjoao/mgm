package com.bnext.dv.integration;

import com.bnext.dv.controller.request.ContactRequest;
import com.bnext.dv.controller.request.UserRequest;
import com.bnext.dv.model.Contact;
import com.bnext.dv.model.User;
import com.bnext.dv.provaider.PhoneValidatorNeutrinoProvider;
import com.bnext.dv.provaider.PhoneValidatorProvider;
import com.bnext.dv.provaider.model.ValidateResult;
import com.bnext.dv.service.ContactService;
import com.bnext.dv.service.ContactServiceDefault;
import com.bnext.dv.service.model.DuplicateContact;
import com.mongodb.client.MongoClient;
import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.exceptions.HttpClientException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.management.InstanceNotFoundException;
import javax.validation.Valid;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Testcontainers
@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ContactITTest {

    @Container
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.0");

    private static EmbeddedServer embeddedServer;
    private static ApplicationContext context;

    @Inject
    private PhoneValidatorProvider phoneValidatorProvider;

    private MongoClient mongoClient;

    @MockBean(PhoneValidatorNeutrinoProvider.class)
    public PhoneValidatorProvider dependency() {
        return mock(PhoneValidatorProvider.class);
    }

    @BeforeAll
    void initialDataSetup() {
        mongoDBContainer.start();
        embeddedServer = ApplicationContext.run(EmbeddedServer.class,
                Map.of("mongodb.uri", String.format("mongodb://%s:%s", mongoDBContainer.getContainerIpAddress(), mongoDBContainer.getMappedPort(27017)))
            );
        context = embeddedServer.getApplicationContext();

        mongoClient = context.getBean(MongoClient.class);

        //TODO Workaround to inject a mock
        ContactService contactService = context.getBean(ContactService.class);
        ((ContactServiceDefault)contactService).setPhoneValidator(phoneValidatorProvider);

    }

    @BeforeEach
    void prepareData (){

        mongoClient.getDatabase("mgm").getCollection("user").drop();
        mongoClient.getDatabase("mgm").getCollection("contact").drop();

        //insert previous data
        List<String> contactList = new ArrayList<>();
        contactList.add("100");
        mongoClient
                .getDatabase("mgm")
                .getCollection("user", User.class).insertOne(new User("u1",contactList));

        contactList = new ArrayList<>();
        contactList.add("100");
        contactList.add("101");
        mongoClient
                .getDatabase("mgm")
                .getCollection("user", User.class).insertOne(new User("u3",contactList));

        mongoClient
                .getDatabase("mgm")
                .getCollection("contact", Contact.class).insertOne(new Contact("100","AA"));

        mongoClient
                .getDatabase("mgm")
                .getCollection("contact", Contact.class).insertOne(new Contact("101","BB"));

        //prepare mock
        ValidateResult phoneValidation = new ValidateResult();
        phoneValidation.setValid(Boolean.TRUE);
        when(phoneValidatorProvider.validatePhoneNumber("91","es")).thenReturn(phoneValidation );

    }


    @Test
    public void createContactTest () throws MalformedURLException {

        //prepare mock
        ValidateResult phoneValidation = new ValidateResult();
        phoneValidation.setValid(Boolean.TRUE);
        when(phoneValidatorProvider.validatePhoneNumber("91","es")).thenReturn(phoneValidation );

        //test
        HttpClient client = HttpClient.create(new URL("http://" + embeddedServer.getHost() + ":" + embeddedServer.getPort()));
        HttpRequest<ContactRequest> request = HttpRequest.POST("/contact", new ContactRequest("u1",new Contact("91","AA")));

        //result
        String body = client.toBlocking().retrieve(request);
        assertNotNull(body);
        assertEquals("{\"phoneNumber\":\"91\",\"contactName\":\"AA\",\"valid\":true,\"id\":\"91\"}", body);
    }


    @Test
    public void createContactTest_user_not_present () throws MalformedURLException {


        //test
        HttpClient client = HttpClient.create(new URL("http://" + embeddedServer.getHost() + ":" + embeddedServer.getPort()));
        HttpRequest<ContactRequest> request = HttpRequest.POST("/contact", new ContactRequest("u2",new Contact("91","AA")));
        assertThrows(HttpClientException.class, () ->  client.toBlocking().retrieve(request));

    }

    @Test
    public void createContactTest_invalid_format () throws MalformedURLException {

        //prepare mock
        ValidateResult phoneValidation = new ValidateResult();
        phoneValidation.setValid(Boolean.TRUE);
        when(phoneValidatorProvider.validatePhoneNumber("91","es")).thenReturn(phoneValidation );

        //test
        HttpClient client = HttpClient.create(new URL("http://" + embeddedServer.getHost() + ":" + embeddedServer.getPort()));
        HttpRequest<ContactRequest> request = HttpRequest.POST("/contact", new ContactRequest("u1",new Contact()));
        assertThrows(HttpClientException.class, () ->  client.toBlocking().retrieve(request));

    }


    @Test
    public void updateContact () throws MalformedURLException{

        //insert previous data
        mongoClient
                .getDatabase("mgm")
                .getCollection("contact", Contact.class).insertOne(new Contact("92","oldName"));

        //test
        HttpClient client = HttpClient.create(new URL("http://" + embeddedServer.getHost() + ":" + embeddedServer.getPort()));
        HttpRequest<Contact> request = HttpRequest.PATCH("/contact", new Contact("92","newName"));

        //result
        String body = client.toBlocking().retrieve(request);
        assertNotNull(body);
        assertEquals("{\"phoneNumber\":\"92\",\"contactName\":\"newName\",\"id\":\"92\"}", body);
    }

    @Test
    public void unassignContact () throws MalformedURLException{

        //test
        HttpClient client = HttpClient.create(new URL("http://" + embeddedServer.getHost() + ":" + embeddedServer.getPort()));
        HttpRequest<ContactRequest> request = HttpRequest.PATCH("/contact/unassign", new ContactRequest("u1",new Contact("100","newName"))  );

        //result
        try {
            client.toBlocking().retrieve(request);
        }catch (Exception e){}
        User user = mongoClient
                .getDatabase("mgm")
                .getCollection("user", User.class).find().cursor().next();

        assertEquals(0,user.getContacts().size());

    }


    @Test
    public void commonsContacts () throws MalformedURLException{

        //test
        HttpClient client = HttpClient.create(new URL("http://" + embeddedServer.getHost() + ":" + embeddedServer.getPort()));
        HttpRequest<ContactRequest> request = HttpRequest.GET("/contact/duplicated?user=u1&userToCompare=u3");

        //result
        String body = client.toBlocking().retrieve(request);
        assertNotNull(body);
        assertEquals("[{\"phoneNumber\":\"100\",\"contactName\":\"AA\",\"id\":\"100\"}]", body);

    }

    @Test
    public void getContactByUser () throws MalformedURLException{

        //test
        HttpClient client = HttpClient.create(new URL("http://" + embeddedServer.getHost() + ":" + embeddedServer.getPort()));
        HttpRequest<ContactRequest> request = HttpRequest.GET("/contact?user=u1");

        //result
        String body = client.toBlocking().retrieve(request);
        assertNotNull(body);
        assertEquals("[{\"phoneNumber\":\"100\",\"contactName\":\"AA\",\"id\":\"100\"}]", body);
    }

}
