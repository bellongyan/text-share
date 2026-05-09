package com.textshare.repository;

import com.textshare.entity.AccessLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {

    Page<AccessLog> findByTextId(String textId, Pageable pageable);

    Page<AccessLog> findByIpAddress(String ipAddress, Pageable pageable);

    @Query("SELECT a FROM AccessLog a WHERE a.accessTime BETWEEN :start AND :end")
    Page<AccessLog> findByTimeRange(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable);
}
