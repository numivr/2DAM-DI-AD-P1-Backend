// src/main/java/org/example/diadp1backend/servicios/ChatService.java
package org.example.diadp1backend.servicios;

import jakarta.servlet.http.HttpServletRequest;
import org.example.diadp1backend.DTOs.*;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;
import java.util.stream.Collectors;


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

    @Transactional
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
        for (Chat c : chats) {
            ChatDTO dto = new ChatDTO();
            dto.setId(c.getId());

            Mensaje ultimoMensaje = chatRepository.getUltimoMensaje(c.getId());


            if (Objects.equals(c.getTipo(), "privado")){
                dto.setNombre(chatRepository.findNombreUsuarioByChatId(c.getId(), userId));
                if (ultimoMensaje != null && ultimoMensaje.getEmisor().getId().equals(userId)) {
                    dto.setUltimoMensaje("Tú: " + (ultimoMensaje.getContenido() != null ? ultimoMensaje.getContenido() : ""));
                } else {
                    dto.setUltimoMensaje(ultimoMensaje != null && ultimoMensaje.getContenido() != null ? ultimoMensaje.getContenido() : "");
                }
            } else {
                dto.setNombre(c.getNombre());
                if (ultimoMensaje != null && ultimoMensaje.getEmisor().getId().equals(userId)) {
                    dto.setUltimoMensaje("Tú: " + (ultimoMensaje.getContenido() != null ? ultimoMensaje.getContenido() : ""));
                } else {
                  assert ultimoMensaje != null;
                  dto.setUltimoMensaje(ultimoMensaje.getEmisor().getNombre() + ": " + ultimoMensaje.getContenido());
                }
            }
            if (ultimoMensaje != null) {
                dto.setFechaUltimoMensaje(TimestampFormatter.formatTimestamp(ultimoMensaje.getFecha()));
            } else {
                dto.setFechaUltimoMensaje("");
            }

            fotos = new ArrayList<>(chatRepository.findFotosByUsuarioActualId(c.getId(), userId));
            dto.setFoto(fotos);
            dto.setTipo(c.getTipo());
            listaDTOs.add(dto);
        }


        return listaDTOs;
    }


    @Transactional
    public CrearChatDTO createChat(CrearChatDTO crearChatDTO) {
        Chat chat = new Chat();
        chat.setNombre(crearChatDTO.getNombre());
        chat.setTipo(crearChatDTO.getTipo());

        chatRepository.save(chat);  // Guardar el chat en la BD primero

        // Verificar que los usuarios existen y obtener sus IDs
        List<Integer> miembrosIds = crearChatDTO.getMiembros().stream()
                .map(nombre -> usuarioRepository.findTopByNombre(nombre)
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + nombre)))
                .map(Usuario::getId)
                .collect(Collectors.toList());

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


        // Insertar cada miembro uno por uno
        for (Integer miembroId : miembrosIds) {
            chatRepository.saveMiembroChat(chat.getId(), miembroId);
        }
        chatRepository.saveMiembroChat(chat.getId(), usuarioRepository.findUsuarioIdByNombre(usuarioActual.getNombre()));

        return crearChatDTO;
    }

    @Transactional
    public List<ChatDTO> salirGrupo(Integer grupoId) {
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

        chatRepository.deleteMiembroFromChat(grupoId, userId);

        return getActiveChatsWithUserProfiles();
    }

    @Transactional(readOnly = true)
    public List<ListarUsuarioDTO> listarMiembros(Integer chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat no encontrado"));
        return chat.getMiembros().stream()
                .map(usuario -> new ListarUsuarioDTO(usuario.getId(), usuario.getNombre(), chatRepository.findFotosByUsuarioActualId(chatId, usuario.getId()).get(0)))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ListarUsuarioDTO> listarAmigos() {
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

        List<Usuario> amigos = usuarioRepository.findAmigosById(userId);

        return amigos.stream()
                .map(usuario -> new ListarUsuarioDTO(usuario.getId(), usuario.getNombre(), chatRepository.findFotoByIdUsuario(usuario.getId())))
                .collect(Collectors.toList());
    }
}


