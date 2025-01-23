package org.example.diadp1backend.servicios;

import lombok.AllArgsConstructor;
import org.example.diadp1backend.repositorios.MensajeRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MensajeService {

    private MensajeRepository mensajeRepository;


}
