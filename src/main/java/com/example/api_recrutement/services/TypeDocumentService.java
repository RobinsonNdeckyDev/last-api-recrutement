package com.example.api_recrutement.services;

import com.example.api_recrutement.models.TypeDocument;
import com.example.api_recrutement.repository.TypeDocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TypeDocumentService {
    private final TypeDocumentRepository typeDocumentRepository;

    public TypeDocumentService(TypeDocumentRepository typeDocumentRepository) {
        this.typeDocumentRepository = typeDocumentRepository;
    }

    public List<TypeDocument> getAllTypeDocuments() {
        return typeDocumentRepository.findAll();
    }

    public Optional<TypeDocument> getTypeDocumentById(Long id) {
        return typeDocumentRepository.findById(id);
    }

    public TypeDocument createTypeDocument(TypeDocument typeDocument) {
        return typeDocumentRepository.save(typeDocument);
    }

    public TypeDocument updateTypeDocument(Long id, TypeDocument typeDocumentDetails) {
        return typeDocumentRepository.findById(id).map(typeDocument -> {
            typeDocument.setNomType(typeDocumentDetails.getNomType());
            typeDocument.setFormatDocument(typeDocumentDetails.getFormatDocument());
            typeDocument.setDescription(typeDocumentDetails.getDescription());
            typeDocument.setEstObligatoire(typeDocumentDetails.getEstObligatoire());
            typeDocument.setEtat(typeDocumentDetails.getEtat());
            return typeDocumentRepository.save(typeDocument);
        }).orElseThrow(() -> new RuntimeException("type document non existant"));
    }

    public void deleteTypeDocument(Long id) {
        typeDocumentRepository.deleteById(id);
    }
}
