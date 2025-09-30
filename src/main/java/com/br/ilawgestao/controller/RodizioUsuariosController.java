package com.br.ilawgestao.controller;

import com.br.ilawgestao.domains.dto.CadastroRodizioDto;
import com.br.ilawgestao.domains.dto.RodizioUsuarioAtividadeDto;
import com.br.ilawgestao.domains.service.RodizioUsuarioAtividadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rodizio-usuario")
public class RodizioUsuariosController {

    @Autowired
    private RodizioUsuarioAtividadeService service;

    @PostMapping
    public ResponseEntity<List<RodizioUsuarioAtividadeDto>> cadastrarRodizio(@RequestBody CadastroRodizioDto rodizio) {
        List<RodizioUsuarioAtividadeDto> dtos = service.cadastrarRodizio(rodizio);
        return new ResponseEntity<List<RodizioUsuarioAtividadeDto>>(dtos, HttpStatus.OK);
    }

    @GetMapping("/{grupoAtividade}/{atividade}")
    public ResponseEntity<List<RodizioUsuarioAtividadeDto>> consultarRodizioPorGrupoAtividade(@PathVariable long grupoAtividade,
                                                                                              @PathVariable long atividade) {
        List<RodizioUsuarioAtividadeDto> dtos = service.consultarRodizioPorGrupoAtividade(grupoAtividade,atividade);
        return new ResponseEntity<List<RodizioUsuarioAtividadeDto>>(dtos, HttpStatus.OK);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity excluirUsuario(@PathVariable long codigo) {
        service.excluirUsuario(codigo);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/consultar-rodizio")
    public ResponseEntity<RodizioUsuarioAtividadeDto> consultarUsuarioRodizio(@RequestParam long grupoAtividade,
                                                                              @RequestParam long grupoTrabalho,
                                                                              @RequestParam long atividade,
                                                                              @RequestParam long usuario) {
        RodizioUsuarioAtividadeDto dto = service.consultarRodizio(grupoAtividade,grupoTrabalho,atividade,usuario);
        return new ResponseEntity<RodizioUsuarioAtividadeDto>(dto,HttpStatus.OK);
    }

    @PostMapping("/incluir-usuario-rodizio")
    public ResponseEntity<List<RodizioUsuarioAtividadeDto>> incluirUsuariosRodizio(@RequestBody CadastroRodizioDto rodizio) {
        List<RodizioUsuarioAtividadeDto> dtos = service.incluirUsuarioRodizioExistente(rodizio);
        return new ResponseEntity<List<RodizioUsuarioAtividadeDto>>(dtos, HttpStatus.OK);
    }
}
