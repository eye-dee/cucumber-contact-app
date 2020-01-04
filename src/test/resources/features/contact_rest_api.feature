
Feature: Test Contact API
  In this feature file, REST api for the contact, will be tested.

  Scenario: Create a contact successfully
    Given user wants to delete all contacts
    Given user wants to create a contact with the following attributes
      | person_name | contact_gst_number | contact_name  |
      | roshan      |         1231231231 | test@test.com |
    And user saves the new contact
    Then the save 'CREATED'

  Scenario: Retrieve a contact by existing id successfully
    Given user retrieves a contact  with id 'LAST_CREATED'
    Then the save 'SUCCESS'

  Scenario: Update a contact successfully
    Given user wants to update a contact with the following attributes
      | id           | person_name    | contact_gst_number | contact_name          |
      | LAST_CREATED | roshan-updated |         1234567899 | test-updated@test.com |
    And user updates the new contact
    Then the save 'SUCCESS'

  Scenario: Create a contact with already saved number
    Given user wants to create a contact with the following attributes
      | person_name | contact_gst_number | contact_name   |
      | roshan2     |         1234567899 | test2@test.com |
    And user saves the new contact
    Then the save 'BAD_REQUEST' for 'contactNumber' with message 'Two contacts cannot have the same phone number.'

  Scenario: Delete a contact successfully
    Given user deletes a contact  with id 'LAST_CREATED'
    Then the save 'SUCCESS'

  Scenario: Retrieve a contact by non existing id successfully
    Given user retrieves a contact  with id '1000000000000000'
    Then the save 'NOT_FOUND'

  Scenario: Update a non existing contact
    Given user wants to update a contact with the following attributes
      | id               | person_name    | contact_gst_number | contact_name          |
      | 1000000000000000 | roshan-updated |         1234567890 | test-updated@test.com |
    And user updates the new contact
    Then the save 'NOT_FOUND'

  Scenario: Delete a non existing contact
    Given user deletes a contact  with id '1000000000000000'
    Then the save 'NOT_FOUND'

  Scenario: Create a contact with empty person name
    Given user wants to create a contact with the following attributes
      | person_name | contact_name      | contact_gst_number |
      |             | roshan@google.com |         1231231231 |
    And user saves the new contact
    Then the save 'BAD_REQUEST' for 'personName' with message 'Name is required'

  Scenario: Create a contact with empty contact number
    Given user wants to create a contact with the following attributes
      | person_name | contact_name      | contact_gst_number |
      | roshan      | roshan@google.com |                    |
    And user saves the new contact
    Then the save 'BAD_REQUEST' for 'contactNumber' with message 'Number is required'

  Scenario: Create a contact with empty contact name
    Given user wants to create a contact with the following attributes
      | person_name | contact_name | contact_gst_number |
      | roshan      |              |         1231231231 |
    And user saves the new contact
    Then the save 'BAD_REQUEST' for 'contactName' with message 'Email is required'

  Scenario: Create a contact with invalid contact number (text)
    Given user wants to create a contact with the following attributes
      | person_name | contact_name  | contact_gst_number |
      | roshan      | test@test.com | contactNumber      |
    And user saves the new contact
    Then the save 'BAD_REQUEST' for 'contact_gst_number' with message 'Invalid contact number'

  Scenario: Retrieve all contacts
    Given user wants to retrieve all contacts with start index 0 and page size 5
    Then the save 'SUCCESS'
    Then the contact list size should be 5 or less
    Then the contact list size should be sorted
