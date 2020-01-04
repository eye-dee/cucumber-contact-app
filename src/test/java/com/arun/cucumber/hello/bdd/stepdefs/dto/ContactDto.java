
package com.arun.cucumber.hello.bdd.stepdefs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.UUID;

public class ContactDto implements Serializable {

    private static final long serialVersionUID = 5270036805695796469L;

    @JsonProperty("person_name")
    private String personName;

    @JsonProperty("contact_name")
    private String contactName;

    @JsonProperty("contact_gst_number")
    private String contactNumber;

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

    public void makeRandom() {
        this.contactName = this.contactName + UUID.randomUUID().toString();
    }

    @Override

    public String toString() {
        return "ContactDto [personName=" + personName + ", contactName=" + contactName + ", contactNumber="
                + contactNumber + "]";
    }

}	
