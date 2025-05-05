package br.insper.matricula.controller;

import br.insper.matricula.matricula.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@WebMvcTest(MatriculaController.class)
@AutoConfigureMockMvc
public class MatriculaControllerTests {

    @MockBean
    private MatriculaService matriculaService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void test_PostMatricula() throws Exception {

        CadastrarMatriculaDTO postDTO = new CadastrarMatriculaDTO("teste@gmail.com", "123", "EM_ANDAMENTO");

        SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwt = jwt().jwt(jwtBuilder -> jwtBuilder
                .claim("https://musica-insper.com/email", "teste@insper.com")
                .claim("https://musica-insper.com/roles", List.of("USER"))
        );

        RetornarMatriculaDTO getDTO = new RetornarMatriculaDTO("123", "teste@gmail.com", "123", LocalDate.now(), "EM_ANDAMENTO", null, null);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


        Mockito.when(matriculaService.salvarMatricula(Mockito.anyString(), Mockito.eq(postDTO)))
                .thenReturn(getDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/matricula").with(jwt)
                        .content(objectMapper.writeValueAsString(postDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(getDTO)));
    }

    @Test
    void test_GetMatriculaComFiltro() throws Exception {

        Matricula matricula = new Matricula();
        matricula.setId("123");
        matricula.setEmailAluno("teste@insper.com");
        matricula.setIdCurso("123");
        matricula.setDataMatricula(LocalDate.now());
        matricula.setStatus("EM_ANDAMENTO");

        SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwt = jwt().jwt(jwtBuilder -> jwtBuilder
                .claim("https://musica-insper.com/email", "teste@insper.com")
                .claim("https://musica-insper.com/roles", List.of("USER"))
        );

        List<String> roles = new ArrayList<>();
        roles.add("USER");

        List<Matricula> matriculas = Arrays.asList(matricula);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Mockito.when(matriculaService.list(matricula.getEmailAluno(), roles, matricula.getIdCurso()))
                .thenReturn(matriculas);

        mockMvc.perform(MockMvcRequestBuilders.get("/matricula/curso/" + matricula.getIdCurso()).with(jwt)
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

        SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwt = jwt().jwt(jwtBuilder -> jwtBuilder
                .claim("https://musica-insper.com/email", "teste@insper.com")
                .claim("https://musica-insper.com/roles", List.of("USER"))
        );

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Mockito.when(matriculaService.findById(matricula.getId()))
                .thenReturn(matricula);

        mockMvc.perform(MockMvcRequestBuilders.get("/matricula/"+ matricula.getId()).with(jwt)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(matricula)));
    }
}