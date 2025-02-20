package org.example.diadp1backend.controladores;

import org.example.diadp1backend.DTOs.MensajeDTO;
import org.example.diadp1backend.servicios.MensajeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/mensajes")
@RestController
public class MensajeController {

    private final MensajeService service;

    public MensajeController(MensajeService service) {
        this.service = service;
    }

    @GetMapping("/conversacion/{id}")
    public ResponseEntity<List<MensajeDTO>> getMensajesWithChatId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getMensajesWithChatId(id));
    }

    @PostMapping("/conversacion/{id}/enviar")
    public ResponseEntity<MensajeDTO> enviarMensaje(@RequestBody MensajeDTO mensajeDTO) {
        return ResponseEntity.ok(service.createMensaje(mensajeDTO));
    }

}
