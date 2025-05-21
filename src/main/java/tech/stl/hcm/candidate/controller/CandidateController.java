package tech.stl.hcm.candidate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.stl.hcm.candidate.dto.CandidateDTO;
import tech.stl.hcm.candidate.service.CandidateService;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    private final CandidateService candidateService;

    @Autowired
    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @PostMapping
    public ResponseEntity<CandidateDTO> createCandidate(@RequestBody CandidateDTO candidateDTO) {
        return ResponseEntity.ok(candidateService.createCandidate(candidateDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateDTO> getCandidate(@PathVariable UUID id) {
        return candidateService.getCandidate(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CandidateDTO> updateCandidate(@PathVariable UUID id, @RequestBody CandidateDTO candidateDTO) {
        candidateDTO.setCandidateId(id);
        return ResponseEntity.ok(candidateService.updateCandidate(candidateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable UUID id) {
        candidateService.deleteCandidate(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CandidateDTO>> getAllCandidates() {
        return ResponseEntity.ok(candidateService.getAllCandidates());
    }
} 