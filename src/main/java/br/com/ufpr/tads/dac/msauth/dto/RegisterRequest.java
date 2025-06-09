package br.com.ufpr.tads.dac.msauth.dto;

import br.com.ufpr.tads.dac.msauth.entity.TipoUsuario;

public class RegisterRequest {
    private String nome;
    private String cpf;
    private String email;
    private String senha;
    private TipoUsuario tipo;
    private String cep;
    private String endereco;
    private int pontos;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public TipoUsuario getTipo() { return tipo; }
    public void setTipo(TipoUsuario tipo) { this.tipo = tipo; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
}
