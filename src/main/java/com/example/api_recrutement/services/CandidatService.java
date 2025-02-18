package com.example.api_recrutement.services;

import com.example.api_recrutement.models.Candidat;
import com.example.api_recrutement.repository.CandidatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Service pour la gestion des candidats
@Service
public class CandidatService {
    public final CandidatRepository candidatRepository;

    public CandidatService(CandidatRepository candidatRepository) {
        this.candidatRepository = candidatRepository;
    }

    public List<Candidat> getAllCandidats() {
        return candidatRepository.findAll();
    }

    public Optional<Candidat> getCandidatById(Long id) {
        return candidatRepository.findById(id);
    }

    public Candidat createCandidat(Candidat candidat) {
        return candidatRepository.save(candidat);
    }

    public Candidat updateCandidat(Long id, Candidat candidatDetails) {
        return candidatRepository.findById(id).map(candidat -> {
            candidat.setCvUrl(candidatDetails.getCvUrl());
            candidat.setSiteUrl(candidatDetails.getSiteUrl());
            candidat.setNiveauEtude(candidatDetails.getNiveauEtude());
            candidat.setDomaineEtude(candidatDetails.getDomaineEtude());
            return candidatRepository.save(candidat);
        }).orElseThrow(() -> new RuntimeException("Candidat non existant"));
    }

    public void deleteCandidat(Long id) {
        candidatRepository.deleteById(id);
    }
}
