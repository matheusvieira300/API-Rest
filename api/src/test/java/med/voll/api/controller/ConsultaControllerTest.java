package med.voll.api.controller;

import med.voll.api.domain.consulta.AgendaDeConsultas;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.consulta.DadosDetalhamentoConsulta;
import med.voll.api.domain.medico.Especialidade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest // para subir o contexto completo do Spring
@AutoConfigureMockMvc
@AutoConfigureJsonTesters//para injetar o JacksonTester
class ConsultaControllerTest {

    //para simular requisições HTTP
    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosAgendamentoConsulta> dadosAgendamentoConsultaJacksonJson;

    @Autowired
    private JacksonTester<DadosDetalhamentoConsulta> dadosDetalhamentoConsultaJson;

    @MockBean// quando precisar injetar seja feito um Mock para não ser usado o banco de dados de fato
    private AgendaDeConsultas agendaDeConsultas;

    @Test
    @DisplayName("Deveria devolver código http 400 quando informações estão inválidas")
    @WithMockUser//para considerar que eu já estou logado para não dar erro 403 de user não autenticado
    void agendar_cenario_1() throws Exception {
        var response = mvc.perform(post("/consultas").content("{}").contentType(MediaType.APPLICATION_JSON))//para adicionar um JSON vazio
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());//para devolver o código 400
    }


    @Test
    @DisplayName("Deveria devolver código http 200 quando informações estão válidas")
    @WithMockUser
    void agendar_cenario_2() throws Exception {
        var data = LocalDateTime.now().plusHours(1);//para adicionar uma hora a mais;
        var especialidade = Especialidade.CARDIOLOGIA;
        var dadosDetalhamento =  new DadosDetalhamentoConsulta(null,1l,1l,data);

        //mockito para devolver os dados indepentende do que tenha any
        when(agendaDeConsultas.agendar(any())).thenReturn(dadosDetalhamento);

        var response = mvc
                .perform(
                        post("/consultas")
                                .contentType(MediaType.APPLICATION_JSON)//para levar o cabeçalho com o JSON
                                .content(dadosAgendamentoConsultaJacksonJson.write(
                                        new DadosAgendamentoConsulta(1l,1l,data,especialidade) //converte para uma String Json
                                ).getJson())
                )
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var jsonEsperado = dadosDetalhamentoConsultaJson.write(
                dadosDetalhamento
        ).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

}