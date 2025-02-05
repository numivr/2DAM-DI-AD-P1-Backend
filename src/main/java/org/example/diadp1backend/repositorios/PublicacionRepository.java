package org.example.diadp1backend.repositorios;

import org.example.diadp1backend.modelos.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PublicacionRepository extends JpaRepository<Publicacion, Integer> {

  @Query("SELECT p FROM Publicacion p LEFT JOIN FETCH p.comentarios LEFT JOIN FETCH p.likes")
  List<Publicacion> findAllWithDetails();


}
