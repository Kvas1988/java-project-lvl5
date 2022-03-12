package hexlet.code.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.service.JWTTokenService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import javax.transaction.Transactional;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("/data.sql")
public class TaskStatusControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    JWTTokenService tokenService;

    @Autowired
    ObjectMapper mapper;// = new ObjectMapper();

    @Test
    void testGetAllTaskStatuses() throws Exception {
        String token = tokenService.getToken(Map.of("email", "johnsmith@gmail.com"));
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/statuses")
                        .header(AUTHORIZATION, token))
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());
        assertThat(response.getContentAsString()).contains("Новый");
        assertThat(response.getContentAsString()).contains("В работе");
    }

    @Test
    void testGetTaskStatusPositive() throws Exception {
        String token = tokenService.getToken(Map.of("email", "johnsmith@gmail.com"));
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/statuses/1")
                        .header(AUTHORIZATION, token))
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());
        assertThat(response.getContentAsString()).contains("Новый");
    }

    // void testGetTaskStatusNegative

    @Test
    void testCreateTaskStatusPositive() throws Exception {
        MockHttpServletResponse postResponse = mockMvc
                .perform(post("/api/statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Новый\"}"))
                .andReturn()
                .getResponse();

        assertEquals(200, postResponse.getStatus());

        // get id from postResponse
        TaskStatus createdTaskStatus = mapper.readValue(postResponse.getContentAsString(), TaskStatus.class);
        Long taskStatusId = createdTaskStatus.getId();

        MockHttpServletResponse response = mockMvc
                .perform(get("/api/statuses"))
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());
        assertThat(response.getContentAsString()).contains("Новый");
        assertThat(response.getContentAsString()).contains("В работе");
    }

    // void testCreateTaskStatusNegative

    @Test
    void testUpdateTaskStatusPositive() throws Exception {

    }
}
