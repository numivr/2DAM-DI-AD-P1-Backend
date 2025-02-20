package org.example.diadp1backend.repositorios;

import org.example.diadp1backend.modelos.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface MensajeRepository extends JpaRepository<Mensaje, Integer> {
    @Query(value = "SELECT m FROM Mensaje m WHERE m.chat.id = :idChat ORDER BY m.fecha asc")
    List<Mensaje> findMensajesByChatId(Integer idChat);

    @Query(value = "SELECT c.foto FROM bbdd_santuario.cualidades c " +
            "JOIN bbdd_santuario.usuarios u ON c.id_usuario = u.id " +
            "WHERE u.nombre = :nombreUsuario", nativeQuery = true)
    String getFotoUsuarioByNombreUsuario(String nombreUsuario);



}
