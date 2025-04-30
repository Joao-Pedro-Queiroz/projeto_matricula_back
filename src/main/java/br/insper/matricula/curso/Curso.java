package br.insper.matricula.curso;

public class Curso {

    private String titulo;
    private String descricao;
    private Float horas;
    private String instrutor;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Float getHoras() {
        return horas;
    }

    public void setHoras(Float horas) {
        this.horas = horas;
    }

    public String getInstrutor() {
        return instrutor;
    }

    public void setInstrutor(String instrutor) {
        this.instrutor = instrutor;
    }
}