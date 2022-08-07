package br.com.donna.controller;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.donna.dto.Login;
import br.com.donna.dto.Sessao;
import br.com.donna.model.User;
import br.com.donna.repository.UserRepository;
import br.com.donna.security.JWTCreator;
import br.com.donna.security.JWTObject;
import br.com.donna.security.SecurityConfig;

@RestController
public class LoginController {
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private SecurityConfig securityConfig;
	
	@Autowired
	private UserRepository repository;
	
	@PostMapping("/login")
	public Sessao logar(@RequestBody Login login) {
		User user = repository.findByUsername(login.getUsername());
		if(user!=null) {
			boolean passwordOk = encoder.matches(login.getPassword(), user.getPassword());
			if(!passwordOk) {
				throw new RuntimeException("Senha inválida para o login: "+login.getUsername());
			}
			Sessao sessao = new Sessao();
			sessao.setLogin(user.getUsername());
			
			JWTObject jwtObject = new JWTObject();
			jwtObject.setIssuedAt(new Date(System.currentTimeMillis()));
			jwtObject.setExpiration(new Date(System.currentTimeMillis() + securityConfig.EXPIRATION));
			jwtObject.setRoles(user.getRoles());
			sessao.setToken(JWTCreator.create(securityConfig.PREFIX, securityConfig.KEY, jwtObject));
			return sessao;
		}else {
			throw new RuntimeException("Erro ao tentar fazer login");
		}
	}
	
	
}
