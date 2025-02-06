package org.example.diadp1backend.controladores;

import lombok.AllArgsConstructor;
import org.example.diadp1backend.DTOs.ChatDTO;
import org.example.diadp1backend.DTOs.ChatWithProfilesDTO;
import org.example.diadp1backend.servicios.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private ChatService service;

    public ChatController(ChatService service) {
        this.service = service;
    }

    @GetMapping("/conversaciones")
    public ResponseEntity<List<ChatDTO>> getChatsWithProfiles() {
        return ResponseEntity.ok(service.getActiveChatsWithUserProfiles());
    }


}
