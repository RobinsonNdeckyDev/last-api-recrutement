package com.example.api_recrutement.mappers;

import com.example.api_recrutement.dtos.TypeDocumentDTO;
import com.example.api_recrutement.models.TypeDocument;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TypeDocumentMapper {
    TypeDocumentMapper INSTANCE = Mappers.getMapper(TypeDocumentMapper.class);

    TypeDocumentDTO toTypeDocumentDTO(TypeDocument typeDocument);
    TypeDocument toTypeDocument(TypeDocumentDTO typeDocumentDTO);
}
