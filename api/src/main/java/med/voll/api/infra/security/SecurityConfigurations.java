package med.voll.api.infra.security;

import jakarta.servlet.Filter;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private Filter securityFilter;

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//        return http.csrf().disable()//para desabilitar a proteção do ataque csrf pois o jwt já resolve esse ataque
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //desabilitar processo de autenticação statefull
//                .and().build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable()) //para desabilitar a proteção do ataque csrf pois o jwt já resolve esse ataque
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //desabilitar processo de autenticação statefull.authorizeHttpRequests(req -> {
                        .authorizeHttpRequests(req -> {
                            req.requestMatchers(HttpMethod.POST, "/login").permitAll();
                            req.anyRequest().authenticated();
                        })
                        .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                        .build();
    }
    

    @Bean// exporta uma classe para o spring fazendo com que ele consiga
    //carregá-la e realize a sua injeção de dependências em outras classes
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEnconder(){
        return new BCryptPasswordEncoder();
    }


}
