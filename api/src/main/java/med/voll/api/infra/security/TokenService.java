package med.voll.api.infra.security;

import med.voll.api.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")//lendo do aplication properties
    private String secret;

    public String gerarToken(Usuario usuario){
        try {
            var algoritmo = Algorithm.HMAC256(secret);//senha secreta para gerar token
            return JWT.create()
                    .withIssuer("API Voll.med")//ferramente responsável pelo token
                    .withSubject(usuario.getLogin())//qual usuário pertence o token
                    .withExpiresAt(dataExpiracao())//momento de expiração do token
                    .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new RuntimeException("erro ao gerrar token jwt", exception);
        }
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-3:00"));
        //para pegar a hora atual e adicionar duas horas para expiração do token
    }

}
