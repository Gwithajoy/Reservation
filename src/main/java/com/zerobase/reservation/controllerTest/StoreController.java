package com.zerobase.reservation.controllerTest;

import com.zerobase.reservation.domain.Store;
import com.zerobase.reservation.dto.request.StoreRequest;
import com.zerobase.reservation.dto.response.StoreResponse;
import com.zerobase.reservation.serviceTest.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

/**
 * 매장(Store) 관련 API 엔드포인트를 제공하는 컨트롤러입니다.
 * 이 컨트롤러는 매장 등록, 수정, 삭제, 상세 조회 기능을 제공합니다.
 */
@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    /**
     * 매장 등록 API
     *
     * 파트너 회원(매장 점장)이 새로운 매장을 등록할 때 사용됩니다.
     * 매장 등록 시 매장명, 위치, 설명 등의 정보가 제공되며, 등록한 매장의 상세 정보가 반환됩니다.
     *
     * @param request: 등록할 매장 정보 (매장명, 설명, 위치 등)
     * @param partnerId: 매장을 등록하는 파트너 회원의 ID (요청 파라미터)
     * @return 등록된 매장의 상세 정보가 포함된 응답 객체
     */
    @PostMapping("/register")
    public ResponseEntity<StoreResponse> registerStore(@RequestBody @Valid StoreRequest request,
                                                       @RequestParam Long partnerId) {
        Store store = storeService.registerStore(request, partnerId);
        return ResponseEntity.ok(new StoreResponse(store));
    }

    /**
     * 매장 수정 API
     *
     * 매장을 등록한 파트너 회원이 기존 매장의 정보를 수정할 때 사용됩니다.
     * 수정할 매장의 ID와 함께 업데이트할 정보가 전달되며, 수정된 매장 정보를 반환합니다.
     *
     * @param storeId: 수정할 매장의 ID (경로 변수)
     * @param request: 수정할 매장 정보 (매장명, 설명, 위치 등)
     * @param partnerId: 매장 수정을 요청하는 파트너 회원의 ID (요청 파라미터)
     * @return 수정된 매장의 상세 정보가 포함된 응답 객체
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
     *
     * 매장을 등록한 파트너 회원이 매장을 삭제할 때 사용됩니다.
     * 삭제할 매장의 ID와 요청한 파트너 회원의 ID를 통해 삭제가 수행되며, 성공 메시지가 반환됩니다.
     *
     * @param storeId: 삭제할 매장의 ID (경로 변수)
     * @param partnerId: 매장 삭제를 요청하는 파트너 회원의 ID (요청 파라미터)
     * @return 삭제 성공 메시지
     */
    @DeleteMapping("/{storeId}")
    public ResponseEntity<?> deleteStore(@PathVariable Long storeId,
                                         @RequestParam Long partnerId) {
        storeService.deleteStore(storeId, partnerId);
        return ResponseEntity.ok("매장이 삭제되었습니다.");
    }

    /**
     * 매장 상세 조회 API
     *
     * 사용자가 특정 매장의 상세 정보를 조회할 때 호출되는 API입니다.
     * 매장 ID를 통해 매장의 상세 정보(매장명, 위치, 설명, 소유자 등)를 반환합니다.
     *
     * @param storeId: 상세 정보를 조회할 매장의 ID (경로 변수)
     * @return 매장의 상세 정보가 포함된 응답 객체
     */
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResponse> getStoreDetails(@PathVariable Long storeId) {
        Store store = storeService.getStoreDetails(storeId);
        return ResponseEntity.ok(new StoreResponse(store));
    }
}
