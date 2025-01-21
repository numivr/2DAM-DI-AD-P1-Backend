package org.example.diadp1backend.DTOs;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatWithProfilesDTO {
    private Integer idChat;
    private String nombreChat;
    private String tipoChat;
    private Integer idUsuario;
    private String nombreUsuario;
    private String fotoPerfil;
}

