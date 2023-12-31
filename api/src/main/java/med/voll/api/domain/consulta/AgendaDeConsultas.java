package med.voll.api.domain.consulta;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.validacoes.agendamento.ValidadorAgendamentoDeConsulta;
import med.voll.api.domain.consulta.validacoes.cancelamento.ValidadorCancelamentoDeConsulta;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//SOLID
//O
@Service //para definir que é uma classe de serviço
public class AgendaDeConsultas {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    //SOLID
    //L
    @Autowired // chamando a interface
    private List<ValidadorAgendamentoDeConsulta> validadores;

    @Autowired
    private List<ValidadorCancelamentoDeConsulta> validadoresCancelamento;

    public DadosDetalhamentoConsulta agendar (DadosAgendamentoConsulta dados){
        if(!pacienteRepository.existsById(dados.idPaciente())) { // se não existe um paciente com esse ID
            throw new ValidacaoException("ID do paciente informado não existe!");
        }

        if(dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico())){//verificar se o ID não é nulo
            throw new ValidacaoException("ID do médico informado não existe!");
        }

        validadores.forEach(v -> v.validar(dados));//for each para percorrer a lista

        var paciente = pacienteRepository.getReferenceById(dados.idPaciente());//passando o paciente
        var medico = escolherMedico(dados);
        if (medico ==null){
            throw new ValidacaoException("Não exite médico disponível nesta data");
        }
        var consulta = new Consulta(null,medico,paciente,dados.data(), null);// e a data através do DTO
        consultaRepository.save(consulta);
        return new DadosDetalhamentoConsulta(consulta);
    }

    private Medico escolherMedico(DadosAgendamentoConsulta dados) {
        if(dados.idMedico() != null){ //se ta vindo o ID do Médico
            return medicoRepository.getReferenceById(dados.idMedico());//carregue ele no banco de dados
        }

        if (dados.especialidade() == null){
            throw new ValidacaoException("Especialidade é Obrigatória quando médico não for escolhido!");
        }

        return medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());

    }

    public void cancelar(DadosCancelamentoConsulta dados) {
        if (!consultaRepository.existsById(dados.idConsulta())) {
            throw new ValidacaoException("Id da consulta informado não existe!");
        }

        validadoresCancelamento.forEach(v -> v.validar(dados));

        var consulta = consultaRepository.getReferenceById(dados.idConsulta());
        consulta.cancelar(dados.motivo());
    }

}