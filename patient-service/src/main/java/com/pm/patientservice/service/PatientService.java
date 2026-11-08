package com.pm.patientservice.service;

/**
 * @author User
 * @since 07-11-2026
 */

import com.pm.patientservice.dtos.PatientRequestDto;
import com.pm.patientservice.dtos.PatientResponseDTO;
import com.pm.patientservice.exception.EmailAlreadyExistsException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();

        return patients.stream().map(PatientMapper::toDto).toList();
    }


    public PatientResponseDTO createPatient(PatientRequestDto patientRequestDto) {

        if (patientRepository.existsByEmail(patientRequestDto.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email already exists:" + patientRequestDto.getEmail());
        }

        Patient patient = patientRepository.save(PatientMapper.toModel(patientRequestDto));
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