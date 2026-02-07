package com.tallbreadstick.backend.repository;

import com.tallbreadstick.backend.entity.AuthLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthLogRepository extends JpaRepository<AuthLog, Integer> {
}
