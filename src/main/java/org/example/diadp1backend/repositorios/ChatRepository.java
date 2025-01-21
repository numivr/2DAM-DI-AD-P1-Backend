package org.example.diadp1backend.repositorios;

import org.example.diadp1backend.DTOs.ChatWithProfilesDTO;
import org.example.diadp1backend.modelos.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

    @Query(value = """
        SELECT DISTINCT c.id AS id_chat, 
                        c.nombre AS nombre_chat, 
                        c.tipo AS tipo_chat, 
                        u.id AS id_usuario, 
                        u.nombre AS nombre_usuario, 
                        cualidades.foto AS foto_perfil
        FROM bbdd_santuario.Chats c
        JOIN bbdd_santuario.Mensajes m ON c.id = m.id_chat
        JOIN bbdd_santuario.Miembros_Chat mc ON c.id = mc.id_chat
        JOIN bbdd_santuario.Usuarios u ON mc.id_usuario = u.id
        LEFT JOIN bbdd_santuario.Cualidades cualidades ON u.id = cualidades.id_usuario
        WHERE mc.id_usuario = :idUsuario
        ORDER BY c.id
        """,
            nativeQuery = true)
    List<ChatWithProfilesDTO> findActiveChatsWithUserProfiles(@Param("idUsuario") Integer idUsuario);
}
