package gtp.hms.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class PatientAdmission {
    private UUID id;
    private UUID patientId;
    private UUID wardId;
    private int bedNumber;
    private String diagnosis;
    private UUID treatingDoctorId;
    private LocalDate admissionDate;
    private LocalDate dischargeDate;
    private boolean isCurrent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PatientAdmission(UUID patientId, UUID wardId, int bedNumber, String diagnosis,
                            UUID treatingDoctorId, LocalDate admissionDate, boolean isCurrent, LocalDate dischargeDate) {

        this.id = UUID.randomUUID();
        this.patientId = patientId;
        this.wardId = wardId;
        this.bedNumber = bedNumber;
        this.diagnosis = diagnosis;
        this.treatingDoctorId = treatingDoctorId;
        this.admissionDate = admissionDate;
        this.dischargeDate = dischargeDate;
        this.isCurrent = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public PatientAdmission(UUID id, UUID patientId, UUID wardId, int bedNumber,
                            String diagnosis, UUID treatingDoctorId, LocalDate admissionDate,
                            boolean isCurrent, LocalDateTime createdAt, LocalDateTime updatedAt) {

        this.id = id;
        this.patientId = patientId;
        this.wardId = wardId;
        this.bedNumber = bedNumber;
        this.diagnosis = diagnosis;
        this.treatingDoctorId = treatingDoctorId;
        this.admissionDate = admissionDate;
        this.isCurrent = isCurrent;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public UUID getWardId() {
        return wardId;
    }

    public void setWardId(UUID wardId) {
        this.wardId = wardId;
    }

    public int getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(int bedNumber) {
        this.bedNumber = bedNumber;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public UUID getTreatingDoctorId() {
        return treatingDoctorId;
    }

    public void setTreatingDoctorId(UUID treatingDoctorId) {
        this.treatingDoctorId = treatingDoctorId;
    }

    public LocalDate getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(LocalDate admissionDate) {
        this.admissionDate = admissionDate;
    }

}
