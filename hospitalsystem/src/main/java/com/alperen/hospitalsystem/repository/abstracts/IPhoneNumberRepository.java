package com.alperen.hospitalsystem.repository.abstracts;

import com.alperen.hospitalsystem.entity.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPhoneNumberRepository extends JpaRepository<PhoneNumber, Integer> {
}
