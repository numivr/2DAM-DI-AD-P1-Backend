package org.example.diadp1backend.repositorios;

import org.example.diadp1backend.DTOs.ChatWithProfilesDTO;
import org.example.diadp1backend.modelos.Chat;
import org.example.diadp1backend.modelos.Mensaje;
import org.example.diadp1backend.modelos.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

    @Query(value = "Select m.id_chat from bbdd_santuario.miembros_chat m where (m.id_usuario = :idUsuario)", nativeQuery = true)
    List<Integer> findChatsByUsuarioId(Integer idUsuario);

    @Query(value = "Select u.nombre from bbdd_santuario.usuarios u join bbdd_santuario.miembros_chat m on (m.id_usuario = u.id) where (m.id_chat = :idChat and u.id != :idUsuario)", nativeQuery = true)
    String findNombreUsuarioByChatId(Integer idChat, Integer idUsuario);

    @Query(value = "SELECT c.foto FROM bbdd_santuario.Cualidades c join bbdd_santuario.usuarios u on (c.id_usuario = u.id) join bbdd_santuario.miembros_chat m on (u.id = m.id_usuario) WHERE m.id_chat = :idChat and u.id != :idUsuario limit 5", nativeQuery = true)
    List<String> findFotoByUsuarioId(Integer idChat, Integer idUsuario);


    @Query(value = "select m from Mensaje m " +
            "join Chat c on (m.chat.id = :idChat) " +
            "order by m.fecha desc limit 1" )
    Mensaje getUltimoMensaje(Integer idChat);


    Chat findChatById(Integer id);

    @Modifying
    @Query(value = "INSERT INTO bbdd_santuario.miembros_chat (id_chat, id_usuario) VALUES (:chatId, :miembroId)", nativeQuery = true)
    void saveMiembroChat(@Param("chatId") Integer chatId, @Param("miembroId") Integer miembroId);

    @Modifying
    @Query(value = "DELETE FROM bbdd_santuario.miembros_chat WHERE id_chat = :chatId AND id_usuario = :userId", nativeQuery = true)
    void deleteMiembroFromChat(@Param("chatId") Integer chatId, @Param("userId") Integer userId);

}