package med.voll.api.domain.paciente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.endereco.DadosEndereco;

public record DadosCadastroPaciente (
        @NotBlank //Verifica se não é nulo e nem vazio //so funciona quando é String
        String nome,
        @NotBlank
        @Email //pra definir que é um e-mail
        String email,

        @NotBlank
        String telefone,

        @NotBlank
        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}")//definindo que  tem que ter 11 dígitos
        String cpf,

        @NotNull
        @Valid // para validar os atributos de outro DTO
        DadosEndereco endereco) {
}