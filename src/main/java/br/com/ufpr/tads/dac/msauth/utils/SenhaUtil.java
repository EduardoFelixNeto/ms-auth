package br.com.ufpr.tads.dac.msauth.utils;

import java.util.Random;

public class SenhaUtil {
    public static String gerarSenhaNumerica(int tamanho) {
        StringBuilder senha = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < tamanho; i++) {
            senha.append(random.nextInt(10));
        }
        return senha.toString();
    }
}

