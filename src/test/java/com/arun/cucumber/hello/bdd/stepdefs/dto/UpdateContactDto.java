package com.arun.cucumber.hello.bdd.stepdefs.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateContactDto implements Serializable {

    private static final long serialVersionUID = 8;

    private String id;

    @JsonProperty("person_name")
    private String personName;

    @JsonProperty("contact_name")
    private String contactName;

    @JsonProperty("contact_gst_number")
    private String contactNumber;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPersonName() {
        return this.personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getContactName() {
        return this.contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return this.contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Override
    public String toString() {
        return "UpdateContactDto [id=" + id + ", personName=" + personName + ", contactName=" + contactName
                + ", contactNumber=" + contactNumber + "]";
    }

}
