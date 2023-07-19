package med.voll.api.controller;


@RestController
@RequestMapping("/medicos")
public class MedicoController {

    @PostMapping
    public void cadastrar(@RequestBody DadosCadastroMedico dados){ //request body para informar que está vindo no corpo da requisição o parametro
        System.out.println(dados);
    }

}
