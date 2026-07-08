package com.restaurante.config;

import com.restaurante.config.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

    @Configuration
    public class SecurityConfig {

        @Autowired
        private CustomAuthenticationSuccessHandler successHandler;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/auth/**", "/registro**", "/login**", "/css/**", "/js/**", "/imagenes/**", "/index.html").permitAll()
                    .requestMatchers("/admin.html", "/admin/**", "/dashboard.html", "/usuarios.html", "/trabajadores.html", "/gestionar-reservas.html").hasRole("ADMIN")
                    .requestMatchers("/mozo.html", "/pedidos.html", "/calificar.html", "/reservas.html", "/registrar-venta.html", "/ventas.html").hasAnyRole("MOZO", "JEFE_MOZO", "ADMIN")
                    .requestMatchers("/cliente.html", "/menu.html", "/carta.html", "/delivery.html", "/reservar.html").hasAnyRole("CLIENTE", "ADMIN")
                    .requestMatchers("/jefe.html", "/asistencia.html", "/reportes.html", "/stock.html", "/ver-mozos.html").hasAnyRole("JEFE_MOZO", "ADMIN")
                    .requestMatchers("/api/pedidos/**", "/api/ventas/**", "/api/reportes/**").hasAnyRole("ADMIN", "MOZO", "JEFE_MOZO")
                    .anyRequest().authenticated()
                )
                .formLogin(form -> form
                    .loginPage("/login.html")
                    .loginProcessingUrl("/login")
                    .successHandler(successHandler)
                    .permitAll()
                )
                .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login.html")
                    .permitAll()
                );
            
            return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }