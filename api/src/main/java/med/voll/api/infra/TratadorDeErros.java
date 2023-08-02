package med.voll.api.infra;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // pro spring entender que é uma controller para tratar erros
public class TratadorDeErros {

    @ExceptionHandler(EntityNotFoundException.class) //pro spring identificar que onde houver está exception
    //é para chamar esse método
    public ResponseEntity tratarErro404(){
        return ResponseEntity.notFound().build();
    }

}
