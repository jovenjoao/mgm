package com.bnext.dv.repository;

import com.bnext.dv.model.Contact;
import com.bnext.dv.model.User;
import com.mongodb.client.*;
import com.mongodb.client.result.InsertOneResult;
import org.bson.BsonString;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserMongoRepositoryShould {

    private UserMongoRepository userMongoRepository ;
    private MongoCollection mongoCollection;

    private User userStub = new User("user");

    @BeforeEach
    public void prepareTest (){

        MongoClient mongoClient;
        mongoClient = Mockito.mock(MongoClient.class);

        MongoDatabase database = Mockito.mock(MongoDatabase.class);
        mongoCollection = Mockito.mock(MongoCollection.class);
        Mockito.when(database.getCollection("user", User.class)).thenReturn(mongoCollection);
        Mockito.when(mongoClient.getDatabase("mgm")).thenReturn(database);

        userMongoRepository = new UserMongoRepository(mongoClient);
    }

    @Test
    public void save (){

        InsertOneResult result = Mockito.mock(InsertOneResult.class);

        Mockito.when(result.getInsertedId()).thenReturn(new BsonString("check"));
        Mockito.when(mongoCollection.insertOne(userStub)).thenReturn(result);
        userMongoRepository.save(userStub);
        Mockito.verify(mongoCollection).insertOne(userStub);
    }


    @Test
    public void findUser (){

        FindIterable mockIterable = Mockito.mock(FindIterable.class);
        MongoCursor<User> mongoCursor = Mockito.mock(MongoCursor.class);
        Mockito.when(mongoCollection.find(Mockito.any(Bson.class))).thenReturn(mockIterable);
        Mockito.when(mockIterable.cursor()).thenReturn(mongoCursor);
        Mockito.when(mongoCursor.hasNext()).thenReturn(true);
        Mockito.when(mongoCursor.next()).thenReturn(userStub);

        Optional<User> result = userMongoRepository.findUser("user");
        assertEquals(Optional.of(userStub),result);

    }

    @Test
    public void findUser_not_found (){

        FindIterable mockIterable = Mockito.mock(FindIterable.class);
        MongoCursor<User> mongoCursor = Mockito.mock(MongoCursor.class);
        Mockito.when(mongoCollection.find(Mockito.any(Bson.class))).thenReturn(mockIterable);
        Mockito.when(mockIterable.cursor()).thenReturn(mongoCursor);
        Mockito.when(mongoCursor.hasNext()).thenReturn(false);


        Optional<User> result = userMongoRepository.findUser("user");
        assertEquals(Optional.empty(),result);

    }

}