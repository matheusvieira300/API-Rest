package med.voll.api.controller;


import jakarta.validation.Valid;
import med.voll.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("medicos")
public class MedicoController {

    @Autowired //para instanciar automaticamente
    private MedicoRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder){
        //UriComponentsBuilder classe do spring que sabe gerar uma Uri
        //request body para informar que está vindo no corpo da requisição o parametro
        //@Valid para o Spring se integrar com o Bean Validation
        var medico = new Medico(dados);
        repository.save(medico);

        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();//uma forma de utilizar pathvariable

        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
    }//implementado o código 201



    @GetMapping
    public ResponseEntity <Page<DadosListagemMedico>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao){//pageable classe do spring para paginação
        //@PageableDefault caso não for passado nenhum parametro na URL para realizar o padrão das páginas
        var page =  repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);// metodo listar convertendo uma lista de médico
        //para DadosListagemMedico

        return ResponseEntity.ok(page);//código 200 que foi efetuado com sucesso
    }



    @PutMapping // putmapping é comumente utiizado para atualização
    @Transactional //definindo que está sendo feita uma transação no banco de dados
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualicaoMedico dados){
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);// chamando o método para atualizar os dados baseado no DTO

        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }



    @DeleteMapping("/{id}")//parâmetro dinâmico para o spring reconhecer {}
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id){//@PathVariable para dizer que é uma váriavel da url acima
        var medico = repository.getReferenceById(id);
        medico.excluir();

        return ResponseEntity.noContent().build(); // para devolver o status 204 sem conteúdo
    }



    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id){
        var medico = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

}
