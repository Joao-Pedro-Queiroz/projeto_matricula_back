package br.insper.matricula.matricula;

import br.insper.matricula.curso.Curso;
import br.insper.matricula.curso.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class MatriculaService {

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private CursoService cursoService;

    public Matricula salvarMatricula(String token, CadastrarMatriculaDTO dto){
        Curso curso = cursoService.getCurso(token, dto.idCurso());

        Matricula matricula = new Matricula();
        matricula.setDataMatricula(LocalDate.now());
        matricula.setEmailAluno(dto.emailAluno());
        matricula.setIdCurso(dto.idCurso());
        matricula.setStatus(dto.status());

        matriculaRepository.save(matricula);
        return matricula;
    }

    public Matricula findById(String email, List<String> roles, String id){
        if (roles.contains("ADMIN")){
            matriculaRepository.findById(id).get();
        }
        return matriculaRepository.findByemailAlunoAndIdCurso(email, id);
    }

    public List<Matricula> list(String email, List<String> roles, String idCurso) {

        if (roles.contains("ADMIN")) {
            return matriculaRepository.findByidCurso(idCurso);
        }

        return matriculaRepository.findByemailAlunoAndidCurso(email, idCurso);
    }

    public void cancelarMatricula(String email, List<String> roles, CancelarMatriculaDTO dto) {

        List<Matricula> lista_matriculas = matriculaRepository.findByemailAlunoAndidCurso(email, dto.id());
        Matricula matricula = matriculaRepository.findById(dto.id()).get();

        if (roles.contains("ADMIN") || lista_matriculas.contains(matricula)) {
            matricula.setStatus("CANCELADO");
            matricula.setDataCancelamento(LocalDate.now());
            matricula.setMotivoCancelamento(dto.motivoCancelamento());
            matriculaRepository.save(matricula);
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }


    }

    public void delete(String id) {
        Matricula matricula = matriculaRepository.findById(id).get();
        if (!matricula.status.equals("EM_ANDAMENTO") ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }else {
            matriculaRepository.delete(matricula);
        }
    }

}
