package com.arun.cucumber.hello.bdd.stepdefs;

import com.arun.cucumber.hello.bdd.stepdefs.dto.ContactDto;
import com.arun.cucumber.hello.bdd.stepdefs.dto.UpdateContactDto;
import com.arun.cucumber.hello.common.Error;
import com.arun.cucumber.hello.contact.Contact;
import cucumber.api.java8.En;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Step Definition Class for ContactDto.
 */
public class ContactSteps extends AbstractSteps implements En {

    public ContactSteps() {

        Given("user wants to delete all contacts", () -> {
            String apiPath = "contacts/all";
            executeDelete(apiPath);
        });

        Given("user wants to create a contact with the following attributes", (DataTable contactDt) -> {
            testContext().reset();
            List<ContactDto> contactList = contactDt.asList(ContactDto.class);

            ContactDto contactDto = contactList.get(0);

            super.testContext().setPayload(contactDto);
        });

        When("user saves the new contact", () -> {
            String createContactUrl = "contacts";

            // AbstractSteps class makes the POST call and stores response in TestContext
            executePost(createContactUrl);
        });

        Then("the save {string}", (String expectedResult) -> {
            Response response = testContext().getResponse();

            switch (expectedResult) {
                case "SUCCESS":
                    assertThat(response.statusCode()).isIn(200);
                    break;
                case "CREATED":
                    assertThat(response.statusCode()).isIn(201);
                    break;
                case "FAILED":
                    assertThat(response.statusCode()).isBetween(401, 504);
                    break;
                case "BAD_REQUEST":
                    assertThat(response.statusCode()).isIn(400);
                    break;
                case "NOT_FOUND":
                    assertThat(response.statusCode()).isIn(404);
                    break;
                default:
                    fail("Unexpected error");
            }
        });

        Then("the save {string} for {string} with message {string}",
                (String errorCode, String fieldName, String errorMessage) -> {

                    Response lastSuccessResponse = this.testContext().getLastSuccessResponse();
                    Response response = testContext().getResponse();

                    switch (errorCode) {
                        case "FAILED":
                            assertThat(response.statusCode()).isBetween(401, 504);
                            break;
                        case "BAD_REQUEST":
                            assertThat(response.statusCode()).isIn(400);
                            break;
                        default:
                            fail("Unexpected error");
                    }

                    Error error = response.as(Error.class);

                    assertThat(error.getErrors().get(fieldName)).isEqualTo(errorMessage);

                });

        Given("user wants to update a contact with the following attributes",
                (io.cucumber.datatable.DataTable updateContactDt) -> {

                    List<UpdateContactDto> contactList = updateContactDt.asList(UpdateContactDto.class);

                    UpdateContactDto updateContactDto = contactList.get(0);
                    if ("LAST_CREATED".equals(updateContactDto.getId())) {
                        assertNotNull(super.testContext().getLastSuccessResponse());
                        assertNotNull(super.testContext().getLastSuccessResponse().as(Contact.class).getId());
                        Long id = super.testContext().getLastSuccessResponse().as(Contact.class).getId();
                        updateContactDto.setId(String.valueOf(id));
                    }
                    updateContactDto.setContactName(updateContactDto.getContactName() + UUID.randomUUID().toString());

                    super.testContext().setPayload(updateContactDto);
                });

        When("user updates the new contact", () -> {
            String updateContactUrl = "contacts/{id}";

            Map<String, String> pathParams = new HashMap<>();
            pathParams.put("id", ((UpdateContactDto) testContext().getPayload()).getId());
            executePut(updateContactUrl, pathParams);
        });

        Given("user deletes a contact  with id {string}", (String id) -> {
            String deleteContactUrl = "contacts/{id}";

            Map<String, String> pathParams = new HashMap<>();
            if ("LAST_CREATED".equals(id)) {
                assertNotNull(super.testContext().getLastSuccessResponse());
                assertNotNull(super.testContext().getLastSuccessResponse().as(Contact.class).getId());
                pathParams.put("id",
                        String.valueOf(super.testContext().getLastSuccessResponse().as(Contact.class).getId()));
            } else {
                pathParams.put("id", id);
            }

            executeDelete(deleteContactUrl, pathParams);
        });

        Given("user wants to retrieve all contacts with start index {int} and page size {int}",
                (Integer startIndex, Integer pageSize) -> {

                    String retrieveContactsUrl = "contacts/{startIndex}/{pageSize}";

                    Map<String, String> pathParams = new HashMap<>();

                    pathParams.put("startIndex", String.valueOf(startIndex));
                    pathParams.put("pageSize", String.valueOf(pageSize));

                    executeGet(retrieveContactsUrl, pathParams);
                });

        Then("the contact list size should be {int} or less", (Integer maxSize) -> {
            List<Contact> list = super.testContext().getResponse().as(ArrayList.class);
            assertTrue(list.size() <= maxSize);
        });

        Given("user retrieves a contact  with id {string}", (String id) -> {

            String retrieveContactsUrl = "contacts/{id}";

            Map<String, String> pathParams = new HashMap<>();
            if ("LAST_CREATED".equals(id)) {

                assertNotNull(super.testContext().getLastSuccessResponse());
                assertNotNull(super.testContext().getLastSuccessResponse().as(Contact.class).getId());
                pathParams.put("id",
                        String.valueOf(super.testContext().getLastSuccessResponse().as(Contact.class).getId()));
            } else {
                pathParams.put("id", id);
            }

            executeGet(retrieveContactsUrl, pathParams);
        });

        Then("the contact list size should be sorted", () -> {
            List<LinkedHashMap<String, String>> list = super.testContext().getResponse().as(ArrayList.class);

            List<String> contactNameList = new ArrayList<>();
            for (LinkedHashMap<String, String> contact : list) {
                contactNameList.add(contact.get("contactName"));
            }

            assertTrue(isSorted(contactNameList));

        });

    }

    public boolean isSorted(List<String> list) {
        boolean sorted = true;
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i - 1).compareTo(list.get(i)) > 0) {
                sorted = false;
                break;
            }
        }

        return sorted;
    }

}
