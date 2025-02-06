package org.example.diadp1backend.repositorios;


import org.example.diadp1backend.modelos.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

  @Query(value = "SELECT u FROM Usuario u WHERE u.nombre = :nombre")
  Optional<Usuario> findTopByNombre(@Param("nombre") String nombre);

  @Query(value = "select u.id from bbdd_santuario.usuarios u where u.nombre = :nombre LIMIT 1", nativeQuery = true)
  Integer findUsuarioIdByNombre(String nombre);
}
