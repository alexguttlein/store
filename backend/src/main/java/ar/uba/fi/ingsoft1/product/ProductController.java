package ar.uba.fi.ingsoft1.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.uba.fi.ingsoft1.attribute.AttributeService;
import ar.uba.fi.ingsoft1.authorization.RequiresPrivilege;
import ar.uba.fi.ingsoft1.exception.EntityNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/products")  // Ruta base para Product
@RequiredArgsConstructor
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private AttributeService attributeService;

    @Operation(summary = "creates new product") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "product successfully created"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
        @ApiResponse(responseCode = "403", ref = "#/components/responses/403"),
        @ApiResponse(responseCode = "404", description = "some of the referenced components of the product don't exist")
    })
    @PostMapping
    @RequiresPrivilege("admin")
    public ResponseEntity<?> createProduct(@RequestBody ProductCreateDTO productCreateDTO) {
        try {
            ProductDTO newProduct = productService.createProduct(productCreateDTO);
            return ResponseEntity.ok(newProduct);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "get all products in the system") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "product list"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
    })
    @GetMapping
    @RequiresPrivilege("user")
    public ResponseEntity<Page<ProductDTO>> getProducts(@RequestParam(defaultValue = "0") int pageNo,
    @RequestParam(defaultValue = "9") int pageSize) {
        if (pageSize < 0) {
            List<ProductDTO> allProducts =  productService.getAllProducts();
            return ResponseEntity.ok(new PageImpl<>(allProducts));

        }

        return ResponseEntity.ok(productService.getProducts(pageNo, pageSize));
    }

    @Operation(summary = "get a product by its id") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "product found"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
        @ApiResponse(responseCode = "403", ref = "#/components/responses/403"),
        @ApiResponse(responseCode = "404", description="Product with given ID does not exist"),
    })
    @GetMapping("/{id}")
    @RequiresPrivilege("user")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        try{
            ProductDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @Operation(summary = "delete a product") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "204", description = "product successfully deleted"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
        @ApiResponse(responseCode = "403", ref = "#/components/responses/403"),
        @ApiResponse(responseCode = "404", description = "Product with given ID does not exist")
    })
    @DeleteMapping("/{id}")
    @RequiresPrivilege("admin")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try{
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "update the information of an existing product") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "product successfully created"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
        @ApiResponse(responseCode = "403", ref = "#/components/responses/403"),
        @ApiResponse(responseCode = "404", description = "some of the referenced components of the product don't exist")
    })
    @PatchMapping("/{id}")
    @RequiresPrivilege("admin")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductCreateDTO productUpdateData
    ) {
        try{
            ProductDTO updatedProduct = productService.updateProduct(id, productUpdateData);
            return ResponseEntity.ok(updatedProduct);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "get all products in stock") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "products in stock"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
    })
    @GetMapping("/in_stock")
    @RequiresPrivilege("user")
    public ResponseEntity<List<ProductDTO>> getProductsInStock() {
        return ResponseEntity.ok(productService.getProductsInStock());
    }
}
