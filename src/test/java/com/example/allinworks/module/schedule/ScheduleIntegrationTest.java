package com.example.allinworks.module.schedule;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//일정 통합 테스트
@SpringBootTest
@AutoConfigureMockMvc
public class ScheduleIntegrationTest {
    @Autowired
    private MockMvc mvc;

    //일정 추가 테스트
    @Test
    void addSchedule() throws Exception {
        mvc.perform(post("/schedules")
                .param("title", "hello")
                .param("memo", "it's a testSc"))
                .andExpect(status().is2xxSuccessful());
    }
}
