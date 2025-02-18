package com.example.api_recrutement.services;

import com.example.api_recrutement.models.Candidat;
import com.example.api_recrutement.models.Candidature;
import com.example.api_recrutement.models.Document;
import com.example.api_recrutement.models.TypeDocument;
import com.example.api_recrutement.repository.CandidatRepository;
import com.example.api_recrutement.repository.CandidatureRepository;
import com.example.api_recrutement.repository.DocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final CandidatureRepository candidatureRepository;
    private final CandidatRepository candidatRepository;

    public DocumentService(DocumentRepository documentRepository, CandidatureRepository candidatureRepository, CandidatRepository candidatRepository) {
        this.documentRepository = documentRepository;
        this.candidatureRepository = candidatureRepository;
        this.candidatRepository = candidatRepository;
    }

    public Optional<Candidature> getCandidatureById(Long id) {
        return candidatureRepository.findById(id);
    }

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public Optional<Document> getDocumentById(Long id) {
        return documentRepository.findById(id);
    }

    public Document createDocument(MultipartFile file, String titre, String description, TypeDocument typeDocument, Long candidatId) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Le fichier est vide");
        }

        // Récupérer le candidat par son ID
        Candidat candidat = candidatRepository.findById(candidatId)
                .orElseThrow(() -> new RuntimeException("Candidat non trouvé"));

        byte[] fileData = file.getBytes();
        Document document = new Document(titre, description, fileData, typeDocument);
        document.setCandidat(candidat);

        return documentRepository.save(document);
    }

    public Document updateDocument(Long id, Document documentDetails) {
        return documentRepository.findById(id).map(document -> {
            document.setTitre(documentDetails.getTitre());
            document.setDescription(documentDetails.getDescription());
            document.setTypeDocument(documentDetails.getTypeDocument());
            document.setCandidat(documentDetails.getCandidat());
            return documentRepository.save(document);
        }).orElseThrow(() -> new RuntimeException("Document non existant"));
    }

    public void deleteDocument(Long id) {
        documentRepository.deleteById(id);
    }

}


//@Service
//public class DocumentService {
//    private final DocumentRepository documentRepository;
//
//    public DocumentService(DocumentRepository documentRepository) {
//        this.documentRepository = documentRepository;
//    }
//
//    public List<Document> getAllDocuments() {
//        return documentRepository.findAll();
//    }
//
//    public Optional<Document> getDocumentById(Long id) {
//        return documentRepository.findById(id);
//    }
//
//    public Document enregistrerDocument(MultipartFile file, String titre, String description, TypeDocument typeDocument, Candidature candidature) throws IOException {
//        byte[] fileData = file.getBytes();
//        Document document = new Document(titre, description, fileData, typeDocument);
//        document.setCandidature(candidature);
//        return documentRepository.save(document);
//    }
//
////    public Document createDocument(Document document) {
////        return documentRepository.save(document);
////    }
//
//    public Document updateDocument(Long id, Document documentDetails) {
//        return documentRepository.findById(id).map(document -> {
//            document.setTitre(documentDetails.getTitre());
//            document.setDescription(documentDetails.getDescription());
//            document.setTypeDocument(documentDetails.getTypeDocument());
//            return documentRepository.save(document);
//        }).orElseThrow(() -> new RuntimeException("format document non existant"));
//    }
//
//    public void deleteDocument(Long id) {
//        documentRepository.deleteById(id);
//    }
//}
