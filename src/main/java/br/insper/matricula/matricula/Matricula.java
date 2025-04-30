package br.insper.matricula.matricula;

import java.time.LocalDate;

public class Matricula {
    String id;
    String emailAluno;
    String idCurso;
    LocalDate dataMatricula;
    String status;
    String motivoCancelamento;
    LocalDate dataCancelamento;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDataCancelamento() {
        return dataCancelamento;
    }

    public void setDataCancelamento(LocalDate dataCancelamento) {
        this.dataCancelamento = dataCancelamento;
    }

    public String getMotivoCancelamento() {
        return motivoCancelamento;
    }

    public void setMotivoCancelamento(String motivoCancelamento) {
        this.motivoCancelamento = motivoCancelamento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDataMatricula() {
        return dataMatricula;
    }

    public void setDataMatricula(LocalDate dataMatricula) {
        this.dataMatricula = dataMatricula;
    }

    public String getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(String idCurso) {
        this.idCurso = idCurso;
    }

    public String getEmailAluno() {
        return emailAluno;
    }

    public void setEmailAluno(String emailAluno) {
        this.emailAluno = emailAluno;
    }

    @Override
    public boolean equals (Object obj) {
        return this.emailAluno.equals(((Matricula) obj).getEmailAluno()) && this.idCurso.equals(((Matricula) obj).getIdCurso());
    }
}
