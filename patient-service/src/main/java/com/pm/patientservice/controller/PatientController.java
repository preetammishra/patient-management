package com.pm.patientservice.controller;

import com.pm.patientservice.dtos.PatientRequestDto;
import com.pm.patientservice.dtos.PatientResponseDTO;
import com.pm.patientservice.service.PatientService;
import com.pm.patientservice.validation.OnCreate;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author User
 * @since 07-11-2026
 */

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }


    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getPatients() {
        List<PatientResponseDTO> patientResponseDTOList = patientService.getPatients();
        return ResponseEntity.ok().body(patientResponseDTOList);
    }

    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(@Validated(OnCreate.class) @RequestBody PatientRequestDto patientReqDto) {
        PatientResponseDTO patientResponseDTO = patientService.createPatient(patientReqDto);
        return ResponseEntity.ok().body(patientResponseDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID id, @Validated({Default.class}) @RequestBody PatientRequestDto patientReqDto) {
        PatientResponseDTO patientResponseDTO = patientService.updatePatient(id,patientReqDto);
        return ResponseEntity.ok().body(patientResponseDTO);
    }

}