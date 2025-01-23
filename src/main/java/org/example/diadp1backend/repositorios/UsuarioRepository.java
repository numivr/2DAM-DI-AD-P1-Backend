package org.example.diadp1backend.repositorios;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository<Usuario> extends JpaRepository<Usuario, Integer> {


}
