package com.example.api_recrutement.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "candidatures")
@Data
@NoArgsConstructor
public class Candidature extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EtatCandidature etat;

    @OneToOne
    @JoinColumn(name = "id_candidat", nullable = false)
    private Candidat candidat;

    @OneToOne
    @JoinColumn(name = "id_annonce", nullable = false)
    private Annonce annonce;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "candidature_documents",
            joinColumns = @JoinColumn(name = "candidature_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id")
    )
    private List<Document> documents;

}
