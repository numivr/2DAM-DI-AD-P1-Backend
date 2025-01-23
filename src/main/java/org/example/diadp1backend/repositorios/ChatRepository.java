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
    Select distinct(m.id_chat) from bbdd_santuario.miembros_chat m where (m.id_usuario = :idUsuario)
""")
    List<Integer> findChatsByUsuarioId(Integer idUsuario);

    List<Chat> findChatsById(Integer id);
}