package com.snack.repositories;

import com.snack.entities.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {
    private ProductRepository productRepository;
    private Product product1;
    private Product product2;

    @BeforeEach
    public void setup() {
        productRepository = new ProductRepository();
        product1 = new Product(1, "Hot Dog", 10.4f, "image1.png");
        product2 = new Product(2, "Burger", 15.5f, "image2.png");

        productRepository.append(product1);
        productRepository.append(product2);
    }

    @Test
    public void confirmarSeProdutoExisteNoRepositorio() {
        assertTrue(productRepository.exists(1), "O produto com ID 1 deveria existir.");
        assertFalse(productRepository.exists(3), "O produto com ID 3 não deveria existir.");
    }

    @Test
    public void testarRemocaoDeProduto() {
        productRepository.remove(1);
        assertFalse(productRepository.exists(1), "O produto com ID 1 deveria ter sido removido.");
    }

    @Test
    public void verificarAtualizacaoDeProduto() {
        Product updatedProduct = new Product(1, "Hot Dog Deluxe", 12.0f, "new_image.png");
        productRepository.update(1, updatedProduct);

        Product productInRepo = productRepository.getById(1);
        assertEquals("Hot Dog Deluxe", productInRepo.getDescription(), "A descrição do produto não foi atualizada corretamente.");
        assertEquals(12.0f, productInRepo.getPrice(), "O preço do produto não foi atualizado corretamente.");
        assertEquals("new_image.png", productInRepo.getImage(), "A imagem do produto não foi atualizada corretamente.");
    }

    @Test
    public void testarRecuperacaoDeTodosOsProdutos() {
        assertEquals(2, productRepository.getAll().size(), "Deveriam haver 2 produtos no repositório.");
    }

    @Test
    public void verificarRemocaoDeProdutoInexistente() {
        productRepository.remove(3);
        assertEquals(2, productRepository.getAll().size(), "Nenhum produto deveria ter sido removido.");
    }

    @Test
    public void testarAtualizacaoDeProdutoInexistente() {
        Product nonExistentProduct = new Product(3, "Pizza", 20.0f, "pizza.png");
        productRepository.update(3, nonExistentProduct);

        assertNull(productRepository.getById(3), "Nenhum produto deveria ter sido adicionado ou atualizado com ID 3.");
    }

    @Test
    public void verificarProdutosComIDsDuplicados() {
        Product duplicateProduct = new Product(1, "Another Hot Dog", 9.0f, "another_image.png");
        productRepository.append(duplicateProduct);

        assertEquals(2, productRepository.getAll().size(), "O repositório não deveria aceitar produtos com IDs duplicados.");
    }

    @Test
    public void confirmarRepositorioInicialVazio() {
        ProductRepository emptyRepository = new ProductRepository();
        assertTrue(emptyRepository.getAll().isEmpty(), "O repositório deveria ser inicializado vazio.");
    }
}
