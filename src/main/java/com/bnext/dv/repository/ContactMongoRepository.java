package com.bnext.dv.repository;

import com.bnext.dv.model.Contact;
import com.bnext.dv.model.User;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import jakarta.inject.Singleton;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class ContactMongoRepository implements ContactRepository{

    private final MongoClient mongoClient;

    public ContactMongoRepository(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public Contact save(Contact contact) {
        getCollection().insertOne(contact);
        return contact;
    }

    @Override
    public Optional<Contact> getContactByPhoneNumber(String phoneNumber) {
        MongoCursor<Contact> cursor = getCollection()
                .find(
                        Filters.eq("_id", phoneNumber)
                ).cursor();
        if (cursor.hasNext()){
            return Optional.of(cursor.next());
        }else{
            return Optional.empty();
        }
    }

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

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public Optional<Contact> updateContactName (String phoneNumber, String contactName){

        BsonValue addToSet  =new BsonDocument("contactName", new BsonString(contactName));
        Bson useUpdate  =new BsonDocument("$set",addToSet);

        UpdateResult result = getCollection().updateOne(
                Filters.eq("_id", phoneNumber),
                useUpdate
        );
        return this.getContactByPhoneNumber(phoneNumber);
    }

    private MongoCollection<Contact> getCollection() {
        return mongoClient
                .getDatabase("mgm")
                .getCollection("contact", Contact.class);
    }

}
