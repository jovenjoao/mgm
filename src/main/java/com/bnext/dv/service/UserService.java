package com.bnext.dv.service;

import com.bnext.dv.model.Contact;
import com.bnext.dv.model.User;
import com.bnext.dv.service.model.DuplicateContact;

import javax.management.InstanceNotFoundException;
import java.util.List;

public interface UserService {

    User saveUser (User user);

    User assignContact (String userId, Contact contact) throws InstanceNotFoundException;

    User unassignContact (String userId, String contactId) throws InstanceNotFoundException;

    User getUser (String userId) throws InstanceNotFoundException;

    List<DuplicateContact> duplicateContactDetect (String userId, String userIdToCompare);

}
