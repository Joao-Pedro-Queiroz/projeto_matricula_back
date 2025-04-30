package br.insper.matricula.matricula;

import java.time.LocalDate;

public record RetornarMatriculaDTO( String id, String emailAluno, String idCurso, LocalDate dataMatricula, String status, String motivoCancelamento, LocalDate dataCancelamento) {
}
