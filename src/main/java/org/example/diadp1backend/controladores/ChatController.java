package org.example.diadp1backend.controladores;

import lombok.AllArgsConstructor;
import org.example.diadp1backend.DTOs.*;
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

    @PostMapping("/crear")
    public ResponseEntity<CrearChatDTO> createChat(@RequestBody CrearChatDTO chatDTO) {
        return ResponseEntity.ok(service.createChat(chatDTO));
    }

    @DeleteMapping("/dejarGrupo")
    public ResponseEntity<List<ChatDTO>> salirGrupo(@RequestBody ChatIdDTO chatIdDTO) {
        return ResponseEntity.ok(service.salirGrupo(chatIdDTO.getGrupoId()));
    }

    @GetMapping("/listarMiembros/{chatId}")
    public ResponseEntity<List<ListarUsuarioDTO>> listarMiembros(@PathVariable Integer chatId) {
        return ResponseEntity.ok(service.listarMiembros(chatId));
    }

    @GetMapping("/listarAmigos")
    public ResponseEntity<List<ListarUsuarioDTO>> listarAmigos() {
        return ResponseEntity.ok(service.listarAmigos());
    }





}
