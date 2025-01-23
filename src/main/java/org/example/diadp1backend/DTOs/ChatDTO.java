package org.example.diadp1backend.DTOs;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatDTO {
    private String nombrePerfil;
    private String fotoPerfil;
    private String ultimoMensaje;

}