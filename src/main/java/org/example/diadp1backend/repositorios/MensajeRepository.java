package org.example.diadp1backend.repositorios;

import org.example.diadp1backend.modelos.Mensajes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface MensajeRepository extends JpaRepository<Mensajes, Integer> {




}
