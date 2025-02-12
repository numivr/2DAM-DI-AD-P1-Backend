package org.example.diadp1backend.servicios;

    import org.springframework.mail.javamail.JavaMailSender;
    import org.springframework.mail.javamail.MimeMessageHelper;
    import org.springframework.stereotype.Service;
    import jakarta.mail.MessagingException;
    import jakarta.mail.internet.MimeMessage;

    @Service
    public class EmailService {

        private final JavaMailSender mailSender;

        public EmailService(JavaMailSender mailSender) {
            this.mailSender = mailSender;
        }

        public void enviarCorreo(String para, String asunto, String mensaje) throws MessagingException {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);

            helper.setTo(para);
            helper.setSubject(asunto);
            helper.setText(mensaje, true); // 'true' permite HTML en el mensaje

            mailSender.send(mail);
        }
    }