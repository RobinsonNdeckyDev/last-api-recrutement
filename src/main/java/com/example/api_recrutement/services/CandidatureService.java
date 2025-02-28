package com.example.api_recrutement.services;

import com.example.api_recrutement.models.*;
import com.example.api_recrutement.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidatureService {
    private final DocumentRepository documentRepository;
    private final CandidatureRepository candidatureRepository;
    private final UserRepository userRepository;
    private final AnnonceRepository annonceRepository;

    public CandidatureService(
            DocumentRepository documentRepository,
            CandidatureRepository candidatureRepository,
            UserRepository userRepository,
            AnnonceRepository annonceRepository
    ) {
        this.documentRepository = documentRepository;
        this.candidatureRepository = candidatureRepository;
        this.userRepository = userRepository;
        this.annonceRepository = annonceRepository;
    }

    public List<Candidature> getAllCandidatures() {
        return candidatureRepository.findAll();
    }

    public Optional<Candidature> getCandidatureById(Long id) {
        return candidatureRepository.findById(id);
    }

    public Candidature createCandidature(Long userId, Long annonceId, List<Long> documentIds) {
        // On recupere le user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User non trouvé"));

        // on recupere l'annonce
        Annonce annonce = annonceRepository.findById(annonceId)
                .orElseThrow(() -> new RuntimeException("Annonce non trouvée"));

        // on recupere les documents
        List<Document> documents = documentRepository.findAllById(documentIds);
        if (documents.size() != documentIds.size()) {
            throw new RuntimeException("certains documents sont introuvables");
        }

        Candidature candidature = new Candidature();
        candidature.setUser(user);
        candidature.setAnnonce(annonce);
        candidature.setDocumentIds(documents);
        candidature.setEtat(EtatCandidature.PENDING);

        return candidatureRepository.save(candidature);
    }

    public Candidature updateCandidature(Long id, Candidature candidature) {
        Optional<Candidature> candidatureOptional = candidatureRepository.findById(id);
        if (!candidatureOptional.isPresent()) {
            throw new RuntimeException("Candidature non trouvée");
        }

        Candidature existingCandidature = candidatureOptional.get();
        existingCandidature.setUser(candidature.getUser());
        existingCandidature.setAnnonce(candidature.getAnnonce());
        existingCandidature.setEtat(candidature.getEtat());
        existingCandidature.setDocumentIds(candidature.getDocumentIds());

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
