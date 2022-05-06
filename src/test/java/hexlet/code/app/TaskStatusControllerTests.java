package hexlet.code.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.dto.TaskStatusDto;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.security.TokenService;
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
    private TokenService tokenService;

    @Autowired
    ObjectMapper objectMapper;

    // testGetAllTaskStatuses
    // testGetTaskStatusPositive
    // testCreateTaskStatusPositive
    // testCreateTaskStatusNegativeInvalidData
    // testCreateTaskStatusNoToken
    // testUpdateTaskStatusPositive
    // testUpdateTaskStatusNegativeInvalidData
    // testUpdateTaskStatusNoToken
    // testDeleteTaskStatusPositive
    // testDeleteTaskStatusNoToken


    @Test
    void testGetAllTaskStatuses() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/statuses"))
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());
        assertThat(response.getContentAsString()).contains("new");
    }

    @Test
    void testGetTaskStatusPositive() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/statuses/54"))
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());
        assertThat(response.getContentAsString()).contains("done");
    }

    @Test
    void testCreateTaskStatusPositive() throws Exception {
        TaskStatusDto taskStatusDto = new TaskStatusDto("New Status For Tests");
        String token = "Bearer " + tokenService.getToken(Map.of("username", "johnsmith@gmail.com"));
        MockHttpServletResponse postResponse = mockMvc
                .perform(post("/api/statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskStatusDto))
                        .header(AUTHORIZATION, token))
                .andReturn()
                .getResponse();

        assertEquals(200, postResponse.getStatus());

        MockHttpServletResponse response = mockMvc
                .perform(get("/api/statuses"))
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());
        assertThat(response.getContentAsString()).contains(taskStatusDto.getName());
    }

    @Test
    void testCreateTaskStatusNegativeInvalidData() throws Exception {
        TaskStatusDto taskStatusDto = new TaskStatusDto("");
        String token = "Bearer " + tokenService.getToken(Map.of("username", "johnsmith@gmail.com"));
        MockHttpServletResponse postResponse = mockMvc
                .perform(post("/api/statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskStatusDto))
                        .header(AUTHORIZATION, token))
                .andReturn()
                .getResponse();

        assertEquals(422, postResponse.getStatus());
    }

    @Test
    void testCreateTaskStatusNoToken() throws Exception {
        // Create without token header
        TaskStatusDto taskStatusDto = new TaskStatusDto("New Status For Tests");
        MockHttpServletResponse postResponse = mockMvc
                .perform(post("/api/statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskStatusDto))
                )
                .andReturn()
                .getResponse();

        // forbidden
        assertEquals(403, postResponse.getStatus());

        // Check new status didn't created
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/statuses"))
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());
        assertThat(response.getContentAsString()).doesNotContain(taskStatusDto.getName());
    }

    @Test
    void testUpdateTaskStatusPositive() throws Exception {

        // get current status
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/statuses/52"))
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());

        // get init status name
        TaskStatus initStatus = objectMapper.readValue(response.getContentAsString(), TaskStatus.class);

        // update
        TaskStatusDto updateTaskStatusDto = new TaskStatusDto("New Status For Tests");
        String token = "Bearer " + tokenService.getToken(Map.of("username", "johnsmith@gmail.com"));
        MockHttpServletRequestBuilder request = put("/api/statuses/52")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateTaskStatusDto))
                .header(AUTHORIZATION, token);

        MockHttpServletResponse patchResponse = mockMvc
                .perform(request)
                .andReturn()
                .getResponse();

        assertEquals(200, patchResponse.getStatus());

        // check updated status
        response = mockMvc
                .perform(get("/api/statuses/52"))
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertThat(response.getContentAsString()).contains(updateTaskStatusDto.getName());
        assertThat( response.getContentAsString() ).doesNotContain(initStatus.getName());
    }

    @Test
    void testUpdateTaskStatusNegativeInvalidData() throws Exception {

        // get current status
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/statuses/52"))
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());

        // get init status name
        TaskStatus initStatus = objectMapper.readValue(response.getContentAsString(), TaskStatus.class);

        // update
        TaskStatusDto updateTaskStatusDto = new TaskStatusDto("");
        String token = "Bearer " + tokenService.getToken(Map.of("username", "johnsmith@gmail.com"));
        MockHttpServletRequestBuilder request = put("/api/statuses/52")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateTaskStatusDto))
                .header(AUTHORIZATION, token);

        MockHttpServletResponse patchResponse = mockMvc
                .perform(request)
                .andReturn()
                .getResponse();

        assertEquals(422, patchResponse.getStatus());

        // check updated status
        response = mockMvc
                .perform(get("/api/statuses/52"))
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertThat( response.getContentAsString() ).contains(initStatus.getName());
    }

    @Test
    void testUpdateTaskStatusNoToken() throws Exception {

        // get current status
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/statuses/52"))
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());

        // get init status name
        TaskStatus initStatus = objectMapper.readValue(response.getContentAsString(), TaskStatus.class);

        // update
        TaskStatusDto updateTaskStatusDto = new TaskStatusDto("New Status For Tests");
        MockHttpServletRequestBuilder request = put("/api/statuses/52")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateTaskStatusDto));

        MockHttpServletResponse patchResponse = mockMvc
                .perform(request)
                .andReturn()
                .getResponse();

        assertEquals(403, patchResponse.getStatus());

        // check status
        response = mockMvc
                .perform(get("/api/statuses/52"))
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertThat(response.getContentAsString()).doesNotContain(updateTaskStatusDto.getName());
        assertThat( response.getContentAsString() ).contains(initStatus.getName());
    }

    @Test
    void testDeleteTaskStatusPositive() throws Exception {
        // get current status
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/statuses/54"))
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());

        // get init status name
        TaskStatus initStatus = objectMapper.readValue(response.getContentAsString(), TaskStatus.class);

        // delete
        String token = "Bearer " + tokenService.getToken(Map.of("username", "johnsmith@gmail.com"));
        MockHttpServletResponse patchResponse = mockMvc
                .perform(delete("/api/statuses/54")
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, patchResponse.getStatus());

        // check status
        response = mockMvc
                .perform(get("/api/statuses"))
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertThat( response.getContentAsString() ).doesNotContain(initStatus.getName());
    }

    @Test
    void testDeleteTaskStatusNoToken() throws Exception {
        // get current status
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/statuses/52"))
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());

        // get init status name
        TaskStatus initStatus = objectMapper.readValue(response.getContentAsString(), TaskStatus.class);

        // delete
        MockHttpServletResponse patchResponse = mockMvc
                .perform(delete("/api/statuses/52"))
                .andReturn()
                .getResponse();

        assertEquals(403, patchResponse.getStatus());

        // check status
        response = mockMvc
                .perform(get("/api/statuses"))
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertThat( response.getContentAsString() ).contains(initStatus.getName());
    }
}
