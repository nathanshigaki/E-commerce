package com.projeto.notification_service.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.projeto.pedido_service.event.PedidoFeitoEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final JavaMailSender javaMailSender;

    @KafkaListener(topics = "pedido-feito")
    public void listen(PedidoFeitoEvent pedidoFeitoEvent) {
        log.info("Mensagem recebida: {}", pedidoFeitoEvent);

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("microsserviceshop@gmail.com");
            messageHelper.setTo(pedidoFeitoEvent.getEmail().toString());
            messageHelper.setSubject(String.format("Seu pedido com numero %s foi recebido", pedidoFeitoEvent.getNumeroPedido()));
            messageHelper.setText(String.format("""
                Olá, 
                
                Seu pedido com numero %s foi recebido e está sendo processado.
                
                Microserviceshop
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
