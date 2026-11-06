package co.edu.dosw.arepaexpress.menu;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MenuHttpTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void devuelveExactamenteLasTresArepasEnJson() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity("/menu", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isNotNull();
        assertThat(response.getHeaders().getContentType().isCompatibleWith(MediaType.APPLICATION_JSON)).isTrue();

        JsonNode expected = objectMapper.readTree("""
                [
                  {"id":1,"nombre":"Arepa de choclo con queso","precio":9000},
                  {"id":2,"nombre":"Arepa rellena de pollo","precio":11000},
                  {"id":3,"nombre":"Arepa de huevo","precio":7000}
                ]
                """);
        assertThat(objectMapper.readTree(response.getBody())).isEqualTo(expected);
    }

    @Test
    void rechazaPostPorqueElMenuSoloSeConsulta() {
        ResponseEntity<String> response = restTemplate.postForEntity("/menu", null, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
    }
}
