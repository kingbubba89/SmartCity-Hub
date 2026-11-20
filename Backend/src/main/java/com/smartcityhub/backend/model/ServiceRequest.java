
package com.smartcityhub.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_requests")
public class ServiceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String residentName;
    private String title;

    @Column(length = 2000)
    private String description;

    private String location;
    private String department;
    private String status;

    @Column(length = 2000)
    private String progressNotes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ServiceRequest() {}

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
        if (status == null) status = "SUBMITTED";
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }

    public String getResidentName() { return residentName; }
    public void setResidentName(String residentName) {
        this.residentName = residentName;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() { return location; }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getDepartment() { return department; }
    public void setDepartment(String department) {
        this.department = department;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getProgressNotes() { return progressNotes; }
    public void setProgressNotes(String progressNotes) {
        this.progressNotes = progressNotes;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
