package org.example.diadp1backend.repositorios;

import org.example.diadp1backend.modelos.Comentario;
import org.example.diadp1backend.modelos.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Integer> {

  @Query("SELECT c FROM Comentario c WHERE c.publicacion.id = :idPublicacion ORDER BY c.fecha DESC")
  ArrayList<Comentario> obtenerComentariosPorPublicacionId(Integer idPublicacion);

}
