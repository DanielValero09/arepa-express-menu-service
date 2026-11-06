package co.edu.dosw.arepaexpress.menu.infrastructure.adapter.in.rest;

import co.edu.dosw.arepaexpress.menu.domain.model.Arepa;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuController {

    @GetMapping(value = "/menu", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Arepa> obtenerMenu() {
        return List.of(
                new Arepa(1, "Arepa de choclo con queso", 9000),
                new Arepa(2, "Arepa rellena de pollo", 11000),
                new Arepa(3, "Arepa de huevo", 7000)
        );
    }
}
