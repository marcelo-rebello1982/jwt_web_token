package br.com.cadastroit.services.config.security;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import lombok.Builder;

@Builder
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
	

	private static final long serialVersionUID = 5733110103396996520L;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
						 AuthenticationException authException) throws IOException {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Acesso nao concedido");
	}
}
