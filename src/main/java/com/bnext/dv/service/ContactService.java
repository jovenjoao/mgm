package com.bnext.dv.service;

import com.bnext.dv.model.Contact;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Optional;


public interface ContactService {

    Contact saveContact (Contact contact);

    Contact getContactByPhone (String phoneNumber) throws InstanceNotFoundException;

    List<Contact> recoveryContacts(List<String> contacts);

    Optional<Contact> updateContact(Contact contact) throws InstanceNotFoundException;
}
