package org.example.diadp1backend.repositorios;


import org.example.diadp1backend.modelos.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {



    Optional<Usuario> findTopByNombre(String nombre);

    //Este repositorio tiene otra estructura diferente a otros repositorios, estoy siguiendo el tutorial
}
