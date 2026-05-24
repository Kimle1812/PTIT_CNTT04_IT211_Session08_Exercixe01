package org.example.session08_exercise01.aspect;

import com.example.it211ss08hw01.entity.InventoryLog;
import com.example.it211ss08hw01.repository.InventoryLogRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDateTime;

@Aspect
@Component
public class InventoryAspect {

    @Autowired
    private InventoryLogRepository logRepository;

    // Security Check
    @Before("execution(* com.example.it211ss08hw01.service.ProductService.deleteProduct(..))")
    public void checkDeletePermission(JoinPoint jp) {
        String role = RequestContextHolder.currentRequestAttributes()
                .getAttribute("X-Role", RequestAttributes.SCOPE_REQUEST).toString();
        if (!"ADMIN".equals(role)) {
            throw new SecurityException("Chỉ ADMIN mới được phép xóa sản phẩm");
        }
    }

    // Activity Logging
    @AfterReturning(pointcut="execution(* com.example.it211ss08hw01.service.ProductService.stockIn(..)) && args(sku,quantity,username)",
            returning="result")
    public void logStockIn(String sku, int quantity, String username, Object result) {
        InventoryLog log = new InventoryLog();
        log.setTimestamp(LocalDateTime.now());
        log.setUsername(username);
        log.setAction("Stock In");
        log.setDetail("Quantity changed: " + quantity + " for SKU " + sku);
        logRepository.save(log);
    }

    @AfterReturning(pointcut="execution(* com.example.it211ss08hw01.service.ProductService.stockOut(..)) && args(sku,quantity,username)",
            returning="result")
    public void logStockOut(String sku, int quantity, String username, Object result) {
        InventoryLog log = new InventoryLog();
        log.setTimestamp(LocalDateTime.now());
        log.setUsername(username);
        log.setAction("Stock Out");
        log.setDetail("Quantity changed: -" + quantity + " for SKU " + sku);
        logRepository.save(log);
    }

    // Performance Tracking
    @Around("execution(* com.example.it211ss08hw01.service.ProductService.inspectInventory(..))")
    public Object trackPerformance(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = pjp.proceed();
        long end = System.currentTimeMillis();
        System.out.println("inspectInventory chạy trong " + (end - start) + "ms");
        return result;
    }
}

