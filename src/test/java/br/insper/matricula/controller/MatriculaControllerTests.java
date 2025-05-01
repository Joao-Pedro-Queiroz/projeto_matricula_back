package br.insper.matricula.controller;

import br.insper.matricula.matricula.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class MatriculaControllerTests {

    @InjectMocks
    private MatriculaController matriculaController;

    @Mock
    private MatriculaService matriculaService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(matriculaController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void test_PostMatricula() throws Exception {

        CadastrarMatriculaDTO postDTO = new CadastrarMatriculaDTO("teste@gmail.com", "123", "EM_ANDAMENTO");
        String token = "123";

        RetornarMatriculaDTO getDTO = new RetornarMatriculaDTO("123", "teste@gmail.com", "123", LocalDate.now(), "EM_ANDAMENTO", null, null);
        ObjectMapper objectMapper = new ObjectMapper();

        Mockito.when(matriculaService.salvarMatricula(token, postDTO)).thenReturn(getDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/matricula")
                        .content(objectMapper.writeValueAsString(postDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(getDTO)));
    }

    @Test
    void test_GetMatriculaComFiltro() throws Exception {

        Matricula matricula = new Matricula();
        matricula.setId("123");
        matricula.setEmailAluno("teste@gmail.com");
        matricula.setIdCurso("123");
        matricula.setDataMatricula(LocalDate.now());
        matricula.setStatus("EM_ANDAMENTO");

        String token = "123";

        List<String> roles = new ArrayList<>();
        roles.add("ADMIN");

        List<Matricula> matriculas = Arrays.asList(matricula);

        ObjectMapper objectMapper = new ObjectMapper();

        Mockito.when(matriculaService.list(matricula.getEmailAluno(), roles, matricula.getIdCurso()))
                .thenReturn(matriculas);

        mockMvc.perform(MockMvcRequestBuilders.get("/matricula/" + matricula.getIdCurso())
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(matriculas)));
    }

    @Test
    void test_GetMatricula() throws Exception {

        Matricula matricula = new Matricula();
        matricula.setId("123");
        matricula.setEmailAluno("teste@gmail.com");
        matricula.setIdCurso("123");
        matricula.setDataMatricula(LocalDate.now());
        matricula.setStatus("EM_ANDAMENTO");

        String token = "123";

        ObjectMapper objectMapper = new ObjectMapper();

        Mockito.when(matriculaService.findById(matricula.getId()))
                .thenReturn(matricula);

        mockMvc.perform(MockMvcRequestBuilders.get("/matricula/"+ matricula.getId())
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(matricula)));
    }
}