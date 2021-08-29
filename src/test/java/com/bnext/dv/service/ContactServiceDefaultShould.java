package com.bnext.dv.service;

import com.bnext.dv.model.Contact;
import com.bnext.dv.provaider.PhoneValidatorProvider;
import com.bnext.dv.provaider.model.ValidateResult;
import com.bnext.dv.repository.ContactRepository;
import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ContactServiceDefaultShould {

    private ContactServiceDefault contactServiceDefault;

    private ContactRepository contactRepository;

    private PhoneValidatorProvider phoneValidator;

    private DozerBeanMapper dozerMapper;

    @BeforeEach
    void setUp() {
        phoneValidator = Mockito.mock(PhoneValidatorProvider.class);
        contactRepository = Mockito.mock(ContactRepository.class);
        dozerMapper = Mockito.mock(DozerBeanMapper.class);
        contactServiceDefault = new ContactServiceDefault(contactRepository,phoneValidator,dozerMapper);
    }

    Contact contactStub = new Contact("99", "AA");

    @Test
    void saveContact_new_contact() {
        ValidateResult validateResult = new ValidateResult();
        validateResult.setValid(Boolean.TRUE);
        Mockito.when(phoneValidator.validatePhoneNumber("99","es")).thenReturn(validateResult);
        Mockito.when(contactRepository.getContactByPhoneNumber("99")).thenReturn(Optional.empty());
        contactServiceDefault.saveContact(contactStub);
        Mockito.verify(contactRepository).save(contactStub);
    }

    @Test
    void saveContact_duplicate_contact() {
        Mockito.when(contactRepository.getContactByPhoneNumber("99")).thenReturn(Optional.of(contactStub));
        contactServiceDefault.saveContact(contactStub);
        Mockito.verify(contactRepository, Mockito.times(0)).save(contactStub);
    }


    @Test
    void getContactByPhone() throws InstanceNotFoundException {
        Mockito.when(contactRepository.getContactByPhoneNumber("99")).thenReturn(Optional.of(contactStub));
        Contact contact = contactServiceDefault.getContactByPhone("99");
        assertEquals(contactStub, contact);
    }

    @Test
    void getContactByPhone_not_found() throws InstanceNotFoundException {
        try {
            Mockito.when(contactRepository.getContactByPhoneNumber("99")).thenReturn(Optional.empty());
            contactServiceDefault.getContactByPhone("99");
            fail();
        } catch (InstanceNotFoundException e) {
        }
    }

    @Test
    void recoveryContacts() {


        List<String> contactKeys = new ArrayList<>();
        contactKeys.add("99");
        contactKeys.add("98");
        List<Contact> contactExpected = new ArrayList<>();
        contactExpected.add(contactStub);
        Mockito.when(contactRepository.recoveryContacts(contactKeys)).thenReturn(contactExpected);

        List<Contact> contactList = contactServiceDefault.recoveryContacts(contactKeys);
        assertEquals(contactExpected,contactList);
    }
}