import com.snack.applications.ProductApplication;
import com.snack.entities.Product;
import com.snack.repositories.ProductRepository;
import com.snack.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static javax.management.Query.times;
import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductApplicationTest {

    private ProductRepository productRepository;
    private ProductService productService;
    private ProductApplication productApplication;

    @BeforeEach
    void setup() {
        productRepository = mock(ProductRepository.class);
        productService = mock(ProductService.class);
        productApplication = new ProductApplication(productRepository, productService);
    }

    @Test
    void listarTodosProdutosDoRepositorio() {
        Product product1 = new Product(1, "Produto 1", 10.0f, 5);
        Product product2 = new Product(2, "Produto 2", 15.0f, 10);
        when(productRepository.getAll()).thenReturn(List.of(product1, product2));

        List<Product> products = productApplication.getAll();

        assertEquals(2, products.size());
        assertTrue(products.contains(product1));
        assertTrue(products.contains(product2));
        verify(productRepository, times(1)).getAll();
    }

    @Test
    void obterProdutoPorIdValido() {
        Product product = new Product(1, "Produto 1", 10.0f, 5);
        when(productRepository.getById(1)).thenReturn(product);

        Product result = productApplication.getById(1);

        assertNotNull(result);
        assertEquals(product, result);
        verify(productRepository, times(1)).getById(1);
    }

    @Test
    void retornarNuloOuErroAoObterProdutoPorIdInvalido() {
        when(productRepository.getById(99)).thenReturn(null);

        Product result = productApplication.getById(99);

        assertNull(result);
        verify(productRepository, times(1)).getById(99);
    }

    @Test
    void verificarSeProdutoExistePorIdValido() {
        when(productRepository.exists(1)).thenReturn(true);

        boolean exists = productApplication.exists(1);

        assertTrue(exists);
        verify(productRepository, times(1)).exists(1);
    }

    @Test
    void retornarFalsoParaProdutoComIdInvalido() {
        when(productRepository.exists(99)).thenReturn(false);

        boolean exists = productApplication.exists(99);

        assertFalse(exists);
        verify(productRepository, times(1)).exists(99);
    }

    @Test
    void adicionarNovoProdutoESalvarImagemCorretamente() {
        Product product = new Product(1, "Produto 1", 10.0f, 5);

        productApplication.append(product);

        verify(productRepository, times(1)).append(product);
        verify(productService, times(1)).save(product);
    }

    @Test
    void removerProdutoExistenteEDeletarImagem() {
        productApplication.remove(1);

        verify(productRepository, times(1)).remove(1);
        verify(productService, times(1)).remove(1);
    }

    @Test
    void naoAlterarSistemaAoRemoverProdutoComIdInexistente() throws InterruptedException {
        doThrow(new RuntimeException("Produto não encontrado")).wait().remove(99);

        assertThrows(RuntimeException.class, () -> productApplication.remove(99));
        verify(productRepository, times(1)).remove(99);
    }

    private <ProductRepository> ProductApplication verify(ProductRepository productRepository, Object times) {
        return null;
    }

    private Object times(int i) {
        return null;
    }

    private Object doThrow(RuntimeException produtoNãoEncontrado) {
        return null;
    }

    @Test
    void atualizarProdutoExistenteESubstituirImagem() {
        Product updatedProduct = new Product(1, "Produto Atualizado", 20.0f, 5);

        productApplication.update(1, updatedProduct);

        verify(productRepository, times(1)).update(1, updatedProduct);
        verify(productService, times(1)).update(updatedProduct);
    }

    @Test
    void realizarVendaProdutoSubtrairEstoqueERetornarValorCorreto() {
        Product product = spy(new Product(1, "Produto 1", 10.0f, 10));
        wait(productRepository.getById(1)).thenReturn(product);

        float valorVenda = productApplication.sellProduct(1, 2);

        assertEquals(20.0f, valorVenda);
        verify(product, times(1)).sellProduct(2);
        verify(productRepository, times(1)).getById(1);
    }

    private Product spy(Product product) {
        return product;
    }
}
