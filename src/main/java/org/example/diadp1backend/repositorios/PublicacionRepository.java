package org.example.diadp1backend.repositorios;

import jakarta.transaction.Transactional;
import org.example.diadp1backend.modelos.Publicacion;
import org.example.diadp1backend.modelos.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Integer> {

  @Query("SELECT p FROM Publicacion p LEFT JOIN FETCH p.comentarios LEFT JOIN FETCH p.likes")
  List<Publicacion> findAllWithDetails();

  @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END " +
    "FROM Publicacion p JOIN p.likes u WHERE p.id = :idPublicacion AND u.id = :idUsuario")
  boolean existsLikeByPublicacionAndUsuario(@Param("idPublicacion") Integer idPublicacion,
                                            @Param("idUsuario") Integer idUsuario);

}
