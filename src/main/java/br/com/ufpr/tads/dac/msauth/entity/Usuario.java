package br.com.ufpr.tads.dac.msauth.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String cpf;

    @Column(unique = true)
    private String email;

    private String senha;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public static class Builder {
        private final Usuario u = new Usuario();

        public Builder nome(String nome) { u.setNome(nome); return this; }
        public Builder cpf(String cpf) { u.setCpf(cpf); return this; }
        public Builder email(String email) { u.setEmail(email); return this; }
        public Builder senha(String senha) { u.setSenha(senha); return this; }
        public Builder tipo(TipoUsuario tipo) { u.setTipo(tipo); return this; }
        public Usuario build() { return u; }
    }

    public static Builder builder() {
        return new Builder();
    }
}
