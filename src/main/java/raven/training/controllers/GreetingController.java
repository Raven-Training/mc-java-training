package raven.training.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import raven.training.exceptions.UserNotFoundException;

/**
 * Controlador encargado de manejar la vista de saludo (`greeting`).
 * Este endpoint se usa como parte de la tarjeta de presentación.
 */
@Controller
public class GreetingController {

    /**
     * Muestra la vista de saludo con un nombre personalizado.
     *
     * @param name  Nombre que se mostrará en el saludo. Si no se proporciona, se usará "World".
     * @param model Modelo de Spring MVC para pasar atributos a la vista.
     * @return Nombre de la vista `greeting`.
     */
    @GetMapping("/greeting")
    public String greeting(
            @RequestParam(name = "name", required = false, defaultValue = "World") String name,
            Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

}
