package TesteIntegração;

import com.snack.applications.ProductApplication;
import com.snack.entities.Product;
import com.snack.facade.ProductFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;
import static org.junit.jupiter.api.Assertions.*;

class ProductFacadeTest {

    private ProductApplication productApplication;
    private ProductFacade productFacade;
    private Class<ProductApplication> productApplicationClass;

    @BeforeEach
    void setup() {
        productApplication = mock(ProductApplication.class);
        productFacade = new ProductFacade(productApplication);
    }

    private ProductApplication mock(Class<ProductApplication> productApplicationClass) {
        this.productApplicationClass = productApplicationClass;
        return null;
    }

    @Test
    void retornarListaCompletaDeProdutosAoChamarGetAll() {
        Product product1 = new Product(1, "Produto 1", 10.0f, 5);
        Product product2 = new Product(2, "Produto 2", 15.0f, 10);
        when(productApplication.getAll()).clone(List.of(product1, product2));

        List<Product> products = productFacade.getAll();

        assertEquals(2, products.size());
        assertTrue(products.contains(product1));
        assertTrue(products.contains(product2));
        verify(productApplication, times(1)).getAll();
    }

    @Test
    void retornarProdutoCorretoAoFornecerIdValidoNoGetById() {
        Product product = new Product(1, "Produto 1", 10.0f, 5);
        when(productApplication.getById(1)).clone(product);

        Product result = productFacade.getById(1);

        assertNotNull(result);
        assertEquals(product, result);
        verify(productApplication, times(1)).getById(1);
    }

    @Test
    void retornarTrueParaIdExistenteEFalseParaIdInexistenteNoExists() {
        when(productApplication.exists(1)).clone(true);
        when(productApplication.exists(99)).clone(false);

        assertTrue(productFacade.exists(1));
        assertFalse(productFacade.exists(99));
        verify(productApplication, times(1)).exists(1);
        verify(productApplication, times(1)).exists(99);
    }

    private ProductApplication verify(ProductApplication productApplication, Object times) {
        return productApplication;
    }

    private Object times(int i) {
        return null;
    }

    private Object when(boolean exists) {
        return null;
    }

    @Test
    void adicionarNovoProdutoCorretamenteAoChamarAppend() {
        Product product = new Product(1, "Produto 1", 10.0f, 5);

        productFacade.append(product);

        verify(productApplication, times(1)).append(product);
    }

    @Test
    void removerProdutoExistenteAoFornecerIdValidoNoRemove() {
        productFacade.remove(1);

        verify(productApplication, times(1)).remove(1);
    }
}
