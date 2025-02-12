package org.example.diadp1backend.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class EmailController {

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/enviar-correo")
    public String enviarCorreo(@RequestBody EmailRequest request) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(request.getEmail());
            mensaje.setSubject("Confirmación de Registro");
            mensaje.setText("Hola " + request.getNombre() + ",\n\nGracias por registrarte. Por favor, confirma tu cuenta.");

            mailSender.send(mensaje);
            return "Correo enviado correctamente";
        } catch (Exception e) {
            return "Error al enviar correo: " + e.getMessage();
        }
    }

    public static class EmailRequest {
        private String email;
        private String nombre;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
    }
}
