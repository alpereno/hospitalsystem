package com.alperen.hospitalsystem.repository;

import com.alperen.hospitalsystem.entity.EmailAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailAddressRepository extends JpaRepository<EmailAddress, Integer> {
}
