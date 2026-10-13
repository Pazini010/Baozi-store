
package com.baozi.store.controller;

import com.baozi.store.model.Pedido;
import com.baozi.store.repository.PedidoRepository;
import com.baozi.store.repository.ClienteRepository;
import com.baozi.store.repository.ProdutoRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoController(
            PedidoRepository pedidoRepository,
            ClienteRepository clienteRepository,
            ProdutoRepository produtoRepository) {

        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    // POST: cadastrar pedido
    @PostMapping
    public ResponseEntity<?> cadastrar(
            @RequestBody Pedido pedido) {

        if (pedido.getCliente() == null
                || pedido.getCliente().getId() == null
                || pedido.getProduto() == null
                || pedido.getProduto().getId() == null) {

            return ResponseEntity.badRequest()
                    .body("Informe o ID do cliente e do produto.");
        }

        if (pedido.getQuantidade() == null
                || pedido.getQuantidade() <= 0) {

            return ResponseEntity.badRequest()
                    .body("A quantidade deve ser maior que zero.");
        }

        var cliente = clienteRepository.findById(
                pedido.getCliente().getId());

        if (cliente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cliente não encontrado.");
        }

        var produto = produtoRepository.findById(
                pedido.getProduto().getId());

        if (produto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Produto não encontrado.");
        }

        pedido.setCliente(cliente.get());
        pedido.setProduto(produto.get());

        Pedido salvo = pedidoRepository.save(pedido);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(salvo);
    }

    // GET: listar todos os pedidos
    @GetMapping
    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    // GET: buscar pedido pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(
            @PathVariable Long id) {

        var resultado = pedidoRepository.findById(id);

        if (resultado.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(resultado.get());
    }

    // DELETE: excluir pedido pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id) {

        if (!pedidoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        pedidoRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
