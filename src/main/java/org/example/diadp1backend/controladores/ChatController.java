package org.example.diadp1backend.controladores;

import lombok.AllArgsConstructor;
import org.example.diadp1backend.DTOs.ChatDTO;
import org.example.diadp1backend.DTOs.ChatWithProfilesDTO;
import org.example.diadp1backend.servicios.ChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat")
@AllArgsConstructor
public class ChatController {

    private ChatService service;

    @GetMapping("/conversaciones")
    public List<ChatWithProfilesDTO> getConversacionesByperfil(@RequestParam Integer idPerfil){
        return service.getActiveChatsWithUserProfiles(idPerfil);
    }
}
