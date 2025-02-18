package com.example.api_recrutement.mappers;

import com.example.api_recrutement.dtos.CandidatureDTO;
import com.example.api_recrutement.models.Candidature;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CandidatureMapper {
    CandidatureMapper INSTANCE = Mappers.getMapper(CandidatureMapper.class);

    CandidatureDTO toCandidatureDTO(Candidature candidature);

    Candidature toCandidature(CandidatureDTO candidatureDTO);
}
