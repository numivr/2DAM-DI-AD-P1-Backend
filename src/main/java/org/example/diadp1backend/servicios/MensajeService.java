package org.example.diadp1backend.servicios;

import lombok.AllArgsConstructor;
import org.example.diadp1backend.DTOs.ChatDTO;
import org.example.diadp1backend.modelos.Chat;
import org.example.diadp1backend.repositorios.MensajeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MensajeService {

    private MensajeRepository mensajeRepository;


}
