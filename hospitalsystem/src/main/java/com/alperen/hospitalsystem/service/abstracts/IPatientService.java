package com.alperen.hospitalsystem.service.abstracts;

import com.alperen.hospitalsystem.entity.Patient;

import java.util.List;

public interface IPatientService {
    public List<Patient> findAll();
    public Patient findById();
    public List<Patient> findByGender();
    public List<Patient> findByAgeRanges(int startRange, int endRange);
    public Patient save(Patient patient);
    public void deleteById(int id);
}
