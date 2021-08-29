package com.bnext.dv.service;

import com.bnext.dv.model.Contact;
import com.bnext.dv.model.User;
import com.bnext.dv.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.management.InstanceNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceDefaultShould {

    private UserServiceDefault userServiceDefault;

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userServiceDefault = new UserServiceDefault(userRepository);
    }

    @Test
    void saveUser() {
        User user = new User("u1");
        this.userServiceDefault.saveUser(user);
        Mockito.verify(userRepository).save(user);
    }

    @Test
    void assignContact() throws InstanceNotFoundException {
        User user = new User ("u1");
        Contact contact = new Contact("91","name");
        Mockito.when(userRepository.findUser("u1")).thenReturn(Optional.of(user));

        this.userServiceDefault.assignContact("u1",contact);
        Mockito.verify(userRepository).assingContactToUser("u1","91");
    }

    @Test
    void assignContact_user_not_found() throws InstanceNotFoundException {
        Contact contact = new Contact("91","name");
        Mockito.when(userRepository.findUser("u1")).thenReturn(Optional.empty());

        assertThrows(InstanceNotFoundException.class,
                () -> this.userServiceDefault.assignContact("u1",contact)) ;
    }

    @Test
    void getUser() throws InstanceNotFoundException {
        User user = new User ("u1");
        Mockito.when(userRepository.findUser("u1")).thenReturn(Optional.of(user));

        assertEquals(user, userServiceDefault.getUser("u1"));
    }

    @Test
    void getUser_user_not_found() throws InstanceNotFoundException {
        Mockito.when(userRepository.findUser("u1")).thenReturn(Optional.empty());

        assertThrows(InstanceNotFoundException.class,
                () -> userServiceDefault.getUser("u1")) ;
    }

    @Test
    void duplicateContactDetect() {
    }
}