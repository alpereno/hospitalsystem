package com.alperen.hospitalsystem.service.abstracts;

import com.alperen.hospitalsystem.Request.PatientRequest;
import com.alperen.hospitalsystem.entity.Patient;

import java.util.List;

public interface IPatientService {
    public List<Patient> findAll();
    public Patient findById(int id);
    public List<Patient> findByName(String name);
    public List<Patient> findByLastName(String lastName);
    public List<Patient> findByGender(char gender);
    public List<Patient> findByDateOfBirthBetween(int startAge, int endAge);
    //public Patient save(PatientRequest patient);
    public Patient save(Patient patient);
    public Patient update(int id, Patient updatedPatient);
    public Patient deleteById(int id);
}
