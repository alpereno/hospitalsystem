package com.alperen.hospitalsystem.repository.abstracts;

import com.alperen.hospitalsystem.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface IPatientRepository extends JpaRepository<Patient, Integer> {
    public List<Patient> findByFirstName(String firstName);
    public List<Patient> findByLastName(String lastName);
    public List<Patient> findByGender(char gender);
    public List<Patient> findByDateOfBirthBetween(Timestamp startDate, Timestamp endDate);
}
