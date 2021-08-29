package com.bnext.dv.service;

import com.bnext.dv.model.Contact;
import com.bnext.dv.model.User;
import com.bnext.dv.repository.UserRepository;
import com.bnext.dv.service.model.DuplicateContact;
import jakarta.inject.Singleton;

import javax.management.InstanceNotFoundException;
import java.util.List;

@Singleton
public class UserServiceDefault implements UserService{

    private UserRepository userRepository;

    public UserServiceDefault (UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User assignContact(String userId, Contact contact) throws InstanceNotFoundException {
        this.userRepository.findUser(userId).orElseThrow(InstanceNotFoundException::new );
        this.userRepository.assingContactToUser(userId,contact.getPhoneNumber());
        return this.userRepository.findUser(userId).orElseThrow(InstanceNotFoundException::new );
    }

    @Override
    public User unassignContact(String userId, String contactId) throws InstanceNotFoundException {
        this.userRepository.findUser(userId).orElseThrow(InstanceNotFoundException::new );
        this.userRepository.unassingContactToUser(userId,contactId);
        return this.userRepository.findUser(userId).orElseThrow(InstanceNotFoundException::new );
    }

    @Override
    public User getUser(String userId) throws InstanceNotFoundException {
        return this.userRepository.findUser(userId).orElseThrow(InstanceNotFoundException::new );
    }

    @Override
    public List<DuplicateContact> duplicateContactDetect(String userId, String userIdToCompare) {
        return this.userRepository.duplicateContacts(userId, userIdToCompare);
    }
}
