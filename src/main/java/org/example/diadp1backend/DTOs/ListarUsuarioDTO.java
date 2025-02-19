package org.example.diadp1backend.DTOs;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListarUsuarioDTO {
    private Integer id;
    private String nombre;
    private String foto;
}
