package org.example.diadp1backend.DTOs;

import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatDTO {
    private Integer id;
    private String nombre;
    private String tipo;
    private List<String> foto;
    private String ultimoMensaje;
    private String fechaUltimoMensaje;
}