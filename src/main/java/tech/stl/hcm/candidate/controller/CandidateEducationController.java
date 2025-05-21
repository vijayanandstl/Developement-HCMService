package tech.stl.hcm.candidate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.stl.hcm.candidate.dto.CandidateEducationDTO;
import tech.stl.hcm.candidate.service.CandidateEducationService;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/candidates/{candidateId}/education")
public class CandidateEducationController {

    private final CandidateEducationService educationService;

    @Autowired
    public CandidateEducationController(CandidateEducationService educationService) {
        this.educationService = educationService;
    }

    @PostMapping
    public ResponseEntity<CandidateEducationDTO> createEducation(
            @PathVariable UUID candidateId,
            @RequestBody CandidateEducationDTO educationDTO) {
        educationDTO.setCandidateId(candidateId);
        return ResponseEntity.ok(educationService.createEducation(educationDTO));
    }

    @GetMapping
    public ResponseEntity<List<CandidateEducationDTO>> getEducationByCandidateId(
            @PathVariable UUID candidateId) {
        return ResponseEntity.ok(educationService.getEducationByCandidateId(candidateId));
    }

    @DeleteMapping("/{educationId}")
    public ResponseEntity<Void> deleteEducation(
            @PathVariable UUID candidateId,
            @PathVariable Integer educationId) {
        educationService.deleteEducation(educationId);
        return ResponseEntity.noContent().build();
    }
} 