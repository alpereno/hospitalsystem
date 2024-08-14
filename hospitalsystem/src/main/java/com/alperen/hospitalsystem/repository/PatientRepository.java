package com.alperen.hospitalsystem.repository;

import com.alperen.hospitalsystem.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
}
