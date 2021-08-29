package com.bnext.dv.repository;

import com.bnext.dv.model.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactRepository {

    Contact save (Contact contact);

    Optional<Contact> getContactByPhoneNumber (String phoneNumber);

    List<Contact> recoveryContacts(List<String> contacts);

    Optional<Contact> updateContactName (String phoneNumber, String contactName);

}
