package br.insper.matricula.curso;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Document(collection = "cursos")
public class Curso {

    @Id
    private String id;

    @NotBlank
    private String titulo;

    @NotBlank
    private String descricao;

    @Positive
    private Integer cargaHoraria;

    @NotBlank
    private String instrutor;

    @NotBlank
    private String emailCriador;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Integer getCargaHoraria() { return cargaHoraria; }
    public void setCargaHoraria(Integer cargaHoraria) { this.cargaHoraria = cargaHoraria; }
    public String getInstrutor() { return instrutor; }
    public void setInstrutor(String instrutor) { this.instrutor = instrutor; }
    public String getEmailCriador() { return emailCriador; }
    public void setEmailCriador(String emailCriador) { this.emailCriador = emailCriador;}
}