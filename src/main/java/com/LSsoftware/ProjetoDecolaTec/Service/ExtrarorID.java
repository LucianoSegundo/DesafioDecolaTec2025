package com.LSsoftware.ProjetoDecolaTec.Service;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public final class ExtrarorID {

	public static Long extrair(JwtAuthenticationToken token) {
		long userId = Long.parseLong(token.getName());
		return userId;
	}
}
