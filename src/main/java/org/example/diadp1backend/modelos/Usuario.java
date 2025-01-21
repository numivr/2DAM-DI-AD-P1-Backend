package org.example.diadp1backend.modelos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Usuarios", schema = "safajobs")
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Usuario {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
}
