package com.zerobase.reservation.contoller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.reservation.dto.request.MemberRegisterRequest;
import com.zerobase.reservation.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerMember_success() throws Exception {
        MemberRegisterRequest request = new MemberRegisterRequest();
        request.setEmail("newuser@example.com");
        request.setPassword("password");
        request.setName("신규 사용자");
        request.setPhone("010-9876-5432");
        request.setRole(Role.USER);

        mockMvc.perform(post("/api/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
