package com.uevocola.com.uevocola.config;

import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.uevocola.com.uevocola.services.YourUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private YourUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@SuppressWarnings("null") @NonNull HttpServletRequest request, @SuppressWarnings("null") @NonNull HttpServletResponse response, @SuppressWarnings("null") @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        // Verifica se o cabeçalho Authorization está presente e começa com "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Remove "Bearer " do início

            try {
                if (jwtUtil.validateToken(token, jwtUtil.extractUsername(token))) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUtil.extractUsername(token));

                    // Cria um objeto de autenticação
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    // Define o contexto de segurança
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (Exception e) {
                logger.warn("Token inválido ou expirado: {}", e.getMessage());
            }
        }

        // Continua a cadeia de filtros
        filterChain.doFilter(request, response);
    }
}
