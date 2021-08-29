package com.bnext.dv.repository;

import com.bnext.dv.model.Contact;
import com.mongodb.client.*;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ContactMongoRepositoryShould {

    private ContactMongoRepository contactMongoRepository;

    private MongoCollection mongoCollection;

    Contact contactStub = new Contact("99", "AA");


    @BeforeEach
    public void prepareTest (){

        MongoClient mongoClient;
        mongoClient = Mockito.mock(MongoClient.class);

        MongoDatabase database = Mockito.mock(MongoDatabase.class);
        mongoCollection = Mockito.mock(MongoCollection.class);
        Mockito.when(database.getCollection("contact", Contact.class)).thenReturn(mongoCollection);
        Mockito.when(mongoClient.getDatabase("mgm")).thenReturn(database);

        contactMongoRepository = new ContactMongoRepository(mongoClient);
    }

    @Test
    public void save (){
        contactMongoRepository.save(contactStub);
        Mockito.verify(mongoCollection).insertOne(contactStub);
    }

    @Test
    public void getContactByPhoneNumber (){

        FindIterable mockIterable = Mockito.mock(FindIterable.class);
        MongoCursor<Contact> mongoCursor = Mockito.mock(MongoCursor.class);
        Mockito.when(mongoCollection.find(Mockito.any(Bson.class))).thenReturn(mockIterable);
        Mockito.when(mockIterable.cursor()).thenReturn(mongoCursor);
        Mockito.when(mongoCursor.hasNext()).thenReturn(true);
        Mockito.when(mongoCursor.next()).thenReturn(contactStub);

        Optional<Contact> result = contactMongoRepository.getContactByPhoneNumber("99");
        assertEquals(Optional.of(contactStub),result);

    }

    @Test
    public void getContactByPhoneNumber_not_found (){

        FindIterable mockIterable = Mockito.mock(FindIterable.class);
        MongoCursor<Contact> mongoCursor = Mockito.mock(MongoCursor.class);
        Mockito.when(mongoCollection.find(Mockito.any(Bson.class))).thenReturn(mockIterable);
        Mockito.when(mockIterable.cursor()).thenReturn(mongoCursor);
        Mockito.when(mongoCursor.hasNext()).thenReturn(false);

        Optional<Contact> result = contactMongoRepository.getContactByPhoneNumber("99");
        assertEquals(Optional.empty(),result);
    }


    @Test
    public void recoveryContacts (){

        List<Contact> expectedResult = new ArrayList<>();
        expectedResult.add(contactStub);

        FindIterable mockIterable = Mockito.mock(FindIterable.class);
        MongoCursor<Contact> mongoCursor = Mockito.mock(MongoCursor.class);
        Mockito.when(mongoCollection.find(Mockito.any(Bson.class))).thenReturn(mockIterable);
        Mockito.when(mockIterable.cursor()).thenReturn(mongoCursor);
        Mockito.when(mongoCursor.hasNext()).thenAnswer(
               new Answer<Boolean>()  {

                   int invokeNumber = 0;

                   @Override
                   public Boolean answer(InvocationOnMock invocation) throws Throwable {
                       invokeNumber++;
                       if (invokeNumber > 1)
                           return false;
                       else
                           return true;
                   }
               }
        );
        Mockito.when(mongoCursor.next()).thenReturn(contactStub);

        List<String> numbers = new ArrayList<>();
        numbers.add("99");
        List<Contact> result = contactMongoRepository.recoveryContacts(numbers);
        assertEquals(expectedResult,result);

    }


/*
    @Override
    public List<Contact> recoveryContacts(List<String> contacts) {

        MongoCursor<Contact> cursor = getCollection()
                .find(
                        Filters.in("_id", contacts)
                ).cursor();

        List<Contact> contactList = new ArrayList<>();
        while (cursor.hasNext()){
            contactList.add(cursor.next());

        }
        return contactList;
    }
*/

}