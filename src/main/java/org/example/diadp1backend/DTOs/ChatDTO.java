package org.example.diadp1backend.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {
    private String nombrePerfil;
    private String fotoPerfil;
    private String ultimoMensaje;
    private LocalDateTime fechaUltimoMensaje;
}