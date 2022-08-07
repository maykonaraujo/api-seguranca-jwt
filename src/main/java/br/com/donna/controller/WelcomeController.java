package br.com.donna.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {
	@GetMapping
	public String Welcome() {
		return "Bem-vindo";
	}
	
	@GetMapping("/users")
	public String users() {
		return "Autorizado acesso a users";
	}
	
	@GetMapping("/managers")
	public String managers() {
		return "Autorizado acesso a managers";
	}
}
