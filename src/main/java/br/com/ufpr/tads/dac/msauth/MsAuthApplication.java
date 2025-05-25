package br.com.ufpr.tads.dac.msauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@SpringBootApplication
public class MsAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsAuthApplication.class, args);
    }

    @RestControllerAdvice
    public class GlobalErrorHandler {

        @ExceptionHandler(Exception.class)
        public ResponseEntity<?> handleException(Exception e) {
            System.out.println(">>> [BACKEND] Exceção inesperada: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro interno: " + e.getMessage());
        }
    }
}
