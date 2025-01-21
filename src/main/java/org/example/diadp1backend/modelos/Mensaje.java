package org.example.diadp1backend.modelos;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mensajes", schema = "santuario")
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Mensaje {
//     id SERIAL PRIMARY KEY,
//    id_chat INT NOT NULL,
//    id_emisor INT NOT NULL,
//    contenido TEXT NOT NULL CHECK (contenido <> ''), -- Asegurar que no esté vacío
//    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "contenido")
    private String contenido;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "id_chat")
    private Integer id_chat;

    @ManyToOne(targetEntity = Usuario.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_emisor")
    private Integer id_emisor;
}
