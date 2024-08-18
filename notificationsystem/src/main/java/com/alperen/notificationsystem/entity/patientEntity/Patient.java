package com.alperen.notificationsystem.entity.patientEntity;

import jakarta.persistence.Column;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Patient {
    private int id;
    private String firstName;
    private String middleName;
    private String lastName;
    private Date dateOfBirth;
    private char gender;
    private String address;
    private String tckn;
    private String passportNumber;
    private boolean isSmsActive;
    private boolean isEmailActive;
    private List<PhoneNumber> phoneNumbers = new ArrayList<>();
    private List<EmailAddress> emailAddresses = new ArrayList<>();

    public Patient() {
    }

    public Patient(List<EmailAddress> emailAddresses, List<PhoneNumber> phoneNumbers, boolean isEmailActive, boolean isSmsActive, String passportNumber, String tckn, String address, char gender, Date dateOfBirth, String lastName, String middleName, String firstName) {
        this.emailAddresses = emailAddresses;
        this.phoneNumbers = phoneNumbers;
        this.isEmailActive = isEmailActive;
        this.isSmsActive = isSmsActive;
        this.passportNumber = passportNumber;
        this.tckn = tckn;
        this.address = address;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.lastName = lastName;
        this.middleName = middleName;
        this.firstName = firstName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTckn() {
        return tckn;
    }

    public void setTckn(String tckn) {
        this.tckn = tckn;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public List<EmailAddress> getEmailAddresses() {
        return emailAddresses;
    }

    public void setEmailAddresses(List<EmailAddress> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    public boolean isSmsActive() {
        return isSmsActive;
    }

    public void setSmsActive(boolean smsActive) {
        isSmsActive = smsActive;
    }

    public boolean isEmailActive() {
        return isEmailActive;
    }

    public void setEmailActive(boolean emailActive) {
        isEmailActive = emailActive;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", gender=" + gender +
                ", address='" + address + '\'' +
                ", tckn='" + tckn + '\'' +
                ", passportNumber='" + passportNumber + '\'' +
                ", isSmsActive=" + isSmsActive +
                ", isEmailActive=" + isEmailActive +
                ", phoneNumbers=" + phoneNumbers +
                ", emailAddresses=" + emailAddresses +
                '}';
    }
}
