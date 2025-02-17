package org.example.diadp1backend.repositorios;

import org.example.diadp1backend.modelos.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface MensajeRepository extends JpaRepository<Mensaje, Integer> {
    @Query(value = "SELECT * FROM bbdd_santuario.mensajes m WHERE m.id_chat = :idChat ORDER BY m.fecha asc", nativeQuery = true)
    List<Mensaje> findMensajesByChatId(Integer idChat);



}
