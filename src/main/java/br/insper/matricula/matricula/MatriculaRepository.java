package br.insper.matricula.matricula;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatriculaRepository extends MongoRepository<Matricula, String> {
    Matricula findByemailAlunoAndIdCurso(String status, String idCurso);
    List<Matricula> findByemailAlunoAndidCurso(String email, String idCurso);
    List<Matricula> findByidCurso(String idCurso);
}
