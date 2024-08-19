package com.alperen.notificationsystem.repository;

import com.alperen.notificationsystem.entity.TargetPatient;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ITargetPatientRepository extends JpaRepository<TargetPatient, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM TargetPatient t WHERE t.notificationId = :notificationId")
    void deleteByNotificationId(@Param("notificationId") Integer notificationId);
}
