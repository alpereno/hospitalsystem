package com.alperen.hospitalsystem.repository.abstracts;

import com.alperen.hospitalsystem.entity.EmailAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEmailAddressRepository extends JpaRepository<EmailAddress, Integer> {
}
