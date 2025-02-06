// src/main/java/org/example/diadp1backend/servicios/ChatService.java
package org.example.diadp1backend.servicios;

import jakarta.servlet.http.HttpServletRequest;
import org.example.diadp1backend.DTOs.ChatDTO;
import org.example.diadp1backend.Security.JWTService;
import org.example.diadp1backend.Security.TokenDataDTO;
import org.example.diadp1backend.modelos.Chat;
import org.example.diadp1backend.modelos.Mensaje;
import org.example.diadp1backend.modelos.Usuario;
import org.example.diadp1backend.repositorios.ChatRepository;
import org.example.diadp1backend.repositorios.UsuarioRepository;
import org.example.diadp1backend.util.TimestampFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ChatService {
    private final ChatRepository chatRepository;
  private final UsuarioRepository usuarioRepository;
  private final JWTService jwtService;
  @Autowired
    public ChatService(ChatRepository chatRepository, UsuarioRepository usuarioRepository, JWTService jwtService) {
        this.chatRepository = chatRepository;
    this.usuarioRepository = usuarioRepository;

    this.jwtService = jwtService;
  }

    public List<ChatDTO> getActiveChatsWithUserProfiles() {

      HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
      String authHeader = request.getHeader("Authorization");

      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        throw new RuntimeException("Token JWT no presente o mal formado");
      }

      String token = authHeader.substring(7);
      TokenDataDTO tokenDataDTO = jwtService.extractTokenData(token);
      String username = tokenDataDTO.getUsername();

      System.out.println("Usuario autenticado: " + username);

      Usuario usuarioActual = usuarioRepository.findTopByNombre(username)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));


      Integer userId = usuarioRepository.findUsuarioIdByNombre(usuarioActual.getNombre());


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
