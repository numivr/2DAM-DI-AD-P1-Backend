package org.example.diadp1backend.servicios;

import org.example.diadp1backend.DTOs.ChatDTO;
import org.example.diadp1backend.modelos.Chat;
import org.example.diadp1backend.repositorios.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        for (Integer chatId : chatIds) {
            Optional<Chat> chat = chatRepository.findById(chatId);
            chat.ifPresent(chats::add);
        }
        for(Chat c : chats){
            ChatDTO dto = new ChatDTO();
            dto.setId(c.getId());

            if (c.getNombre() == null) {
                dto.setNombre(chatRepository.findNombreUsuarioByChatId(c.getId(), userId));
            }else {
                dto.setNombre(c.getNombre());
            }

            dto.setUltimoMensaje(chatRepository.getUltimoMensaje(c.getId()).getContenido());
            dto.setFechaUltimoMensaje(chatRepository.getUltimoMensaje(c.getId()).getFecha());

            dto.setTipo(c.getTipo());
            listaDTOs.add(dto);
        }

        return listaDTOs;
    }


}
