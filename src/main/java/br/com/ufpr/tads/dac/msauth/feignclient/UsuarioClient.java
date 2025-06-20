package br.com.ufpr.tads.dac.msauth.feignclient;

import br.com.ufpr.tads.dac.msauth.dto.RegistroUsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "usuarios", url = "${usuarios.url}")
public interface UsuarioClient {

    @PostMapping("/usuarios")
    ResponseEntity<Void> criar(@RequestBody RegistroUsuarioDTO dto,
                               @RequestHeader("Authorization") String token);
}

