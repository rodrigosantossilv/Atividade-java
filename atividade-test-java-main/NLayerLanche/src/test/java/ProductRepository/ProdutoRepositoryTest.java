import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.snack.applications.ProductApplication;
import com.snack.entities.Product;
import com.snack.services.ProductService;
import com.snack.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ProdutoRepositoryTest {

    private ProductApplication productApplication;
    private ProductService productService;
    private ProductRepository productRepository;

    @BeforeEach
    public void configurar() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(productRepository);
        productApplication = new ProductApplication(productService);
    }

    @Test
    public void deveListarTodosOsProdutosDoRepositorio() {
        Product produto1 = new Product(1, "Chips", 10, 1.5f);
        Product produto2 = new Product(2, "Refrigerante", 20, 1.0f);

        wait(productRepository.findAll()).thenReturn(Arrays.asList(produto1, produto2));

        List<Product> produtos = productApplication.getAll();

        assertNotNull(produtos);
        assertEquals(2, produtos.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void deveObterProdutoPorIdValido() {
        Product produto = new Product(1, "Chips", 10, 1.5f);
        when(productRepository.findById(1)).thenReturn(Optional.of(produto));

        Product resultado = productApplication.getById(1);

        assertNotNull(resultado);
        assertEquals("Chips", resultado.getName());
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    public void deveRetornarNuloAoObterProdutoPorIdInvalido() {
        when(productRepository.findById(999)).thenReturn(Optional.empty());

        Product resultado = productApplication.getById(999);

        assertNull(resultado);
        verify(productRepository, times(1)).findById(999);
    }

    @Test
    public void deveVerificarSeProdutoExistePorIdValido() {
        when(productRepository.existsById(1)).thenReturn(true);

        boolean existe = productApplication.exists(1);

        assertTrue(existe);
        verify(productRepository, times(1)).existsById(1);
    }

    @Test
    public void deveRetornarFalsoAoVerificarSeProdutoExistePorIdInvalido() {
        when(productRepository.existsById(999)).thenReturn(false);

        boolean existe = productApplication.exists(999);

        assertFalse(existe);
        verify(productRepository, times(1)).existsById(999);
    }

    @Test
    public void deveAdicionarNovoProdutoESalvarSuaImagem() {
        Product produto = new Product(1, "Chips", 10, 1.5f);

        productApplication.append(produto);

        verify(productRepository, times(1)).save(produto);
        // Verificações adicionais relacionadas ao salvamento da imagem podem ser adicionadas aqui.
    }

    @Test
    public void deveRemoverProdutoExistenteEDeletarSuaImagem() {
        Product produto = new Product(1, "Chips", 10, 1.5f);
        when(productRepository.existsById(1)).thenReturn(true);
        when(productRepository.findById(1)).thenReturn(Optional.of(produto));

        productApplication.remove(1);

        verify(productRepository, times(1)).deleteById(1);
        // Verificações adicionais relacionadas à exclusão da imagem podem ser adicionadas aqui.
    }

    @Test
    public void naoDeveAlterarSistemaAoRemoverProdutoInexistente() {
        when(productRepository.existsById(999)).thenReturn(false);

        productApplication.remove(999);

        verify(productRepository, never()).deleteById(999);
    }

    @Test
    public void deveAtualizarProdutoExistenteESubstituirSuaImagem() {
        Product produto = new Product(1, "Chips", 10, 1.5f);
        when(productRepository.existsById(1)).thenReturn(true);
        when(productRepository.findById(1)).thenReturn(Optional.of(produto));

        Product produtoAtualizado = new Product(1, "Chips Salgados", 15, 2.0f);
        productApplication.update(1, produtoAtualizado);

        verify(productRepository, times(1)).save(produtoAtualizado);
        // Verificações adicionais relacionadas à substituição da imagem podem ser adicionadas aqui.
    }

    @Test
    public void deveVenderProdutoESubtrairEstoqueCorretamente() {
        Product produto = new Product(1, "Chips", 10, 1.5f);
        when(productRepository.findById(1)).thenReturn(Optional.of(produto));

        float total = productApplication.sellProduct(1, 3);

        assertEquals(4.5f, total);
        assertEquals(7, produto.getStock());
        verify(productRepository, times(1)).save(produto);
    }
}
