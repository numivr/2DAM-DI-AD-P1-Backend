package org.example.diadp1backend.repositorios;


import jakarta.transaction.Transactional;
import org.example.diadp1backend.modelos.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

  @Transactional
  @Query(value = "SELECT * FROM bbdd_santuario.usuarios WHERE nombre = :nombre", nativeQuery = true)
  Optional<Usuario> findTopByNombre(@Param("nombre") String nombre);

  @Transactional
  @Query(value = "select u.id from Usuario u where u.nombre = :nombre")
  Integer findUsuarioIdByNombre(String nombre);

  @Query(value = """ 
    SELECT u.* FROM bbdd_santuario.usuarios u
    JOIN bbdd_santuario.follows f1 ON u.id = f1.id_seguido
    JOIN bbdd_santuario.follows f2 ON u.id = f2.id_seguidor
    WHERE f1.id_seguidor = :id 
    AND f2.id_seguido = :id 
    AND u.id != :id
""", nativeQuery = true)
  List<Usuario> findAmigosById(@Param("id") Integer id);

}
