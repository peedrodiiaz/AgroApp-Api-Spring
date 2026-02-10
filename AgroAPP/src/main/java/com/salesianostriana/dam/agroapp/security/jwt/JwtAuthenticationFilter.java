package com.salesianostriana.dam.agroapp.security.jwt;

import com.salesianostriana.dam.agroapp.model.Trabajador;
import com.salesianostriana.dam.agroapp.repository.TrabajadorRepository;
import com.salesianostriana.dam.agroapp.security.errorhandling.JwtException;
import com.salesianostriana.dam.agroapp.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final TrabajadorRepository trabajadorRepository;
  private final JwtService jwtService;

  @Autowired
  @Qualifier("handlerExceptionResolver")
  private HandlerExceptionResolver resolver;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String token = getJwtAccessTokenFromRequest(request);

    try {
      if (StringUtils.hasText(token) && jwtService.validateAccessToken(token)) {

        Long id = jwtService.getUserIdFromAccessToken(token);

        Optional<Trabajador> result = trabajadorRepository.findById(id);

        if (result.isPresent()) {
          Trabajador trabajador = result.get();
          UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
              trabajador,
              null,
              trabajador.getAuthorities());

          authenticationToken.setDetails(new WebAuthenticationDetails(request));

          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
      } else {
        filterChain.doFilter(request, response);
      }
    } catch (JwtException ex) {
      resolver.resolveException(request, response, null, ex);
    }
  }

  private String getJwtAccessTokenFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader(JwtService.TOKEN_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtService.TOKEN_PREFIX)) {
      return bearerToken.substring(JwtService.TOKEN_PREFIX.length());
    }
    return null;
  }
}
