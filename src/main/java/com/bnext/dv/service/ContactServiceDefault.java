package com.bnext.dv.service;

import com.bnext.dv.model.Contact;
import com.bnext.dv.provaider.PhoneValidatorProvider;
import com.bnext.dv.provaider.model.ValidateResult;
import com.bnext.dv.repository.ContactRepository;
import io.micronaut.http.client.exceptions.HttpClientException;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.client.netty.FullNettyClientHttpResponse;
import io.micronaut.http.server.exceptions.HttpServerException;
import jakarta.inject.Singleton;
import org.dozer.DozerBeanMapper;

import javax.management.InstanceNotFoundException;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

@Singleton
public class ContactServiceDefault implements ContactService{

    private ContactRepository contactRepository;

    private PhoneValidatorProvider phoneValidator;

    private DozerBeanMapper dozerMapper;

    public ContactServiceDefault (ContactRepository contactRepository, PhoneValidatorProvider phoneValidator, DozerBeanMapper dozerMapper){
        this.contactRepository = contactRepository;
        this.phoneValidator = phoneValidator;
        this.dozerMapper = dozerMapper;
    }

    public void setPhoneValidator (PhoneValidatorProvider phoneValidator){
        this.phoneValidator = phoneValidator;
    }

    @Override
    public Contact saveContact(Contact contact) {
        Optional<Contact> existingContact = contactRepository.getContactByPhoneNumber(contact.getPhoneNumber());
        return existingContact.orElseGet(() -> {
            try {
                ValidateResult validatorResult = phoneValidator.validatePhoneNumber(contact.getPhoneNumber(),"es");
                if (!validatorResult.getValid()){
                    throw new  InvalidParameterException ("invalid-phone");
                }

                dozerMapper.map(validatorResult,contact);
                return contactRepository.save(contact);
            }catch (HttpClientException | HttpServerException e){
                String result = (String) ((FullNettyClientHttpResponse) ((HttpClientResponseException) e).getResponse()).getBody(String.class).orElseGet(
                        () -> "No body present"
                );
                throw new  InvalidParameterException (e.getMessage());
            }
        });
    }

    @Override
    public Contact getContactByPhone(String phoneNumber) throws InstanceNotFoundException {
        return contactRepository.getContactByPhoneNumber(phoneNumber).orElseThrow(InstanceNotFoundException::new);
    }

    @Override
    public List<Contact> recoveryContacts(List<String> contacts){
        return contactRepository.recoveryContacts(contacts);
    }

    @Override
    public Optional<Contact> updateContact(Contact contact) throws InstanceNotFoundException {
        Optional<Contact> existingContact = contactRepository.getContactByPhoneNumber(contact.getPhoneNumber());
        existingContact.orElseThrow(() -> new InstanceNotFoundException());
        return contactRepository.updateContactName(contact.getPhoneNumber(),contact.getContactName());
    }

}
