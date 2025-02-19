package org.example.diadp1backend.repositorios;


import org.example.diadp1backend.modelos.Cualidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CualidadRepository extends JpaRepository<Cualidad, Integer> {

  @Query("SELECT c.foto FROM Cualidad c WHERE c.usuario.id = :idUsuario")
  Optional<String> obtenerFotoPorUsuarioId(Integer idUsuario);
}
