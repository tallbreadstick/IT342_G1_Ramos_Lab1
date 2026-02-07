package com.tallbreadstick.backend.service;

import com.tallbreadstick.backend.entity.AuthLog;
import com.tallbreadstick.backend.entity.User;
import com.tallbreadstick.backend.repository.AuthLogRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthLogService {

    private final AuthLogRepository authLogRepository;

    public AuthLogService(AuthLogRepository authLogRepository) {
        this.authLogRepository = authLogRepository;
    }

    public void log(AuthLog.Action action, User user, AuthLog.Status status, String ip) {
        AuthLog log = new AuthLog();
        log.setAction(action);
        log.setTargetAccount(user);
        log.setStatus(status);
        log.setSourceIp(ip);

        authLogRepository.save(log);
    }
}
