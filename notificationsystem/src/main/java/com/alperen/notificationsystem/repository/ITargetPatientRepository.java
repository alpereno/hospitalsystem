package com.alperen.notificationsystem.repository;

import com.alperen.notificationsystem.entity.TargetPatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITargetPatientRepository extends JpaRepository<TargetPatient, Integer> {
}
