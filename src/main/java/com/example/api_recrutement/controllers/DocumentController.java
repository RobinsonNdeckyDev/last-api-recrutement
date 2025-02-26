package com.example.api_recrutement.controllers;

import com.example.api_recrutement.mappers.DocumentMapper;
import com.example.api_recrutement.models.Candidature;
import com.example.api_recrutement.models.Document;
import com.example.api_recrutement.models.TypeDocument;
import com.example.api_recrutement.repository.CandidatureRepository;
import com.example.api_recrutement.services.DocumentService;
import com.example.api_recrutement.dtos.DocumentDTO;
import com.example.api_recrutement.services.TypeDocumentService;
import org.springframework.http.HttpHeaders;
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
    private final TypeDocumentService typeDocumentService;

    public DocumentController(
            DocumentService documentService,
            CandidatureRepository candidatureRepository,
            TypeDocumentService typeDocumentService
    ) {
        this.documentService = documentService;
        this.candidatureRepository = candidatureRepository;
        this.typeDocumentService = typeDocumentService;
    }

    public Optional<Candidature> getCandidatureById(Long id) {
        return candidatureRepository.findById(id);
    }

    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) {
        Document document = documentService.getDocumentById(id)
                .orElseThrow(() -> new RuntimeException("Document non trouvé"));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getTitre() + "\"")
                .body(document.getData());
    }

    @PostMapping
    public ResponseEntity<?> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("titre") String titre,
            @RequestParam("description") String description,
            @RequestParam("typeDocumentId") Long typeDocumentId,
            @RequestParam("userId") Long userId) {
        try {
            TypeDocument typeDocument = typeDocumentService.getTypeDocumentById(typeDocumentId)
                    .orElseThrow(() -> new RuntimeException("Type de document non trouvé"));

            Document document = documentService.createDocument(file, titre, description, typeDocumentId, userId);
            return ResponseEntity.ok(document);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'upload du document: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.ok().build();
    }

}
