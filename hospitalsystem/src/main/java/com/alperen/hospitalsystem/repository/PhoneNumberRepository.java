package com.alperen.hospitalsystem.repository;

import com.alperen.hospitalsystem.entity.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Integer> {
}
