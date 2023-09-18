package med.voll.api.domain.medico;


import med.voll.api.domain.consulta.Consulta;
import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.paciente.DadosCadastroPaciente;
import med.voll.api.domain.paciente.Paciente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest //anotação utilizada para testar uma interface JPA repository
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)//se não colocar está anotação ele vai usar como teste o banco de dados em mémoria
    //replace.none para não alterar as configurações do banco dedados
@ActiveProfiles("test")//para ele entender e ler apenas o application-test.properties
class MedicoRepositoryTest {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired // entity manager para testes automatizados
    private TestEntityManager em;

    @Test
    @DisplayName("Deveria devolver null quando único médico cadastrado não está disponível na data")//para descrever no teste
    void escolherMedicoAleatorioLivreNaDataCenario1() {

        //given ou arrange | onde eu cadastro as informações
        var proximaSegundaAS10 = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY)) //pegue a data atual e modifique para sempre atuar na próxima segunda feira
                .atTime(10,0);
        var medico = cadastrarMedico("Dimitri","medico@email.com","1234", Especialidade.CARDIOLOGIA);
        var paciente = cadastrarPaciente("Pessoa","paciente@email.com","324512343842");
        cadastrarConsulta(medico, paciente,proximaSegundaAS10);


        //when ou act | onde eu executo a ação que quero testar
        var medicoLivre = medicoRepository.escolherMedicoAleatorioLivreNaData(Especialidade.CARDIOLOGIA, proximaSegundaAS10);

        //then ou assert | onde eu verifico se ocorreu o resultado esperado
        assertThat(medicoLivre).isNull();
    }



    @Test
    @DisplayName("Deveria devolver médico quando ele estiver disponível na data")//para descrever no teste
    void escolherMedicoAleatorioLivreNaDataCenario2() {

        //given ou arrange | onde eu cadastro as informações
        var proximaSegundaAS10 = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY)) //pegue a data atual e modifique para sempre atuar na próxima segunda feira
                .atTime(10,0);
        var medico = cadastrarMedico("Dimitri","medico@email.com","1234", Especialidade.CARDIOLOGIA);

        //when ou act | onde eu executo a ação que quero testar
        var medicoLivre = medicoRepository.escolherMedicoAleatorioLivreNaData(Especialidade.CARDIOLOGIA, proximaSegundaAS10);

        //then ou assert | onde eu verifico se ocorreu o resultado esperado
        assertThat(medicoLivre).isEqualTo(medico);
    }



    private void cadastrarConsulta(Medico medico, Paciente paciente, LocalDateTime data) {
        em.persist(new Consulta(null, medico, paciente, data,null));
    }

    private Medico cadastrarMedico(String nome, String email, String crm, Especialidade especialidade) {
        var medico = new Medico(dadosMedico(nome, email, crm, especialidade));
        em.persist(medico);
        return medico;
    }

    private Paciente cadastrarPaciente(String nome, String email, String cpf) {
        var paciente = new Paciente(dadosPaciente(nome, email, cpf));
        em.persist(paciente);
        return paciente;
    }

    private DadosCadastroMedico dadosMedico(String nome, String email, String crm, Especialidade especialidade) {
        return new DadosCadastroMedico(
                nome,
                email,
                "61999999999",
                crm,
                especialidade,
                dadosEndereco()
        );
    }

    private DadosCadastroPaciente dadosPaciente(String nome, String email, String cpf) {
        return new DadosCadastroPaciente(
                nome,
                email,
                "61999999999",
                cpf,
                dadosEndereco()
        );
    }

    private DadosEndereco dadosEndereco() {
        return new DadosEndereco(
                "rua xpto",
                "bairro",
                "00000000",
                "Brasilia",
                "DF",
                null,
                null
        );
    }


}