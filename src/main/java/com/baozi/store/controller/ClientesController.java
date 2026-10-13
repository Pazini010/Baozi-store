package com.baozi.store.controller;

import com.baozi.store.model.Cliente;
import com.baozi.store.repository.ClienteRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClientesController {

    private final ClienteRepository clienteRepository;

    public ClientesController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // POST: cadastrar um cliente
    @PostMapping
    public ResponseEntity<Cliente> cadastrar(
            @RequestBody Cliente cliente) {

        Cliente clienteSalvo = clienteRepository.save(cliente);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clienteSalvo);
    }

    // GET: listar todos os clientes
    @GetMapping
    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    // GET: buscar um cliente pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(
            @PathVariable Long id) {

        var cliente = clienteRepository.findById(id);

        if (cliente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cliente.get());
    }

    // PUT: atualizar um cliente
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(
            @PathVariable Long id,
            @RequestBody Cliente dados) {

        var resultado = clienteRepository.findById(id);

        if (resultado.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Cliente cliente = resultado.get();
        cliente.setNome(dados.getNome());

        Cliente atualizado = clienteRepository.save(cliente);

        return ResponseEntity.ok(atualizado);
    }

    // DELETE: excluir um cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id) {

        if (!clienteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        clienteRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}