package com.bnext.dv.repository;

import com.bnext.dv.model.User;
import com.bnext.dv.service.model.DuplicateContact;

import java.util.List;
import java.util.Optional;

public interface UserRepository  {

    User save (User user);

    Optional<User> findUser (String userId);

    Optional<User> updateUser (User user);

    Optional<User> assingContactToUser(String UserId, String contactId);

    List<DuplicateContact> duplicateContacts(String userId, String userIdToCompare);

    Optional<User> unassingContactToUser(String userId, String contactId);
}
