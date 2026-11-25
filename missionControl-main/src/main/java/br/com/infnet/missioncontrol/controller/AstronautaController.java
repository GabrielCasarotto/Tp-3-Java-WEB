package br.com.infnet.missioncontrol.controller;

import br.com.infnet.missioncontrol.model.Astronauta;
import br.com.infnet.missioncontrol.model.ErrorResponse;
import br.com.infnet.missioncontrol.model.StatusAstronauta;
import br.com.infnet.missioncontrol.service.AstronautaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/astronautas")
public class AstronautaController {
    private final AstronautaService astronautaService;

    public AstronautaController(AstronautaService astronautaService) {
        this.astronautaService = astronautaService;
    }

    @GetMapping
    public List<Astronauta> listar(@RequestParam(required = false) StatusAstronauta status) {
        if(status == null) {
            return astronautaService.listarTodos();
        }
        return astronautaService.filtrarPorStatus(status);
    }

    @GetMapping("/{id}")
    public ResponseEntity buscarPeloId(@PathVariable Long id) {
        if(id <= 0) {
            return ResponseEntity.badRequest().body(new ErrorResponse("ID invalido"));
        }
        Astronauta astronauta = astronautaService.buscarPeloId(id);
        if(astronauta == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Astronauta nao encontrado"));
        }
        return ResponseEntity.ok().body(astronauta);
    }

    @PostMapping
    public ResponseEntity salvar(@RequestBody Astronauta astronauta) {
        try {
            astronautaService.adicionar(astronauta);
            return ResponseEntity.status(HttpStatus.CREATED).body(astronauta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody Astronauta astronauta) {
        if(id <= 0) {
            return ResponseEntity.badRequest().body(new ErrorResponse("ID invalido"));
        }
        Astronauta existente = astronautaService.buscarPeloId(id);
        if(existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Astronauta nao encontrado"));
        }
        try {
            astronautaService.atualizar(existente, astronauta);
            return ResponseEntity.ok().body(existente);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@PathVariable Long id) {
        if(id <= 0) {
            return ResponseEntity.badRequest().body(new ErrorResponse("ID invalido"));
        }
        boolean removeu = astronautaService.remover(id);
        if(removeu) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Astronauta nao encontrado"));
    }
}
