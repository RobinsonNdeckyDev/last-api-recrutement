package com.example.api_recrutement.dtos;

import com.example.api_recrutement.models.EtatCandidature;
import lombok.Data;

import java.util.List;

@Data
public class CandidatureDTO {
    private EtatCandidature etat;
    private Long annonceId;
    private Long candidatId;
    private List<Long> documentIds; // Utilisez une liste d'identifiants pour les documents
}
