package med.voll.api.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.endereco.DadosEndereco;

public record DadosCadastroMedico(
        @NotBlank //Verifica se não é nulo e nem vazio //so funciona quando é String
        String nome,
        @NotBlank
        @Email //pra definir que é um e-mail
        String email,

        @NotBlank
        String telefone,

        @NotBlank
        @Pattern(regexp = "\\d{4,6}")//definindo que  tem que ter entre 4 a 6 digitos o CRM, Expressão Regular
        String crm,
        @NotNull
        Especialidade especialidade,
        @NotNull
        @Valid // para validar os atributos de outro DTO
        DadosEndereco endereco) {
}
