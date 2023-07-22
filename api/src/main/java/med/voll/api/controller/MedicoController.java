package med.voll.api.controller;


import jakarta.validation.Valid;
import med.voll.api.endereco.Endereco;
import med.voll.api.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired //para instancia automaticamente
    private MedicoRepository repository;

    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroMedico dados){ //request body para informar que está vindo no corpo da requisição o parametro
        //@Valid para o Spring se integrar com o Bean Validation
        repository.save(new Medico(dados));
    }

    @GetMapping
    public Page<DadosListagemMedico> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao){//pageable classe do spring para paginação
        //@PageableDefault caso não for passado nenhum parametro na URL para realizar o padrão das páginas
        return repository.findAll(paginacao).map(DadosListagemMedico::new);// metodo listar convertendo uma lista de médico
        //para DadosListagemMedico
    }

    @PutMapping // putmapping é comumente utiizado para atualização
    @Transactional //definindo que está sendo feita uma transação no banco de dados
    public void atualizar(@RequestBody @Valid DadosAtualicaoMedico dados){
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);// chamando o método para atualizar os dados baseado no DTO

    }

}
