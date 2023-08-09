package med.voll.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
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

    public String getSubject(String tokenJWT) {// método para validar o TOKEN
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer("API Voll.med")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

    private Instant dataExpiracao() {
        ZoneOffset offset = ZoneOffset.ofHours(-3); // Criando o deslocamento de -3 horas em relação ao UTC
        return LocalDateTime.now().plusHours(2).toInstant(offset);
        //para pegar a hora atual e adicionar duas horas para expiração do token
    }




}
