package med.voll.api.infra.security;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//        return http.csrf().disable()//para desabilitar a proteção do ataque csrf pois o jwt já resolve esse ataque
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //desabilitar processo de autenticação statefull
//                .and().build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable()) //para desabilitar a proteção do ataque csrf pois o jwt já resolve esse ataque
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //desabilitar processo de autenticação statefull
                .build();
    }

    @Bean// exporta uma classe para o spring fazendo com que ele consiga
    //carregá-la e realize a sua injeção de dependências em outras classes
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEnconder passwordEnconder(){
        return new BCryptPasswordEncoder();
    }


}
