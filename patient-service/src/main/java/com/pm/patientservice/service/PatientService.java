package com.pm.patientservice.service;

/**
 * @author User
 * @since 07-11-2026
 */

import com.pm.patientservice.dtos.PatientRequestDto;
import com.pm.patientservice.dtos.PatientResponseDTO;
import com.pm.patientservice.exception.EmailAlreadyExistsException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.grpc.BillingServiceGrpcClient;
import com.pm.patientservice.kafka.KafkaProducer;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {

    private static final Logger log = LoggerFactory.getLogger(PatientService.class);
    private final PatientRepository patientRepository;

    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final KafkaProducer kafkaProducer;

    public PatientService(PatientRepository patientRepository,
                          BillingServiceGrpcClient billingServiceGrpcClient, KafkaProducer kafkaProducer) {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.kafkaProducer = kafkaProducer;
    }

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();

        return patients.stream().map(PatientMapper::toDto).toList();
    }


    public PatientResponseDTO createPatient(PatientRequestDto patientRequestDto) {


        log.info("STEP 1: Starting patient creation");

        if (patientRepository.existsByEmail(patientRequestDto.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email already exists:" + patientRequestDto.getEmail());
        }

        Patient patient = patientRepository.save(PatientMapper.toModel(patientRequestDto));

        log.info("STEP 2: Patient saved successfully: {}", patient.getId());

        log.info("STEP 3: Calling billing-service via gRPC");


        billingServiceGrpcClient.createBillingAccount(patient.getId().toString(),
                patient.getName(),patient.getEmail());

        kafkaProducer.sendEvent(patient);

        log.info("STEP 4: Billing gRPC call completed");


        return PatientMapper.toDto(patient);
    }

    public PatientResponseDTO updatePatient(
            UUID id,
            PatientRequestDto patientReqDto) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Patient not found"));

        if (patientReqDto.getName() != null) {
            patient.setName(patientReqDto.getName());
        }

        if (patientReqDto.getEmail() != null) {
            patient.setEmail(patientReqDto.getEmail());
        }

        if (patientReqDto.getAddress() != null) {
            patient.setAddress(patientReqDto.getAddress());
        }

        if (patientReqDto.getDateOfBirth() != null) {
            patient.setDateOfBirth(
                    LocalDate.parse(
                            patientReqDto.getDateOfBirth()
                    ).atStartOfDay()
            );
        }

        if (patientReqDto.getRegisteredDate() != null) {
            patient.setRegisteredDate(
                    LocalDate.parse(
                            patientReqDto.getRegisteredDate()
                    ).atStartOfDay()
            );
        }

        Patient updatedPatient = patientRepository.save(patient);

        return PatientMapper.toDto(updatedPatient);
    }

    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }

}