package com.arun.cucumber.hello.contact;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "contacts", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "contactNumber" }, name = "UK_contact_contactNumber") })
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "createdAt", "updatedAt" }, allowGetters = true)
public class Contact implements Serializable {

	private static final long serialVersionUID = 5270036805695796469L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Name is required")
	@NotBlank(message = "Name is required")
	@JsonProperty("person_name")
	private String personName;

	@JsonProperty("contact_name")
	@NotNull(message = "Email is required")
	@NotBlank(message = "Email is required")
	private String contactName;

	@JsonProperty("contact_gst_number")
	@ContactNumberConstraint
	private int contactNumber;

	private Date createdAt;

	private Date updatedAt;

	private Integer version;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
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

	public int getContactNumber() {
		return this.contactNumber;
	}

	public void setContactNumber(int contactNumber) {
		this.contactNumber = contactNumber;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "Contact [id=" + this.id + ", personName=" + this.personName + ", contactName=" + this.contactName
				+ ", contactNumber=" + this.contactNumber + ", createdAt=" + this.createdAt + ", updatedAt="
				+ this.updatedAt + ", version=" + this.version + "]";
	}

}
