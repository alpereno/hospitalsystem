package com.alperen.hospitalsystem.service.abstracts;

import com.alperen.hospitalsystem.entity.PatientName;

import java.util.List;

public interface IPatientNameService {
    public List<PatientName> findAll();
    public List<PatientName> findByFirstName();
    public List<PatientName> findByLastName();
    public PatientName save(PatientName patientName);
    public void deleteById(int id);
}
