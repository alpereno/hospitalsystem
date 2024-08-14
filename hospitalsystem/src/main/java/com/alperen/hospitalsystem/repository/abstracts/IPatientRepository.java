package com.alperen.hospitalsystem.repository.abstracts;

import com.alperen.hospitalsystem.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPatientRepository extends JpaRepository<Patient, Integer> {
}
