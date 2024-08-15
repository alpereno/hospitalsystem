package com.alperen.hospitalsystem.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "phone_numbers")
public class PhoneNumber {
    // Annotate the class as an entity and map to db table
    // define fields
    // annotate fields with db column names
    // create constructors
    // generate getter/setter methods
    // generate toString() method

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phone_id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "phone_type")
    private String phoneType;

    public PhoneNumber() {
    }

    public PhoneNumber(Patient patient, String phoneNumber, String phoneType) {
        this.patient = patient;
        this.phoneNumber = phoneNumber;
        this.phoneType = phoneType;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    @Override
    public String toString() {
        return "PhoneNumber{" +
                "id=" + id +
                ", patient=" + patient +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", phoneType='" + phoneType + '\'' +
                '}';
    }
}
