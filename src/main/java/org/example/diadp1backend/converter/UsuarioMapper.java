package org.example.diadp1backend.converter;


import org.example.diadp1backend.DTOs.UsuarioDTO;
import org.example.diadp1backend.modelos.Usuario;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

  UsuarioDTO toDTO(Usuario entity);

  Usuario toEntity(UsuarioDTO dto);

  List<UsuarioDTO> toDTO(List<Usuario> listEntity);

  List<Usuario> toEntity(List<UsuarioDTO> listDTOs);
}
