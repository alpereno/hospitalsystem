package com.alperen.hospitalsystem.entity;

import com.alperen.hospitalsystem.Request.PatientRequest;
import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="patients")
public class Patient{
    // Annotate the class as an entity and map to db table
    // define fields
    // annotate fields with db column names
    // ** set up mapping to PhoneNumber, EmailAddress and PatientName entities
    // create constructors
    // generate getter/setter methods
    // generate toString() method

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "gender", length = 1)
    private char gender;

    @Column(name = "address")
    private String address;

    @Column(name = "tckn", unique = true, length = 11)
    private String tckn;

    @Column(name = "passport_number", unique = true, length = 20)
    private String passportNumber;

    @Column(name = "version_number")
    private int versionNumber = 1;

    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    @Column(name = "is_Active")
    private boolean isActive = true;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhoneNumber> phoneNumbers = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmailAddress> emailAddresses = new ArrayList<>();

    // bool 2 alan sms ve email true gönder false hayır

    public Patient() {
    }

    public Patient(String firstName, String middleName, String lastName, Date dateOfBirth, char gender, String address,
                   String tckn, String passportNumber, Timestamp createdAt, Timestamp updatedAt,
                   int versionNumber, boolean isActive) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.tckn = tckn;
        this.passportNumber = passportNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.versionNumber = versionNumber;
        this.isActive = isActive;
    }

    public Patient(String firstName, String middleName, String lastName, Date dateOfBirth, char gender, String address, String tckn, String passportNumber, int versionNumber, Timestamp createdAt, Timestamp updatedAt, boolean isActive, List<PhoneNumber> phoneNumbers, List<EmailAddress> emailAddresses) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.tckn = tckn;
        this.passportNumber = passportNumber;
        this.versionNumber = versionNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isActive = isActive;
        this.phoneNumbers = phoneNumbers;
        this.emailAddresses = emailAddresses;
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

    public void addPhoneNumber(PhoneNumber phoneNumber) {
        phoneNumbers.add(phoneNumber);
        phoneNumber.setPatient(this);
    }

    public void removePhoneNumber(PhoneNumber phoneNumber) {
        phoneNumbers.remove(phoneNumber);
        phoneNumber.setPatient(null);
    }

    public void addEmailAddress(EmailAddress emailAddress) {
        emailAddresses.add(emailAddress);
        emailAddress.setPatient(this);
    }

    public void removeEmailAddress(EmailAddress emailAddress) {
        emailAddresses.remove(emailAddress);
        emailAddress.setPatient(null);
    }

    public void arrangePatientEmailAddress(){
        for (EmailAddress email:this.getEmailAddresses()){
            email.setPatient(this);
        }
    }

    public void arrangePatientPhoneNumbers(){
        for (PhoneNumber number:this.getPhoneNumbers()){
            number.setPatient(this);
        }
    }

    public void arrangeUpdateTime(){
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());

        this.setUpdatedAt(Timestamp.from(zonedDateTime.toInstant()));
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
                ", versionNumber=" + versionNumber +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", isActive=" + isActive +
                ", phoneNumbers=" + phoneNumbers +
                ", emailAddresses=" + emailAddresses +
                '}';
    }
}
