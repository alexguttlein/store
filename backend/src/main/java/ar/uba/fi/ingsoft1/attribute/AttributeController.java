package ar.uba.fi.ingsoft1.attribute;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.uba.fi.ingsoft1.authorization.RequiresPrivilege;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class AttributeController {
    @Autowired
    private AttributeService attributeService;

    @Operation(summary = "creates a new attribute in the system") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "attribute successfully added"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
        @ApiResponse(responseCode = "403", ref = "#/components/responses/403"),
    })
    @PostMapping
    @RequestMapping("/attribute")
    @RequiresPrivilege("admin")
    public ResponseEntity<Object> createAttribute(@RequestBody AttributeDTO attributeDTO){
        AttributeDTO newAttribute = attributeService.createAttribute(attributeDTO);
        return ResponseEntity.ok(newAttribute);
    }

    @Operation(summary = "get all attributes in the system") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "attribute list"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
        @ApiResponse(responseCode = "403", ref = "#/components/responses/403"),
    })
    @GetMapping("/attribute")
    @RequiresPrivilege("admin")
    public ResponseEntity<List<AttributeDTO>> getAttributes() {
        return ResponseEntity.ok(attributeService.getAttributes());
    }

    @Operation(summary = "obtain an attribute by its id") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "attribute found"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
        @ApiResponse(responseCode = "403", ref = "#/components/responses/403"),
    })
    @GetMapping("/attribute/{id}")
    @RequiresPrivilege("admin")
    public ResponseEntity<AttributeDTO> getAttributeById(@PathVariable Long id) {
        AttributeDTO attribute = attributeService.getAttributeById(id);
        return ResponseEntity.ok(attribute);
    }

    @Operation(summary = "deletes an attribute from the system") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "attribute successfully deleted"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
        @ApiResponse(responseCode = "403", ref = "#/components/responses/403"),
    })
    @DeleteMapping("/attribute/{id}")
    @RequiresPrivilege("admin")
    public ResponseEntity<Void> deleteAttribute(@PathVariable Long id) {
        attributeService.deleteAttribute(id);
        return ResponseEntity.noContent().build();
    }

    //endpoint: attribute/search?attributeName={nombreDelAtributo}
    @Operation(summary = "find attributes with given name") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "attributes found"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
        @ApiResponse(responseCode = "403", ref = "#/components/responses/403"),
    })
    @GetMapping("/attribute/search")
    @RequiresPrivilege("admin")
    public ResponseEntity<List<AttributeDTO>> getAttributesByName(@RequestParam String attributeName) {
        List<AttributeDTO> attributes = attributeService.findAttributesByName(attributeName);
        return ResponseEntity.ok(attributes);
    }
}
