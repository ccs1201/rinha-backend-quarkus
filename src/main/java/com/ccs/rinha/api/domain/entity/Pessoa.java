package com.ccs.rinha.api.domain.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.panache.common.Page;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Pessoa extends PanacheEntityBase {
    public Pessoa() {
    }

    public Pessoa(UUID id, String nome, String apelido, LocalDate nascimento, List<String> stack) {
        this.id = id;
        this.nome = nome;
        this.apelido = apelido;
        this.nascimento = nascimento;
        this.stack = stack;
    }

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;
    private String nome;
    @Column(unique = true)
    private String apelido;
    private LocalDate nascimento;
    @ElementCollection
    @CollectionTable(name = "stack", indexes = @Index(name = "idx_stack", columnList = "stack"))
    private List<String> stack;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public LocalDate getNascimento() {
        return nascimento;
    }

    public void setNascimento(LocalDate nascimento) {
        this.nascimento = nascimento;
    }

    public List<String> getStack() {
        return stack;
    }

    public void setStack(List<String> stack) {
        this.stack = stack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pessoa pessoa = (Pessoa) o;
        return Objects.equals(id, pessoa.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static List<Pessoa> findByTermo(String termo) {
        termo = "%".concat(termo).concat("%");
        return find("nome like ?1 or apelido like ?2 ", termo, termo)
                .page(Page.ofSize(50)).list();
    }
}
