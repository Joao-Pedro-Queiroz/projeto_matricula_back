package br.insper.matricula.service;

import br.insper.matricula.curso.Curso;
import br.insper.matricula.curso.CursoService;
import br.insper.matricula.matricula.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MatriculaServiceTests {

    @InjectMocks
    private MatriculaService matriculaService;

    @Mock
    private CursoService cursoService;

    @Mock
    private MatriculaRepository matriculaRepository;

    @Test
    void test_saveMatriculaSuccesfully() {

        Matricula matricula = new Matricula();
        matricula.setId("123");
        matricula.setEmailAluno("teste@gmail.com");
        matricula.setIdCurso("123");
        matricula.setDataMatricula(LocalDate.now());
        matricula.setStatus("EM_ANDAMENTO");

        Curso curso = new Curso();
        curso.setId("123");
        curso.setTitulo("CCOMP");
        curso.setDescricao("Teste");
        curso.setCargaHoraria(120);
        curso.setInstrutor("Teste");
        curso.setEmailCriador("teste@gmail.com");

        CadastrarMatriculaDTO postDTO = new CadastrarMatriculaDTO(matricula.getEmailAluno(), matricula.getIdCurso(), matricula.getStatus());

        Mockito.when(cursoService.getCurso("123", matricula.getIdCurso())).thenReturn(curso);

        Mockito.when(matriculaRepository.save(matricula)).thenReturn(matricula);

        RetornarMatriculaDTO getDTO = matriculaService.salvarMatricula("123", postDTO);

        Assertions.assertEquals("123", getDTO.id());
        Assertions.assertEquals("teste@gmail.com", getDTO.emailAluno());
        Assertions.assertEquals("123", getDTO.idCurso());
        Assertions.assertEquals(LocalDate.now(), getDTO.dataMatricula());
        Assertions.assertEquals("EM_ANDAMENTO", getDTO.status());
        Assertions.assertNull(getDTO.motivoCancelamento());
        Assertions.assertNull(getDTO.dataCancelamento());
    }

    @Test
    void test_saveMatriculaErrorCursoNotFound() {
        CadastrarMatriculaDTO postDTO = new CadastrarMatriculaDTO("teste@gmail.com", "123", "EM_ANDAMENTO");
        String token = "123";

        Mockito.when(cursoService.getCurso(token, postDTO.idCurso()))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> matriculaService.salvarMatricula(token, postDTO));

        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }


    @Test
    void test_findAllMatriculaByIdCursoSuccesfully() {
        String idCurso = "123";

        Mockito.when(matriculaRepository.findByidCurso(idCurso)).thenReturn(new ArrayList<>());

        List<String> roles = new ArrayList<>();
        roles.add("ADMIN");

        List<Matricula> matriculas = matriculaService.list("teste@gmail.com", roles, "123");

        Assertions.assertEquals(0, matriculas.size());
    }

    @Test
    void test_findAllMatriculaByEmailAlunoAndIdCursoSuccesfully() {
        String idCurso = "123";
        String emailAluno = "teste@gmail.com";

        Mockito.when(matriculaRepository.findByEmailAlunoAndIdCurso(emailAluno, idCurso)).thenReturn(new ArrayList<>());

        List<String> roles = new ArrayList<>();

        List<Matricula> matriculas = matriculaService.list("teste@gmail.com", roles, "123");

        Assertions.assertEquals(0, matriculas.size());
    }


    @Test
    void test_findMatriculaByIdSuccesfully() {

        Matricula matricula = new Matricula();
        matricula.setId("123");
        matricula.setEmailAluno("teste@gmail.com");
        matricula.setIdCurso("123");
        matricula.setDataMatricula(LocalDate.now());
        matricula.setStatus("EM_ANDAMENTO");

        Mockito.when(matriculaRepository.findById(matricula.getId())).thenReturn(Optional.of(matricula));

        matricula = matriculaService.findById(matricula.getId());

        Assertions.assertEquals("123", matricula.getId());
        Assertions.assertEquals("teste@gmail.com", matricula.getEmailAluno());
        Assertions.assertEquals("123", matricula.getIdCurso());
        Assertions.assertEquals(LocalDate.now(), matricula.getDataMatricula());
        Assertions.assertEquals("EM_ANDAMENTO", matricula.getStatus());
        Assertions.assertNull(matricula.getMotivoCancelamento());
        Assertions.assertNull(matricula.getDataCancelamento());
    }

    @Test
    void test_findMatriculaByIdErrorNotFound() {

        String matriculaId = "123";

        Mockito.when(matriculaRepository.findById(matriculaId)).thenReturn(Optional.empty());

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> matriculaService.findById(matriculaId));

        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void test_cancelarMatriculaSuccesfully() {
        Matricula matricula = new Matricula();
        matricula.setId("123");
        matricula.setEmailAluno("teste@gmail.com");
        matricula.setIdCurso("123");
        matricula.setDataMatricula(LocalDate.now());
        matricula.setStatus("CANCELADO");
        matricula.setMotivoCancelamento("Cancelamento");
        matricula.setDataCancelamento(LocalDate.now());

        String emailAluno = "teste@gmail.com";

        Mockito.when(matriculaRepository.findByEmailAluno(emailAluno)).thenReturn(new ArrayList<>());

        List<String> roles = new ArrayList<>();
        roles.add("ADMIN");

        String matriculaId = "123";

        Mockito.when(matriculaRepository.findById(matriculaId)).thenReturn(Optional.of(matricula));

        CancelarMatriculaDTO cancelaDTO = new CancelarMatriculaDTO(matricula.getMotivoCancelamento());

        Mockito.when(matriculaRepository.save(matricula)).thenReturn(matricula);

        RetornarMatriculaDTO getDTO = matriculaService.cancelarMatricula(emailAluno, roles, cancelaDTO, matricula.getId());

        Assertions.assertEquals("123", getDTO.id());
        Assertions.assertEquals("teste@gmail.com", getDTO.emailAluno());
        Assertions.assertEquals("123", getDTO.idCurso());
        Assertions.assertEquals(LocalDate.now(), getDTO.dataMatricula());
        Assertions.assertEquals("CANCELADO", getDTO.status());
        Assertions.assertEquals("Cancelamento", getDTO.motivoCancelamento());
        Assertions.assertEquals(LocalDate.now(), getDTO.dataCancelamento());
    }

    @Test
    void test_cancelarMatriculaErrorBadRequest() {
        Matricula matricula = new Matricula();
        matricula.setId("123");
        matricula.setEmailAluno("teste@gmail.com");
        matricula.setIdCurso("123");
        matricula.setDataMatricula(LocalDate.now());
        matricula.setStatus("CANCELADO");
        matricula.setMotivoCancelamento("Cancelamento");
        matricula.setDataCancelamento(LocalDate.now());

        String emailAluno = "teste@gmail.com";

        Mockito.when(matriculaRepository.findByEmailAluno(emailAluno)).thenReturn(new ArrayList<>());

        List<String> roles = new ArrayList<>();

        String matriculaId = "123";

        Mockito.when(matriculaRepository.findById(matriculaId)).thenReturn(Optional.of(matricula));

        CancelarMatriculaDTO cancelaDTO = new CancelarMatriculaDTO(matricula.getMotivoCancelamento());

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> matriculaService.cancelarMatricula(emailAluno, roles, cancelaDTO, matricula.getId()));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void test_deleteMatriculaSuccesfully() {

        Matricula matricula = new Matricula();
        matricula.setId("123");
        matricula.setStatus("EM_ANDAMENTO");

        Mockito.when(matriculaRepository.findById(matricula.getId())).thenReturn(Optional.of(matricula));

        matriculaService.delete(matricula.getId());

        Mockito.verify(matriculaRepository, Mockito.times(1)).delete(matricula);
    }

    @Test
    void test_deleteMatriculaErrorBadRequest() {
        Matricula matricula = new Matricula();
        matricula.setId("123");
        matricula.setStatus("CANCELADA");

        Mockito.when(matriculaRepository.findById(matricula.getId())).thenReturn(Optional.of(matricula));

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> matriculaService.delete(matricula.getId()));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }
}