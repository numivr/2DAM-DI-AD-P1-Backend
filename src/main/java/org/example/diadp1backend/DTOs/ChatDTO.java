package org.example.diadp1backend.DTOs;

import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatDTO {
    private Integer id;
    private String nombre;
    private String tipo;
    private String ultimoMensaje;
    private Timestamp fechaUltimoMensaje;
}