package com.alperen.hospitalsystem.repository.abstracts;

import com.alperen.hospitalsystem.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface IPatientRepository extends JpaRepository<Patient, Integer> {
    public List<Patient> findAllByIsActiveTrue();
    // find by name (using like) does not matter case and based on active status
    public List<Patient> findByFirstNameContainingIgnoreCaseAndIsActiveTrue(String firstName);
    public List<Patient> findByLastNameContainingIgnoreCaseAndIsActiveTrue(String lastName);
    public List<Patient> findByGenderAndIsActiveTrue(char gender);
    List<Patient> findByDateOfBirthBetweenAndIsActiveTrue(Timestamp startDate, Timestamp endDate);

}
