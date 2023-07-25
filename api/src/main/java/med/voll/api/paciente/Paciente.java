package med.voll.api.paciente;

import lombok.Getter;
import lombok.Setter;
import med.voll.api.endereco.Endereco;
import med.voll.api.medico.DadosCadastroMedico;


@Getter
@Setter
public class Paciente {

    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    private Endereco endereco;

    public void excluir() {
    }
}
