package com.textshare.repository;

import com.textshare.entity.Text;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TextRepository extends JpaRepository<Text, String> {

    @Modifying
    @Query("DELETE FROM Text t WHERE t.expiresAt < :now")
    int deleteByExpiresAtBefore(@Param("now") LocalDateTime now);

    @Query("SELECT t FROM Text t WHERE t.expiresAt > :now AND t.isDeleted = false")
    List<Text> findActiveTexts(@Param("now") LocalDateTime now);

    @Modifying
    @Query("UPDATE Text t SET t.viewCount = :count WHERE t.id = :id")
    int updateViewCount(@Param("id") String id, @Param("count") Integer count);
}
