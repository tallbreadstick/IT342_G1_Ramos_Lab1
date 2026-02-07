package com.tallbreadstick.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "auth_logs")
public class AuthLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Action action;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_account", nullable = false)
    private User targetAccount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "date_logged", nullable = false, updatable = false)
    private LocalDateTime dateLogged;

    @Column(name = "source_ip", length = 15)
    private String sourceIp;

    @PrePersist
    protected void onCreate() {
        this.dateLogged = LocalDateTime.now();
    }

    // ===== Enums =====

    public enum Action {
        LOGIN,
        REGISTER,
        LOGOUT
    }

    public enum Status {
        OK,
        ERR
    }

    // ===== Constructors =====

    public AuthLog() {}

    // ===== Getters and Setters =====

    public Integer getId() {
        return id;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public User getTargetAccount() {
        return targetAccount;
    }

    public void setTargetAccount(User targetAccount) {
        this.targetAccount = targetAccount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getDateLogged() {
        return dateLogged;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }
}
