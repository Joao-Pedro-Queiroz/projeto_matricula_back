package br.insper.matricula.matricula;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatriculaRepository extends MongoRepository<Matricula, String> {
    List<Matricula> findByEmailAluno(String email);
    List<Matricula> findByidCurso(String idCurso);
}
