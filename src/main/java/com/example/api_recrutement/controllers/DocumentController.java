package com.example.api_recrutement.controllers;

import com.example.api_recrutement.mappers.DocumentMapper;
import com.example.api_recrutement.models.Candidature;
import com.example.api_recrutement.models.Document;
import com.example.api_recrutement.models.TypeDocument;
import com.example.api_recrutement.repository.CandidatureRepository;
import com.example.api_recrutement.services.DocumentService;
import com.example.api_recrutement.dtos.DocumentDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    private final CandidatureRepository candidatureRepository;
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService, CandidatureRepository candidatureRepository) {
        this.documentService = documentService;
        this.candidatureRepository = candidatureRepository;
    }

    public Optional<Candidature> getCandidatureById(Long id) {
        return candidatureRepository.findById(id);
    }

    @GetMapping
    public List<Document> getAllDocuments() {
        return documentService.getAllDocuments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {
        return documentService.getDocumentById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Document> createDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("titre") String titre,
            @RequestParam("description") String description,
            @RequestParam("typeDocumentId") Long typeDocumentId,
            @RequestParam("candidatId") Long candidatId
           ) {

        try {
            // Récupérez ou créez les objets nécessaires
            TypeDocument typeDocument = documentService.getDocumentById(typeDocumentId)
                    .orElseThrow(() -> new RuntimeException("TypeDocument not found")).getTypeDocument();

            Document document = documentService.createDocument(file, titre, description, typeDocument, candidatId);
            return ResponseEntity.status(HttpStatus.CREATED).body(document);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Document> updateDocument(@PathVariable Long id, @RequestBody DocumentDTO documentDTO) {
        Document document = DocumentMapper.INSTANCE.toDocument(documentDTO);
        Document updatedDocument = documentService.updateDocument(id, document);
        return ResponseEntity.ok(updatedDocument);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}









//package com.example.api_recrutement.controllers;
//
//import org.springframework.web.bind.annotation.*;
//import com.example.api_recrutement.models.Document;
//import com.example.api_recrutement.services.DocumentService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import com.example.api_recrutement.dtos.DocumentDTO;
//import com.example.api_recrutement.mappers.DocumentMapper;
//
//import java.util.List;
//
//// Contrôleur pour la gestion des candidats
//@RestController
//@RequestMapping("/documents")
//public class DocumentController {
//    public final DocumentService documentService;
//
//    public DocumentController(DocumentService documentService) {
//        this.documentService = documentService;
//    }
//
//    // Récupère tous les documents
//    @GetMapping
//    public List<Document> getAllDocuments() {
//        return documentService.getAllDocuments();
//    }
//
//    // Récupère un document par son ID
//    @GetMapping("/{id}")
//    public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {
//        return documentService.getDocumentById(id)
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    // Crée un nouveau document
//    @PostMapping
//    public ResponseEntity<Document> createDocument(@RequestBody DocumentDTO documentDTO) {
//        // Convertit DocumentDTO en Document
//        Document document = DocumentMapper.INSTANCE.toDocument(documentDTO);
//        Document createDocument = documentService.createDocument(document);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createDocument);
//    }
//
//    // Met à jour un document
//    @PutMapping("/{id}")
//    public ResponseEntity<Document> updateDocument(@PathVariable Long id, @RequestBody DocumentDTO documentDTO) {
//        // Convertit documentDTO en document
//        Document document = DocumentMapper.INSTANCE.toDocument(documentDTO);
//        Document updateDocument = documentService.updateDocument(id, document);
//        return ResponseEntity.ok(updateDocument);
//    }
//
//    // Supprime un document par son ID
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
//        documentService.deleteDocument(id);
//        return ResponseEntity.noContent().build();
//    }
//}
