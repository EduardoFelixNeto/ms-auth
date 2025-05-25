package br.com.ufpr.tads.dac.msauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarSenhaInicial(String emailDestino, String senha) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailDestino);
        message.setSubject("Sua senha de acesso");
        message.setText("Olá!\n\nSua senha de acesso ao sistema é: " + senha + "\n\nPor favor, altere-a após o primeiro login.");

        mailSender.send(message);
    }
}

