package com.bnext.dv.controller;

import com.bnext.dv.controller.request.ContactRequest;
import com.bnext.dv.model.Contact;
import com.bnext.dv.model.User;
import com.bnext.dv.service.ContactService;
import com.bnext.dv.service.UserService;
import com.bnext.dv.service.model.DuplicateContact;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import javax.management.InstanceNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller("/contact")
public class ContactController {

    private ContactService contactService;
    private UserService userService;

    public ContactController(ContactService contactService,UserService userService) {
        this.contactService = contactService;
        this.userService = userService;
    }

    @Post
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes( MediaType.APPLICATION_JSON)
    public Contact createContact(@Valid @Body ContactRequest contactRequest) throws InstanceNotFoundException {
        Contact contact = this.contactService.saveContact(contactRequest.getContact());
        userService.assignContact(contactRequest.getUserId(), contact);
        return contact;
    }

    @Patch
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes( MediaType.APPLICATION_JSON)
    public Contact updateContact(@Body Contact contactRequest) throws InstanceNotFoundException {
        Optional<Contact> contactOptional = this.contactService.updateContact(contactRequest);
        return contactOptional.orElseThrow(() -> new InstanceNotFoundException());
    }

    @Patch ("/unassign")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes( MediaType.APPLICATION_JSON)
    public void unassignContact(@Valid @Body ContactRequest contactRequest) throws InstanceNotFoundException {
        User user = userService.getUser(contactRequest.getUserId());
        userService.unassignContact(contactRequest.getUserId(), contactRequest.getContact().getPhoneNumber());
    }



    @Get("/duplicated")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes( MediaType.APPLICATION_JSON)
    public List<Contact> commonsContacts(@QueryValue ("user") String userId, @QueryValue ("userToCompare")String userIdToCompare) {
        List<DuplicateContact> contacts = this.userService.duplicateContactDetect(userId,userIdToCompare);
        List<String> contactsId = contacts.stream().map(contact -> contact.getId()).collect(Collectors.toList());
        return this.contactService.recoveryContacts(contactsId);
    }

    @Get
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes( MediaType.APPLICATION_JSON)
    public List<Contact> getContactByUser( @QueryValue(value = "user") String userId) throws InstanceNotFoundException {
        User user = this.userService.getUser(userId);
        return this.contactService.recoveryContacts(user.getContacts());
    }

}