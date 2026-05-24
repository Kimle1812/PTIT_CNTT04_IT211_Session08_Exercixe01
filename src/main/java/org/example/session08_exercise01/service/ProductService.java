package org.example.session08_exercise01.service;

import lombok.RequiredArgsConstructor;
import org.example.session08_exercise01.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public void stockIn(String sku, int quantity, String user) {
        int updated = productRepository.stockIn(sku, quantity);
        if (updated == 0) throw new RuntimeException("Không tìm thấy sản phẩm");
    }

    public void stockOut(String sku, int quantity, String user) {
        int updated = productRepository.stockOut(sku, quantity);
        if (updated == 0) throw new RuntimeException("Xuất kho vượt quá số lượng hiện có hoặc không tìm thấy sản phẩm");
    }

    public List<Map<String, Object>> inspectInventory() {
        return productRepository.findAll()
                .stream()
                .map(p -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("sku", p.getSku());
                    item.put("quantity", p.getQuantity());
                    return item;
                })
                .collect(Collectors.toList());
    }


    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}

