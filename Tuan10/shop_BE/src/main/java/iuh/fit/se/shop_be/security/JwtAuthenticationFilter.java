package iuh.fit.se.shop_be.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import iuh.fit.se.shop_be.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String email = null;
        String jwt = null;

        // Hỗ trợ cả "Bearer <token>" và chỉ "<token>"
        if (authHeader != null) {
            if (authHeader.startsWith("Bearer ")) {
                jwt = authHeader.substring(7);
            } else {
                jwt = authHeader.trim();
            }
            
            if (!jwt.isEmpty()) {
                try {
                    email = jwtUtil.extractEmail(jwt);
                    
                    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        if (jwtUtil.validateToken(jwt, email)) {
                            String role = jwtUtil.extractRole(jwt);
                            Long userId = jwtUtil.extractUserId(jwt);

                            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                    email,
                                    null,
                                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                            );
                            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authToken);

                            request.setAttribute("userId", userId);
                        }
                    }
                } catch (ExpiredJwtException e) {
                    logger.warn("JWT token has expired: " + e.getMessage());
                } catch (SignatureException e) {
                    logger.warn("Invalid JWT signature: " + e.getMessage());
                } catch (Exception e) {
                    logger.warn("Error parsing JWT token: " + e.getMessage());
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}

