// src/main/java/org/example/diadp1backend/DTOs/CrearChatDTO.java
package org.example.diadp1backend.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrearChatDTO {
    private String nombre;
    private String tipo;
    private List<String> miembros;
}