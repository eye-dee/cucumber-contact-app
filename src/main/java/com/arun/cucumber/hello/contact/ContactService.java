package com.arun.cucumber.hello.contact;

import com.arun.cucumber.hello.common.ResourceNotFoundException;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ContactService {

    private @Autowired
    ContactRepository contactRepository;

    public Contact create(@Valid Contact contact) {
        return this.contactRepository.save(contact);
    }

    public Contact get(Long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("contact", "id", id));
    }

    public List<Contact> getAllWithPagination(int startIndex, int pageSize) {
        return contactRepository
                .findAll(PageRequest.of(startIndex / pageSize, pageSize))
                .getContent();
    }

    public List<Contact> getAll() {
        return contactRepository.findAll();
    }

    public Contact update(Long id, @Valid Contact contact) {
        contact.setId(id);

        contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("contact", "id", id));
        return contactRepository.save(contact);
    }

    public void delete(Long id) {
        contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("contact", "id", id));
        contactRepository.deleteById(id);
    }

    public void deleteAll() {
        contactRepository.deleteAll();
    }

}
