package ProductService;

import com.snack.entities.Product;
import com.snack.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServicTest {

    private ProductRepository productRepository;
    private Product validProduct;
    private Product productWithoutImage;

    @BeforeEach
    public void setup() {
        productRepository = new ProductRepository();
        validProduct = new Product(1, "Valid Product", 10.0F, "path/to/valid/image.jpg");
        productWithoutImage = new Product(2, "Product Without Image", 5.0F, null);

        // Adiciona produtos comuns à lista antes de cada teste
        productRepository.append(validProduct);
    }

    @Test
    public void testarSalvarProdutoComImagemValida() {
        assertTrue(productRepository.exists(validProduct.getId()));
        assertEquals("path/to/valid/image.jpg", validProduct.getImage());
    }

    @Test
    public void testarSalvarProdutoComImagemInexistente() {
        productRepository.append(productWithoutImage);
        assertTrue(productRepository.exists(productWithoutImage.getId()));
        assertNull(productWithoutImage.getImage());
    }

    @Test
    public void testarAtualizarProdutoExistente() {
        Product updatedProduct = new Product(1, "Updated Product", 15.0F, "new/path/to/image.jpg");
        productRepository.update(validProduct.getId(), updatedProduct);

        Product fetchedProduct = productRepository.getById(validProduct.getId());
        assertNotNull(fetchedProduct);
        assertEquals("Updated Product", fetchedProduct.getDescription());
        assertEquals(15.0, fetchedProduct.getPrice());
        assertEquals("new/path/to/image.jpg", fetchedProduct.getImage());
    }

    @Test
    public void testarRemoverProdutoExistente() {
        productRepository.remove(validProduct.getId());
        assertFalse(productRepository.exists(validProduct.getId()));
    }

    @Test
    public void testarObterCaminhoDaImagemPorId() {
        Product fetchedProduct = productRepository.getById(validProduct.getId());
        assertNotNull(fetchedProduct);
        assertEquals("path/to/valid/image.jpg", fetchedProduct.getImage());
    }
}
