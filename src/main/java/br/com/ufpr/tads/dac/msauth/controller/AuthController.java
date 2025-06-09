package br.com.ufpr.tads.dac.msauth.controller;

import br.com.ufpr.tads.dac.msauth.dto.LoginRequest;
import br.com.ufpr.tads.dac.msauth.dto.RegisterRequest;
import br.com.ufpr.tads.dac.msauth.entity.Usuario;
import br.com.ufpr.tads.dac.msauth.repository.UsuarioRepository;
import br.com.ufpr.tads.dac.msauth.security.JwtService;
import br.com.ufpr.tads.dac.msauth.security.SHA256Util;
import br.com.ufpr.tads.dac.msauth.service.EmailService;
import br.com.ufpr.tads.dac.msauth.utils.SenhaUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private UsuarioRepository usuarioRepository;
    private JwtService jwtService;
    private EmailService emailService;

    @Autowired
    public AuthController(UsuarioRepository usuarioRepository, JwtService jwtService, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody RegisterRequest req) {
        System.out.println(">>> Requisição recebida: " + req.getEmail());
        try {
            String senhaGerada = SenhaUtil.gerarSenhaNumerica(6);
            String salt = SHA256Util.gerarSalt();
            String hash = SHA256Util.hashSenha(senhaGerada, salt);

            Usuario usuario = Usuario.builder()
                    .nome(req.getNome())
                    .cpf(req.getCpf())
                    .email(req.getEmail())
                    .senha(hash + ":" + salt)
                    .tipo(req.getTipo())
                    .cep(req.getCep())
                    .endereco(req.getEndereco())
                    .pontos(0)
                    .build();

            usuarioRepository.save(usuario);

            emailService.enviarSenhaInicial(req.getEmail(), senhaGerada);

            return ResponseEntity.ok("Usuário registrado. A senha foi enviada ao e-mail.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao verificar e-mail: " + e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(req.getEmail());

        if (usuarioOpt.isEmpty()) return ResponseEntity.status(401).body("Usuário não encontrado");

        Usuario usuario = usuarioOpt.get();
        String[] partes = usuario.getSenha().split(":");

        String hashSalvo = partes[0];
        String salt = partes[1];

        String hashEntrada = SHA256Util.hashSenha(req.getSenha(), salt);

        if (!hashEntrada.equals(hashSalvo)) {
            return ResponseEntity.status(401).body("Senha inválida");
        }

        String token = jwtService.gerarToken(usuario.getEmail(), usuario.getTipo().name());

        return ResponseEntity.ok().body(token);
    }
}