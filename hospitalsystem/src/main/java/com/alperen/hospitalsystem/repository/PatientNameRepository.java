package com.alperen.hospitalsystem.repository;

import com.alperen.hospitalsystem.entity.PatientName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientNameRepository extends JpaRepository<PatientName, Integer> {
}
