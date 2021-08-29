package com.bnext.dv.repository;

import com.bnext.dv.model.User;
import com.bnext.dv.service.model.DuplicateContact;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import jakarta.inject.Singleton;
import org.bson.*;
import org.bson.conversions.Bson;

import java.util.*;

import static com.mongodb.client.model.Aggregates.*;


@Singleton
public class UserMongoRepository implements UserRepository {

    private final MongoClient mongoClient;

    public UserMongoRepository(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }


    @Override
    public User save(User user) {
        BsonValue objectid = getCollection().insertOne(user).getInsertedId();
        user.setId(objectid.asString().getValue());
        return user;
    }

    @Override
    public Optional<User> findUser(String userId) {
        MongoCursor<User> userCursor = getCollection()
                .find(
                        Filters.eq("_id", userId)
                ).cursor();

        if (userCursor.hasNext()){
            return Optional.of(userCursor.next());
        }else{
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> updateUser(User user) {
        UpdateResult result = getCollection().replaceOne(
                Filters.eq("_id", user.getId()),
                user
        );
        if (result.getMatchedCount() == 1) {
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> assingContactToUser(String userId, String contactId) {
        BsonValue addToSet  =new BsonDocument("contacts", new BsonString(contactId));
        Bson useUpdate  =new BsonDocument("$addToSet",addToSet);

        UpdateResult result = getCollection().updateOne(
                Filters.eq("_id", userId),
                useUpdate
        );
        return this.findUser(userId);
    }

    @Override
    public List<DuplicateContact> duplicateContacts(String userId, String userIdToCompare) {


        Bson matchUsers = match(Filters.in( "_id", Arrays.asList(userId,userIdToCompare)));
        Bson unwind = unwind("$contacts");
        Bson group = group("$contacts", Accumulators.sum("count", 1));
        Bson matchDuplicated = match(Filters.eq( "count", 2));

        MongoCursor<DuplicateContact> duplicateContactCursor = mongoClient
                        .getDatabase("mgm")
                        .getCollection("user", DuplicateContact.class).aggregate(Arrays.asList(matchUsers,unwind,group,matchDuplicated)).cursor();


        List<DuplicateContact> duplicateContacts = new ArrayList<>();
        while (duplicateContactCursor.hasNext()){
            duplicateContacts.add(duplicateContactCursor.next());
        }

        return duplicateContacts;
    }

    @Override
    public Optional<User> unassingContactToUser(String userId, String contactId){
        BsonValue removeElement  =new BsonDocument("contacts", new BsonString(contactId));
        Bson useUpdate  =new BsonDocument("$pull",removeElement);

        UpdateResult result = getCollection().updateOne(
                Filters.eq("_id", userId),
                useUpdate
        );
        return this.findUser(userId);

    }



    private MongoCollection<User> getCollection() {
        return mongoClient
                .getDatabase("mgm")
                .getCollection("user", User.class);
    }

}
