package com.example.api_recrutement.services;

import com.example.api_recrutement.models.*;
import com.example.api_recrutement.repository.CandidatRepository;
import com.example.api_recrutement.repository.CandidatureRepository;
import com.example.api_recrutement.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidatureService {
    private final DocumentRepository documentRepository;
    private final CandidatureRepository candidatureRepository;
    private final CandidatRepository candidatRepository;

    public CandidatureService(DocumentRepository documentRepository, CandidatureRepository candidatureRepository, CandidatRepository candidatRepository) {
        this.documentRepository = documentRepository;
        this.candidatureRepository = candidatureRepository;
        this.candidatRepository = candidatRepository;
    }

    public List<Candidature> getAllCandidatures() {
        return candidatureRepository.findAll();
    }

    public Optional<Candidature> getCandidatureById(Long id) {
        return candidatureRepository.findById(id);
    }

    public Candidature createCandidature(Long candidatId, Long annonceId, List<Long> documentIds) {
        Candidat candidat = candidatRepository.findById(candidatId)
                .orElseThrow(() -> new RuntimeException("Candidat non trouvé"));

        Annonce annonce = new Annonce(); // Récupérez ou créez l'annonce ici

        List<Document> documents = documentRepository.findAllById(documentIds);

        Candidature candidature = new Candidature();
        candidature.setCandidat(candidat);
        candidature.setAnnonce(annonce);
        candidature.setDocuments(documents);

        return candidatureRepository.save(candidature);
    }

    public Candidature updateCandidature(Long id, Candidature candidature) {
        Optional<Candidature> candidatureOptional = candidatureRepository.findById(id);
        if (!candidatureOptional.isPresent()) {
            throw new RuntimeException("Candidature non trouvée");
        }

        Candidature existingCandidature = candidatureOptional.get();
        existingCandidature.setCandidat(candidature.getCandidat());
        existingCandidature.setAnnonce(candidature.getAnnonce());
        existingCandidature.setEtat(candidature.getEtat());
        existingCandidature.setDocuments(candidature.getDocuments());

        return candidatureRepository.save(existingCandidature);
    }

    public void deleteCandidature(Long id) {
        Optional<Candidature> candidatureOptional = candidatureRepository.findById(id);
        if (!candidatureOptional.isPresent()) {
            throw new RuntimeException("Candidature non trouvée");
        }

        candidatureRepository.delete(candidatureOptional.get());
    }
}
