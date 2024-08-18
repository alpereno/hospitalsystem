package com.alperen.hospitalsystem.service.abstracts;

import com.alperen.hospitalsystem.Request.PatientRequest;
import com.alperen.hospitalsystem.Response.PatientResponse;
import com.alperen.hospitalsystem.entity.Patient;
import com.alperen.hospitalsystem.exception.IncorrectSavePatientException;

import java.util.List;

public interface IPatientService {
    public List<PatientResponse> findAll();
    public PatientResponse findById(int id);
    public List<PatientResponse> findByName(String name);
    public List<PatientResponse> findByLastName(String lastName);
    public List<PatientResponse> findByGender(char gender);
    public List<PatientResponse> findByDateOfBirthBetween(int startAge, int endAge);
    public List<PatientResponse> findByAgeRangeAndGender(int startAge, int endAge, char gender);
    //public Patient save(PatientRequest patient);
    public PatientResponse save(PatientRequest patient) throws IncorrectSavePatientException;
    public PatientResponse update(int id, PatientRequest updatedPatient);
    public boolean deleteById(int id);

    //public Patient saveV2(PatientRequest patientRequest);
}
