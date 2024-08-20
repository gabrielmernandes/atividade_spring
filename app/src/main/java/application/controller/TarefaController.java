package application.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import application.model.Tarefa;
import application.repository.TarefaRepository;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {
    @Autowired
    private TarefaRepository tarefaRepo;

    @GetMapping
    public Iterable<Tarefa> list() {
        return tarefaRepo.findAll();
    }

    @PostMapping
    public Tarefa insert(@RequestBody Tarefa tarefa) {
        if (tarefa.getDescricao() == null || tarefa.getDescricao().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Descrição não pode ser vazia");
        }
        return tarefaRepo.save(tarefa);
    }

    @PutMapping("/{id}")
    public Tarefa put(
        @PathVariable long id,
        @RequestBody Tarefa novosDados) {
        if (novosDados.getDescricao() == null || novosDados.getDescricao().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Descrição não pode ser vazia");
        }

        Optional<Tarefa> resultado = tarefaRepo.findById(id);

        if (resultado.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa Não Encontrada");
        }

        Tarefa tarefa = resultado.get();
        tarefa.setDescricao(novosDados.getDescricao());
        tarefa.setConcluido(novosDados.isConcluido());

        return tarefaRepo.save(tarefa);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        if (!tarefaRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa Não Encontrada");
        }

        tarefaRepo.deleteById(id);
    }
}
