package com.alperen.hospitalsystem.Request;

import com.alperen.hospitalsystem.entity.EmailAddress;
import com.alperen.hospitalsystem.entity.Patient;
import com.alperen.hospitalsystem.entity.PhoneNumber;
import jakarta.persistence.Column;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientRequest {

    private String firstName;
    private String middleName;
    private String lastName;
    private Date dateOfBirth;
    private char gender;
    private String address;
    private String tckn;
    private String passportNumber;
    private int versionNumber = 1;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean isActive;
    private boolean isSmsActive;
    private boolean isEmailActive;
    private List<PhoneNumber> phoneNumbers = new ArrayList<>();
    private List<EmailAddress> emailAddresses = new ArrayList<>();

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

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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
}
