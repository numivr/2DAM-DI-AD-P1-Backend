// src/main/java/org/example/diadp1backend/servicios/ChatService.java
package org.example.diadp1backend.servicios;

import org.example.diadp1backend.DTOs.ChatDTO;
import org.example.diadp1backend.modelos.Chat;
import org.example.diadp1backend.modelos.Mensaje;
import org.example.diadp1backend.repositorios.ChatRepository;
import org.example.diadp1backend.util.TimestampFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ChatService {
    private final ChatRepository chatRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public List<ChatDTO> getActiveChatsWithUserProfiles(Integer userId) {
        List<Integer> chatIds = chatRepository.findChatsByUsuarioId(userId);
        List<Chat> chats = new ArrayList<>();
        List<ChatDTO> listaDTOs = new ArrayList<>();
        List<String> fotos = new ArrayList<>();
        for (Integer chatId : chatIds) {
            Optional<Chat> chat = chatRepository.findById(chatId);
            chat.ifPresent(chats::add);
        }
        for(Chat c : chats){
            ChatDTO dto = new ChatDTO();
            dto.setId(c.getId());

            Mensaje ultimoMensaje = chatRepository.getUltimoMensaje(c.getId());
            if (c.getNombre() == null) {
                dto.setNombre(chatRepository.findNombreUsuarioByChatId(c.getId(), userId));
                dto.setUltimoMensaje(ultimoMensaje.getContenido());

            } else {
                dto.setNombre(c.getNombre());
                dto.setUltimoMensaje(ultimoMensaje.getEmisor().getNombre() + ": " + ultimoMensaje.getContenido());
            }
            fotos = new ArrayList<>(chatRepository.findFotoByUsuarioId(c.getId(), userId));
            dto.setFoto(fotos);
            dto.setFechaUltimoMensaje(TimestampFormatter.formatTimestamp(chatRepository.getUltimoMensaje(c.getId()).getFecha()));

            dto.setTipo(c.getTipo());
            listaDTOs.add(dto);
        }

        return listaDTOs;
    }
}