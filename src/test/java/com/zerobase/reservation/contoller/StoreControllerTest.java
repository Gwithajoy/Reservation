package com.zerobase.reservation.contoller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.reservation.domain.Member;
import com.zerobase.reservation.enums.Role;
import com.zerobase.reservation.domain.Store;
import com.zerobase.reservation.dto.request.StoreRequest;
import com.zerobase.reservation.repository.MemberRepository;
import com.zerobase.reservation.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Long partnerId;

    @BeforeEach
    public void setup() {
        // 기존 데이터를 초기화합니다.
        storeRepository.deleteAll();
        memberRepository.deleteAll();

        // 파트너 회원 생성 (이메일: partner@exmaple.com, 이름: 홍길동, 암호: 암호화된비밀번호)
        Member partner = Member.builder()
                .email("partner@exmaple.com")
                .password("암호화된비밀번호")
                .name("홍길동")
                .role(Role.PARTNER)
                .build();
        partner = memberRepository.save(partner);
        partnerId = partner.getId();
    }

    @Test
    @WithMockUser(username = "partner@exmaple.com", roles = {"PARTNER"})
    public void registerStore_success() throws Exception {
        StoreRequest request = new StoreRequest();
        request.setStoreName("맛있는 한식당");
        request.setDescription("정갈한 한식 전문점입니다.");
        request.setLocation("서울 강남구");

        mockMvc.perform(post("/api/stores/register")
                        .param("partnerId", partnerId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                // 실제 JSON 응답은 snake_case로 직렬화되므로
                .andExpect(jsonPath("$.store_name", is("맛있는 한식당")))
                .andExpect(jsonPath("$.owner_id", is(partnerId.intValue())));
    }

    @Test
    @WithMockUser(username = "partner@exmaple.com", roles = {"PARTNER"})
    public void updateStore_success() throws Exception {
        // 기존 매장을 '예전 매장' 이름으로 등록한 후 업데이트 테스트 진행
        Store store = Store.builder()
                .storeName("예전 매장")
                .description("예전 설명")
                .location("예전 위치")
                .owner(memberRepository.findById(partnerId).orElseThrow())
                .createdAt(java.time.LocalDateTime.now())
                .build();
        store = storeRepository.save(store);

        StoreRequest updateRequest = new StoreRequest();
        updateRequest.setStoreName("업데이트 매장");
        updateRequest.setDescription("업데이트 설명");
        updateRequest.setLocation("업데이트 위치");

        mockMvc.perform(put("/api/stores/{storeId}", store.getId())
                        .param("partnerId", partnerId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                // JSON 응답의 필드명은 snake_case
                .andExpect(jsonPath("$.store_name", is("업데이트 매장")));
    }

    @Test
    @WithMockUser(username = "partner@exmaple.com", roles = {"PARTNER"})
    public void deleteStore_success() throws Exception {
        // 삭제 테스트용 매장 '삭제 매장'
        Store store = Store.builder()
                .storeName("삭제 매장")
                .description("삭제용 설명")
                .location("삭제용 위치")
                .owner(memberRepository.findById(partnerId).orElseThrow())
                .createdAt(java.time.LocalDateTime.now())
                .build();
        store = storeRepository.save(store);

        mockMvc.perform(delete("/api/stores/{storeId}", store.getId())
                        .param("partnerId", partnerId.toString()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    public void getStoreDetails_success() throws Exception {
        // 상세 정보 조회 테스트용 매장 '상세 매장'
        Store store = Store.builder()
                .storeName("상세 매장")
                .description("상세 설명")
                .location("상세 위치")
                .owner(memberRepository.findById(partnerId).orElseThrow())
                .createdAt(java.time.LocalDateTime.now())
                .build();
        store = storeRepository.save(store);

        mockMvc.perform(get("/api/stores/{storeId}", store.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.store_name", is("상세 매장")));
    }
}