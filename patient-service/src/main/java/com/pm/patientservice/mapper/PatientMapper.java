package com.pm.patientservice.mapper;

import com.pm.patientservice.dtos.PatientRequestDto;
import com.pm.patientservice.dtos.PatientResponseDTO;
import com.pm.patientservice.model.Patient;

import java.time.LocalDate;

/**
 * @author User
 * @since 07-11-2026
 */
public class PatientMapper {

    public static PatientResponseDTO toDto(Patient patient) {
        PatientResponseDTO patientResponseDTO = new PatientResponseDTO();
        patientResponseDTO.setId(patient.getId().toString());
        patientResponseDTO.setName(patient.getName());
        patientResponseDTO.setAddress(patient.getAddress());
        patientResponseDTO.setEmail(patient.getEmail());
        patientResponseDTO.setDateOfBirth(patient.getDateOfBirth().toString());
        return patientResponseDTO;
    }

    public static Patient toModel(PatientRequestDto reqDto) {

        Patient patient = new Patient();
        patient.setName(reqDto.getName());
        patient.setAddress(reqDto.getAddress());
        patient.setEmail(reqDto.getEmail());
        patient.setDateOfBirth(LocalDate.parse(reqDto.getDateOfBirth()).atStartOfDay());
        patient.setRegisteredDate(LocalDate.parse(reqDto.getRegisteredDate()).atStartOfDay());
        return patient;

    }

}