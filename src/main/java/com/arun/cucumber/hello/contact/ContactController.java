package com.arun.cucumber.hello.contact;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContactController {

    private Logger logger = LoggerFactory.getLogger(ContactController.class);

    private @Autowired
    ContactService contactService;

    @PostMapping("api/contacts")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Contact createContact(@RequestBody Contact contact) {
        this.logger.info("Saving contact {}", contact);
        return contactService.create(contact);
    }

    @PutMapping("api/contacts/{id}")
    public Contact updateContact(@PathVariable Long id, @RequestBody Contact contact) {
        return contactService.update(id, contact);
    }

    @DeleteMapping("api/contacts/{id}")
    public ResponseEntity<?> deleteContact(@PathVariable(value = "id") Long contactId) {
        this.contactService.delete(contactId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("api/contacts/all")
    public ResponseEntity<?> deleteAllContacts() {
        this.contactService.deleteAll();
        return ResponseEntity.ok().build();
    }

    @GetMapping("api/contacts/{startIndex}/{pageSize}")
    public List<Contact> getAllContactsWithPagination(
            @PathVariable("startIndex") int startIndex,
            @PathVariable("pageSize") int pageSize
    ) {
        return contactService.getAllWithPagination(startIndex, pageSize);
    }

    @GetMapping("api/contacts")
    public List<Contact> getAllContacts() {
        return contactService.getAll();
    }

    @GetMapping("/api/contacts/{id}")
    public Contact getContactById(@PathVariable(value = "id") Long contactId) {
        return contactService.get(contactId);
    }

}
