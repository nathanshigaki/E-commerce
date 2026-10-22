package com.projeto.notification_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.projeto.pedido_service.event.PedidoFeitoEvent;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final JavaMailSender javaMailSender;

    @Value("${notification.email.from:contato@pulsecommerce.com}")
    private String emailFrom;

    public NotificationService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @KafkaListener(topics = "pedido-feito")
    public void listen(PedidoFeitoEvent pedidoFeitoEvent) {
        log.info("Mensagem recebida: {}", pedidoFeitoEvent);

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(emailFrom);
            messageHelper.setTo(pedidoFeitoEvent.getEmail().toString());
            messageHelper.setSubject(String.format("Seu pedido com numero %s foi recebido", pedidoFeitoEvent.getNumeroPedido()));
            messageHelper.setText(String.format("""
                Ola,

                Seu pedido com numero %s foi recebido e esta sendo processado com sucesso.

                Atenciosamente,
                Equipe PulseCommerce
                """, pedidoFeitoEvent.getNumeroPedido()));
        };
        try {
            javaMailSender.send(messagePreparator);
            log.info("Email enviado para {}", pedidoFeitoEvent.getEmail());
        } catch (MailException e) {
            log.error("Erro ao enviar email para {}: {}", pedidoFeitoEvent.getEmail(), e.getMessage());
            throw new RuntimeException("Erro ao enviar email", e);
        }
    }
}

