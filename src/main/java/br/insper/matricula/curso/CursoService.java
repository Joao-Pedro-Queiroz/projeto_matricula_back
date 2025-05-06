package br.insper.matricula.curso;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;


@Service
public class CursoService {
    public Curso getCurso(String token, String id) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://54.232.22.180:8080/api/cursos/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // ou "Bearer " + token, se for o caso

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            return restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    Curso.class
            ).getBody();
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}