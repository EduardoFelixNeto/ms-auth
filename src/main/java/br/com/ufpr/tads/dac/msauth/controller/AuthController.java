package br.com.ufpr.tads.dac.msauth.controller;

import br.com.ufpr.tads.dac.msauth.dto.LoginRequest;
import br.com.ufpr.tads.dac.msauth.dto.RegisterRequest;
import br.com.ufpr.tads.dac.msauth.dto.RegistroUsuarioDTO;
import br.com.ufpr.tads.dac.msauth.entity.TipoUsuario;
import br.com.ufpr.tads.dac.msauth.entity.Usuario;
import br.com.ufpr.tads.dac.msauth.feignclient.UsuarioClient;
import br.com.ufpr.tads.dac.msauth.repository.UsuarioRepository;
import br.com.ufpr.tads.dac.msauth.security.JwtService;
import br.com.ufpr.tads.dac.msauth.security.SHA256Util;
import br.com.ufpr.tads.dac.msauth.service.EmailService;
import br.com.ufpr.tads.dac.msauth.utils.SenhaUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private UsuarioRepository usuarioRepository;
    private JwtService jwtService;
    private EmailService emailService;
    private UsuarioClient usuarioClient;

    @Autowired
    public AuthController(UsuarioRepository usuarioRepository,
                          JwtService jwtService,
                          EmailService emailService,
                          UsuarioClient usuarioClient) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.usuarioClient = usuarioClient;
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
                    .build();

            usuarioRepository.save(usuario);

            emailService.enviarSenhaInicial(req.getEmail(), senhaGerada);

            RegistroUsuarioDTO usuarioDTO = new RegistroUsuarioDTO();
            usuarioDTO.setNome(req.getNome());
            usuarioDTO.setCpf(req.getCpf());
            usuarioDTO.setEmail(req.getEmail());
            usuarioDTO.setCep(req.getCep());
            usuarioDTO.setEndereco(req.getEndereco());
            usuarioDTO.setTipo(req.getTipo());
            usuarioDTO.setPontos(0);

            //Recuperar token
            String token = "Bearer " + jwtService.gerarToken(req.getEmail(), req.getTipo().name());

            usuarioClient.criar(usuarioDTO,token);

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

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/buscar-id")
    public ResponseEntity<Long> buscarIdPorEmail(@RequestParam String email) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        return usuario.map(value -> ResponseEntity.ok(value.getId()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarPorTipoEAtivo(
            @RequestParam("tipo") TipoUsuario tipo,
            @RequestParam("ativo") boolean ativo) {

        List<Usuario> usuarios = usuarioRepository.findByTipoAndAtivo(tipo, ativo);
        return ResponseEntity.ok(usuarios);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> inativarUsuario(@PathVariable Long id) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Usuario usuario = usuarioOpt.get();
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);

        Map<String, String> response = new HashMap<>();
        response.put("mensagem", "Funcionário inativado com sucesso.");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/atualizar-funcionario")
    public ResponseEntity<?> atualizarUsuario(@RequestBody RegistroUsuarioDTO dto) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(dto.getId());
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Usuario usuario = usuarioOpt.get();
        usuario.setNome(dto.getNome());
        usuario.setCpf(dto.getCpf());
        usuario.setEmail(dto.getEmail());

        usuarioRepository.save(usuario);

        return ResponseEntity.ok().build();
    }

}