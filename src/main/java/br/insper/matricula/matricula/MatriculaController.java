package br.insper.matricula.matricula;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matricula")
public class MatriculaController {

    @Autowired
    private MatriculaService matriculaService;

    @PostMapping
    public RetornarMatriculaDTO create(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody CadastrarMatriculaDTO dto
    ) {
        String token = jwt.getTokenValue();
        return matriculaService.salvarMatricula(token, dto);
    }

    @GetMapping("/{idCurso}")
    public List<Matricula> list(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String idCurso
    ) {
        String email = jwt.getClaimAsString("https://musica-insper.com/email");
        List<String> roles = jwt.getClaimAsStringList("https://musica-insper.com/roles");
        return matriculaService.list(email, roles, idCurso);
    }

    @GetMapping("/{id}")
    public Matricula getById(
            @PathVariable String id
    ) {
        return matriculaService.findById(id);
    }


    /**
     * Cancela uma matrícula (marca status=CANCELADO).
     * Só ADMIN ou dono da matrícula podem cancelar.
     */
    @PutMapping("/cancelar/{id}")
    public RetornarMatriculaDTO cancelar(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody CancelarMatriculaDTO dto,
            @PathVariable String id
    ) {
        String email = jwt.getClaimAsString("https://musica-insper.com/email");
        List<String> roles = jwt.getClaimAsStringList("https://musica-insper.com/roles");
        return matriculaService.cancelarMatricula(email, roles, dto, id);
    }

    /**
     * Deleta uma matrícula em andamento.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        matriculaService.delete(id);
    }
}
