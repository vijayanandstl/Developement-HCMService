package tech.stl.hcm.candidate.exception;

public class CandidateNotFoundException extends RuntimeException {
    public CandidateNotFoundException(String message) {
        super(message);
    }
} 