package ar.uba.fi.ingsoft1.alternative;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.uba.fi.ingsoft1.authorization.RequiresPrivilege;
import ar.uba.fi.ingsoft1.product.ProductDTO;
import ar.uba.fi.ingsoft1.product.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class AlternativeController {
    @Autowired
    private AlternativeService alternativeService;

    @Autowired
    private ProductService productService;

    @Operation(summary = "creates a new \"alternative\" in the system") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "alternative successfully added"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
        @ApiResponse(responseCode = "403", ref = "#/components/responses/403"),
    })
    @PostMapping("/alternative")
    @RequiresPrivilege("admin")
    public ResponseEntity<Object> createAlternative(@RequestBody AlternativeDTO alternativeDTO){
        AlternativeDTO newAlternative = alternativeService.createAlternative(alternativeDTO);
        return ResponseEntity.ok(newAlternative);
    }

    @Operation(summary = "creates a new alternative with variants") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "product successfully added"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
        @ApiResponse(responseCode = "403", ref = "#/components/responses/403"),
    })
    @PostMapping("/variants")
    @RequiresPrivilege("admin")
    public ResponseEntity<String> createProduct(@RequestBody Map<String, Object> request) {
        String alternativeName = (String) request.get("productName");
        String alternativePhoto = (String) request.get("productPhoto");
        List<Map<String, String>> variants = (List<Map<String, String>>) request.get("variants");

        alternativeService.createAlternativeWithVariants(alternativeName, alternativePhoto,variants);

        return ResponseEntity.ok("Product created successfully");
    }

    @Operation(summary = "get all alternatives in the system") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "rule successfully added"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
    })
    @GetMapping("/alternative")
    @RequiresPrivilege("user")
    public ResponseEntity<Page<AlternativeDTO>> getAlternatives(@RequestParam(defaultValue = "0") int pageNo,
    @RequestParam(defaultValue = "9") int pageSize) {
        if (pageSize < 0) {
            List<AlternativeDTO> allAlternatives = alternativeService.getAllAlternatives();
            return ResponseEntity.ok(new PageImpl<>(allAlternatives));
        }

        return ResponseEntity.ok(alternativeService.getAlternatives(pageNo, pageSize));
    }

    @Operation(summary = "get all products with given alternative") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "product list"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
    })
    @GetMapping("alternative/{id}/products")
    @RequiresPrivilege("user")
    public ResponseEntity<List<ProductDTO>>  getProductsByAlternativeId(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductsByAlternativeId(id));
    }

    @Operation(summary = "get an alternative by its id") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "alternative found"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
        @ApiResponse(responseCode = "403", ref = "#/components/responses/403"),
    })
    @GetMapping("/alternative/{id}")
    @RequiresPrivilege("admin")
    public ResponseEntity<AlternativeDTO> getAlternativeById(@PathVariable Long id) {
        AlternativeDTO alternative = alternativeService.getAlternativeById(id);
        return ResponseEntity.ok(alternative);
    }

    @Operation(summary = "delete an alternative by its id") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "alternative successfully deleted"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
        @ApiResponse(responseCode = "403", ref = "#/components/responses/403"),
    })
    @DeleteMapping("/alternative/{id}")
    @RequiresPrivilege("admin")
    public ResponseEntity<Void> deleteAlternative(@PathVariable Long id) {
        alternativeService.deleteAlternative(id);
        return ResponseEntity.noContent().build();
    }

    //endpoint: alternative/search?alternativeName={nombreDelVariante}
    @Operation(summary = "find alternatives with given name") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "alternatives list"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
    })
    @GetMapping("/alternative/search")
    @RequiresPrivilege("user")
    public ResponseEntity<List<AlternativeDTO>> getAlternativeByName(@RequestParam String alternativeName) {
        List<AlternativeDTO> alternatives = alternativeService.findAlternativesByName(alternativeName);
        return ResponseEntity.ok(alternatives);
    }
}

