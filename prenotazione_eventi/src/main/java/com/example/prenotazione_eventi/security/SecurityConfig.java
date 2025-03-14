package com.example.prenotazione_eventi.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.prenotazione_eventi.repositoryes.UtenteRepository;
import com.example.prenotazione_eventi.models.Utente;

import java.util.Collections;
import java.util.List;

/**
 * Configurazione di Spring Security per gestire l'autenticazione e
 * l'autorizzazione.
 */
@Configuration
@RequiredArgsConstructor

public class SecurityConfig {
    private final UtenteRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            Utente user = userRepository.findByEmail(email);
            if (user == null) {
                throw new UsernameNotFoundException("Utente non trovato con email: " + email);
            }
            // Convertiamo il ruolo enum in una stringa compatibile con Spring Security
            List<GrantedAuthority> authorities = Collections.singletonList(
                    new SimpleGrantedAuthority(user.getRuolo().name()));

            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    authorities);
        };
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disabilitiamo il CSRF (in produzione valutare se mantenerlo)
                .csrf(csrf -> csrf.disable())

                // Configuriamo l'accesso alle pagine
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/css/**", "/js/**").permitAll() // Accesso libero
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Accesso solo admin
                        .requestMatchers("/user/**").hasRole("USER") // Accesso solo utenti normali
                        .anyRequest().authenticated() // Tutte le altre richiedono autenticazione
                )

                // Configuriamo il form di login
                .formLogin(form -> form
                        .loginPage("/auth/login") // Pagina di login personalizzata
                        .loginProcessingUrl("/auth/login") // URL a cui il form invia i dati
                       /*  .successHandler((request, response, authentication) -> {
                            // Controlliamo il ruolo dell'utente per il redirect
                            String redirectUrl = authentication.getAuthorities().stream()
                                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"))
                                            ? "/admin/eventi" // Redirect alla lista eventi admin
                                            : "/user/eventi"; // Redirect alla lista eventi utente

                            response.sendRedirect(redirectUrl);
                        })*/
                        .defaultSuccessUrl("/user/eventi", true) // Redirect dopo il login
                        .permitAll())

                // Configuriamo il logout
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/login?logout")
                        .invalidateHttpSession(true) // Invalida la sessione al logout
                        .deleteCookies("JSESSIONID") // Cancella il cookie di sessione
                        .permitAll())

                // Se usi JWT, disabilita la gestione di sessioni
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configuriamo il provider di autenticazione
                .authenticationProvider(authenticationProvider());

        return http.build();
    }

}
