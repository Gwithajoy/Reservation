package com.zerobase.reservation.controller;

import com.zerobase.reservation.domain.Store;
import com.zerobase.reservation.dto.request.StoreRequest;
import com.zerobase.reservation.dto.response.StoreResponse;
import com.zerobase.reservation.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    /**
     * 매장 등록 API
     */
    @PostMapping("/register")
    public ResponseEntity<StoreResponse> registerStore(@RequestBody @Valid StoreRequest request,
                                                       @RequestParam Long partnerId) {
        Store store = storeService.registerStore(request, partnerId);
        return ResponseEntity.ok(new StoreResponse(store));
    }

    /**
     * 매장 수정 API
     */
    @PutMapping("/{storeId}")
    public ResponseEntity<StoreResponse> updateStore(@PathVariable Long storeId,
                                                     @RequestBody @Valid StoreRequest request,
                                                     @RequestParam Long partnerId) {
        Store updatedStore = storeService.updateStore(storeId, request, partnerId);
        return ResponseEntity.ok(new StoreResponse(updatedStore));
    }

    /**
     * 매장 삭제 API
     */
    @DeleteMapping("/{storeId}")
    public ResponseEntity<?> deleteStore(@PathVariable Long storeId,
                                         @RequestParam Long partnerId) {
        storeService.deleteStore(storeId, partnerId);
        return ResponseEntity.ok("매장이 삭제되었습니다.");
    }

    /**
     * 매장 상세 조회 API
     */
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResponse> getStoreDetails(@PathVariable Long storeId) {
        Store store = storeService.getStoreDetails(storeId);
        return ResponseEntity.ok(new StoreResponse(store));
    }
}
