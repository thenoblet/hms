package gtp.hms.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Ward {
    private UUID id;
    private int wardNumber;
    private int numberOfBeds;
    private UUID departmentId;
    private UUID supervisorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Ward() {}

    public Ward(int wardNumber, int numberOfBeds, UUID departmentId, UUID supervisorId) {

        this.id = UUID.randomUUID();
        this.wardNumber = wardNumber;
        this.numberOfBeds = numberOfBeds;
        this.departmentId = departmentId;
        this.supervisorId = supervisorId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Ward(UUID id, int wardNumber, int numberOfBeds, UUID departmentId,
                UUID supervisorId, LocalDateTime createdAt, LocalDateTime updatedAt) {

        this.id = id;
        this.wardNumber = wardNumber;
        this.numberOfBeds = numberOfBeds;
        this.departmentId = departmentId;
        this.supervisorId = supervisorId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setId() {
        this.id = UUID.randomUUID();
    }

    public int getWardNumber() {
        return wardNumber;
    }

    public void setWardNumber(int wardNumber) {
        this.wardNumber = wardNumber;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public void setNumberOfBeds(int numberOfBeds) {
        this.numberOfBeds = numberOfBeds;
    }

    public UUID getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }

    public UUID getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(UUID supervisorId) {
        this.supervisorId = supervisorId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }


}
