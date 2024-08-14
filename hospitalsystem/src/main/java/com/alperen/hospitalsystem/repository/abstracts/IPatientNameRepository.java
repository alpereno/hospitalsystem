package com.alperen.hospitalsystem.repository.abstracts;

import com.alperen.hospitalsystem.entity.PatientName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPatientNameRepository extends JpaRepository<PatientName, Integer> {
}
