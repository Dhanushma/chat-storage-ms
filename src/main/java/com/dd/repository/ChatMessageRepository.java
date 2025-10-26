package com.dd.repository;

import com.dd.entity.ChatMessage;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    Optional<List<ChatMessage>> findBySessionId(long sessionId);

    Page<ChatMessage> findBySessionIdOrderByCreatedOnAsc(long sessionId, Pageable pageable);

    @Modifying
    @Query("delete from ChatMessage m where m.sessionId = :sessionId")
    void deleteBySessionId(@Param("sessionId") long sessionId);

}
