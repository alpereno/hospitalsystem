package com.alperen.hospitalsystem.service;

import com.alperen.hospitalsystem.entity.PatientName;

import java.util.List;

public interface PatientNameService {
    public List<PatientName> findAll();
    public List<PatientName> findByFirstName();
    public List<PatientName> findByLastName();
    public PatientName save(PatientName patientName);
    public void deleteById(int id);
}
