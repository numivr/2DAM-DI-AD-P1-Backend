package org.example.diadp1backend.controladores;

import org.example.diadp1backend.DTOs.MensajeDTO;
import org.example.diadp1backend.servicios.MensajeService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
