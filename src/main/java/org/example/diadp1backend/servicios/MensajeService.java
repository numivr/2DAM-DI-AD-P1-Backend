package org.example.diadp1backend.servicios;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.example.diadp1backend.DTOs.ChatDTO;
import org.example.diadp1backend.DTOs.MensajeDTO;
import org.example.diadp1backend.Security.JWTService;
import org.example.diadp1backend.Security.TokenDataDTO;
import org.example.diadp1backend.modelos.Chat;
import org.example.diadp1backend.modelos.Mensaje;
import org.example.diadp1backend.modelos.Usuario;
import org.example.diadp1backend.repositorios.ChatRepository;
import org.example.diadp1backend.repositorios.MensajeRepository;
import org.example.diadp1backend.repositorios.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MensajeService {

    private final UsuarioRepository usuarioRepository;
    private final ChatRepository chatRepository;
    private final MensajeRepository mensajeRepository;
    private final JWTService jwtService;

    MensajeService(UsuarioRepository usuarioRepository, ChatRepository chatRepository, MensajeRepository mensajeRepository, JWTService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.chatRepository = chatRepository;
        this.mensajeRepository = mensajeRepository;
        this.jwtService = jwtService;
    }

    @Transactional
    public List<MensajeDTO> getMensajesWithChatId(Integer idChat) {
        List<Mensaje> mensajes = mensajeRepository.findMensajesByChatId(idChat);
        return mensajes.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private MensajeDTO convertToDTO(Mensaje mensaje) {
        return new MensajeDTO(
                mensaje.getId(),
                mensaje.getChat().getId(),
                mensaje.getEmisor().getNombre(),
                mensaje.getContenido(),
                mensaje.getFecha(),
                mensajeRepository.getFotoUsuarioByNombreUsuario(mensaje.getEmisor().getNombre())
        );
    }

    @Transactional
    public MensajeDTO createMensaje(MensajeDTO mensajeDTO) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token JWT no presente o mal formado");
        }

        String token = authHeader.substring(7);
        TokenDataDTO tokenDataDTO = jwtService.extractTokenData(token);
        String username = tokenDataDTO.getUsername();

        System.out.println("Usuario autenticado: " + username);

        Usuario emisor = usuarioRepository.findTopByNombre(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Usuario emisor = usuarioRepository.findTopByNombre(mensajeDTO.getNombreEmisor()).orElse(null);
        Chat chat = chatRepository.findById(mensajeDTO.getIdChat()).orElse(null);
        Mensaje mensaje = new Mensaje();
        mensaje.setChat(chat);
        mensaje.setEmisor(emisor);
        mensaje.setContenido(mensajeDTO.getContenido());
        mensaje.setFecha(new Timestamp(System.currentTimeMillis()));
        mensajeRepository.save(mensaje);
        return convertToDTO(mensaje);
    }
}
