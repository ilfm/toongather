package com.toongather.toongather.domain.webtoon.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toongather.toongather.domain.webtoon.dto.WebtoonRequest;
import com.toongather.toongather.domain.webtoon.service.WebtoonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.toongather.toongather.domain.webtoon.domain.Age.OVER15;
import static com.toongather.toongather.domain.webtoon.domain.Platform.DAUM;
import static com.toongather.toongather.domain.webtoon.domain.WebtoonStatus.END;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WebtoonController.class)
class WebtoonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WebtoonService webtoonService;

   @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new WebtoonController(webtoonService))
                .build();
    }

    @DisplayName("웹툰 상세 조회에 성공한다")
    @Test
    void readWebtoon() throws Exception {
        // given
        Long toonId = 1L;

        // when & then
        mockMvc.perform(get("/webtoon/{toonId}", toonId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(webtoonService, times(1)).readWebtoon(toonId);
    }

    @DisplayName("웹툰 등록에 성공한다.")
    @Test
    void createWebtoon() throws Exception {
        // given
        WebtoonRequest request = WebtoonRequest.builder()
                .title("테스트 웹툰")
                .platform(DAUM)
                .age(OVER15)
                .status(END)
                .build();

        // when & then
        mockMvc.perform(post("/webtoon/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(webtoonService, times(1)).createWebtoon(any(WebtoonRequest.class));
    }

    @DisplayName("웹툰 수정에 성공한다.")
    @Test
    void updateWebtoon() throws Exception {
        // given
        Long toonId = 1L;
        WebtoonRequest request = WebtoonRequest.builder()
                .title("수정된 웹툰")
                .platform(DAUM)
                .age(OVER15)
                .status(END)
                .build();

        // when & then
        mockMvc.perform(put("/webtoon/{toonId}", toonId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(webtoonService, times(1)).updateWebtoon(eq(toonId), any(WebtoonRequest.class));
    }

    @DisplayName("웹툰 삭제에 성공한다.")
    @Test
    void deleteWebtoon() throws Exception {
        // given
        Long toonId = 1L;

        // when & then
        mockMvc.perform(delete("/webtoon/{toonId}", toonId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(webtoonService, times(1)).deleteWebtoon(toonId);
    }
}