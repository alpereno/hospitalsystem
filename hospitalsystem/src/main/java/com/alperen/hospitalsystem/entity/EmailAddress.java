package com.alperen.hospitalsystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "email_addresses")
public class EmailAddress {
    // Annotate the class as an entity and map to db table
    // define fields
    // annotate fields with db column names
    // create constructors
    // generate getter/setter methods
    // generate toString() method

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "email_type")
    private String emailType;

    // Default constructor
    public EmailAddress() {}

    public EmailAddress(Patient patient, String emailAddress, String emailType) {
        this.patient = patient;
        this.emailAddress = emailAddress;
        this.emailType = emailType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailType() {
        return emailType;
    }

    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }

    @Override
    public String toString() {
        return "EmailAddress{" +
                "id=" + id +
                ", patient=" + patient +
                ", emailAddress='" + emailAddress + '\'' +
                ", emailType='" + emailType + '\'' +
                '}';
    }
}
