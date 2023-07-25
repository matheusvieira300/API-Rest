package med.voll.api.paciente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.endereco.DadosEndereco;
import med.voll.api.medico.Especialidade;

public record DadosCadastroPaciente (
        @NotBlank //Verifica se não é nulo e nem vazio //so funciona quando é String
        String nome,
        @NotBlank
        @Email //pra definir que é um e-mail
        String email,

        @NotBlank
        String telefone,

        @NotBlank
        @Pattern(regexp = "\\d{11}")//definindo que  tem que ter 11 dígitos
        String cpf,

        @NotNull
        @Valid // para validar os atributos de outro DTO
        DadosEndereco endereco) {
}