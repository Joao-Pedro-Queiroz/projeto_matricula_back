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

    public RetornarMatriculaDTO salvarMatricula(String token, CadastrarMatriculaDTO dto, String emailAluno){
        Curso curso = cursoService.getCurso(token, dto.idCurso());

        Matricula matricula = new Matricula();
        matricula.setDataMatricula(LocalDate.now());
        matricula.setEmailAluno(emailAluno);
        matricula.setIdCurso(dto.idCurso());
        matricula.setStatus(dto.status());

        matricula = matriculaRepository.save(matricula);
        return new RetornarMatriculaDTO(matricula.getId(), matricula.getEmailAluno(), matricula.getIdCurso(), matricula.getDataMatricula(), matricula.getStatus(), matricula.getMotivoCancelamento(), matricula.getDataCancelamento());
    }

    public Matricula findById(String id){
        return matriculaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<Matricula> list(String email, List<String> roles, String idCurso) {

        if (roles.contains("ADMIN")) {
            return matriculaRepository.findByidCurso(idCurso);
        }

        return matriculaRepository.findByEmailAluno(email);
    }

    public RetornarMatriculaDTO cancelarMatricula(String email, List<String> roles, CancelarMatriculaDTO dto, String id) {

        List<Matricula> lista_matriculas = matriculaRepository.findByEmailAluno(email);
        Matricula matricula = findById(id);

        if (roles.contains("ADMIN") || lista_matriculas.contains(matricula)) {
            matricula.setStatus("CANCELADO");
            matricula.setDataCancelamento(LocalDate.now());
            matricula.setMotivoCancelamento(dto.motivoCancelamento());
            matricula = matriculaRepository.save(matricula);
            return new RetornarMatriculaDTO(matricula.getId(), matricula.getEmailAluno(), matricula.getIdCurso(), matricula.getDataMatricula(), matricula.getStatus(), matricula.getMotivoCancelamento(), matricula.getDataCancelamento());
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }


    }

    public void delete(String id) {
        Matricula matricula = findById(id);
        if (!matricula.status.equals("EM_ANDAMENTO") ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }else {
            matriculaRepository.delete(matricula);
        }
    }

}
