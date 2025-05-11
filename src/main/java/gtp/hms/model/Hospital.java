package gtp.hms.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Hospital {
    private UUID id;
    private String name;
    private String branch;
    private String address;
    private String city;
    private String state;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public Hospital(UUID id, String name, String branch, String address,
                    String city, String state, LocalDateTime createdAt, LocalDateTime updatedAt) {

        this.id = id;
        this.name = name;
        this.branch = branch;
        this.address = address;
        this.city = city;
        this.state = state;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Hospital(String name, String branch, String address, String city, String state) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.branch = branch;
        this.address = address;
        this.city = city;
        this.state = state;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCity() {
        return city;
    }
}
