package org.example.diadp1backend.repositorios;


import jakarta.transaction.Transactional;
import org.example.diadp1backend.modelos.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

  @Transactional
  @Query(value = "SELECT u FROM Usuario u WHERE u.nombre = :nombre")
  Optional<Usuario> findTopByNombre(@Param("nombre") String nombre);

  @Transactional
  @Query(value = "select u.id from Usuario u where u.nombre = :nombre")
  Integer findUsuarioIdByNombre(String nombre);
}
