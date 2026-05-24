package org.example.session08_exercise01.controller;

import jakarta.validation.Valid;
import org.example.session08_exercise01.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/stock-in")
    public ResponseEntity<String> stockIn(@RequestHeader("X-User") String user,
                                          @RequestHeader("X-Role") String role,
                                          @Valid @RequestBody com.example.it211ss08hw01.entity.dto.StockRequest req) {
        productService.stockIn(req.getSku(), req.getQuantity(), user);
        return ResponseEntity.ok("Nhập kho thành công");
    }

    @PostMapping("/stock-out")
    public ResponseEntity<String> stockOut(@RequestHeader("X-User") String user,
                                           @RequestHeader("X-Role") String role,
                                           @Valid @RequestBody com.example.it211ss08hw01.entity.dto.StockRequest req) {
        productService.stockOut(req.getSku(), req.getQuantity(), user);
        return ResponseEntity.ok("Xuất kho thành công");
    }

    @GetMapping("/inspect")
    public ResponseEntity<List<Map<String, Object>>> inspect(@RequestHeader("X-User") String user,
                                                             @RequestHeader("X-Role") String role) {
        return ResponseEntity.ok(productService.inspectInventory());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@RequestHeader("X-User") String user,
                                         @RequestHeader("X-Role") String role,
                                         @PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Xóa sản phẩm thành công");
    }
}


