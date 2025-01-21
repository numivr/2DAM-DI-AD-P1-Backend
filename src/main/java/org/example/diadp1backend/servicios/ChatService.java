package org.example.diadp1backend.servicios;

import org.example.diadp1backend.DTOs.ChatWithProfilesDTO;
import org.example.diadp1backend.repositorios.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {
    private final ChatRepository chatRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public List<ChatWithProfilesDTO> getActiveChatsWithUserProfiles(Integer userId) {
        List<Object[]> results = chatRepository.findActiveChatsWithUserProfiles(userId);
        List<ChatWithProfilesDTO> chatsActivosDTOS = new ArrayList<>();

        for(Object[] r : results){
            ChatWithProfilesDTO dto = new ChatWithProfilesDTO();
            dto.setIdChat((Integer) r[0]);
            dto.setNombreChat((String) r[1]);
            dto.setTipoChat((String) r[2]);
            dto.setIdUsuario((Integer) r[3]);
            dto.setNombreUsuario((String) r[4]);
            dto.setFotoPerfil((String) r[5]);

            chatsActivosDTOS.add(dto);
        }

        return chatsActivosDTOS;
    }

}
