package org.example.diadp1backend.servicios;

import lombok.AllArgsConstructor;
import org.example.diadp1backend.DTOs.ChatDTO;
import org.example.diadp1backend.DTOs.MensajeDTO;
import org.example.diadp1backend.modelos.Chat;
import org.example.diadp1backend.modelos.Mensaje;
import org.example.diadp1backend.modelos.Usuario;
import org.example.diadp1backend.repositorios.MensajeRepository;
import org.example.diadp1backend.repositorios.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MensajeService {

    private final UsuarioRepository usuarioRepository;
    private MensajeRepository mensajeRepository;

    @Transactional
    public List<MensajeDTO> getMensajesWithChatId(Integer idChat) {
        List<Mensaje> mensajes = mensajeRepository.findMensajesByChatId(idChat);
        return mensajes.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private MensajeDTO convertToDTO(Mensaje mensaje) {
        return new MensajeDTO(
                mensaje.getId(),
                mensaje.getChat().getId(),
                mensaje.getEmisor().getId(),
                mensaje.getEmisor().getNombre(),
                mensaje.getContenido(),
                mensaje.getFecha()
        );
    }
}
