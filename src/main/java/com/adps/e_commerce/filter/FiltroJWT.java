package com.adps.e_commerce.filter;

import com.adps.e_commerce.domain.Usuario;
import com.adps.e_commerce.repository.UsuarioRepository;
import com.adps.e_commerce.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class FiltroJWT extends OncePerRequestFilter {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JWTService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                 HttpServletResponse response,
                                 FilterChain chain)
        throws ServletException, IOException{


        String authHeader = request.getHeader("Authorization");
        String token = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
        }
        if(token != null){
            try{
                var claims = jwtService.getClaims(token);
                String email = claims.getSubject();
                String role = claims.get("role").toString();

                if(email != null && role != null){
                    Usuario usuario = usuarioRepository.findByEmail(email)
                            .orElse(null);

                    if(usuario != null){
                        var authorities = List.of(new SimpleGrantedAuthority(role));

                    var authentication = new UsernamePasswordAuthenticationToken(
                            usuario,
                            null,
                            authorities);
                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        chain.doFilter(request, response);
    }
}
