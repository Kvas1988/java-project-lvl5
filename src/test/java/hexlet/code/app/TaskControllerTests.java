package hexlet.code.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.dto.TaskDto;
import hexlet.code.app.dto.TaskStatusDto;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.security.TokenService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.transaction.Transactional;

import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("/data.sql")
public class TaskControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenService tokenService;

    @Autowired
    ObjectMapper objectMapper;

    // testGetAllTasks
    // testGetTaskPositive
    // testCreateTaskPositive
    // testCreateTaskNegativeInvalidData
    // testCreateTaskNoToken
    // testUpdateTaskPositive
    // testUpdateTaskNegativeInvalidData
    // testUpdateTaskNoToken
    // testDeleteTaskPositive
    // testDeleteTaskInvalidToken

    @Test
    void testGetAllTasks() throws Exception {
        String token = "Bearer " + tokenService.getToken(Map.of("username", "johnsmith@gmail.com"));
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/tasks")
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());
        assertThat(response.getContentAsString()).contains("init description task");
    }

    @Test
    void testGetTaskPositive() throws Exception {
        String token = "Bearer " + tokenService.getToken(Map.of("username", "johnsmith@gmail.com"));
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/tasks/51")
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());
        assertThat(response.getContentAsString()).contains("init description task");
    }

    @Test
    void testCreateTaskPositive() throws Exception {
        TaskDto taskDto = new TaskDto("test task", "test task desc", 1L, 22L, null);
        String token = "Bearer " + tokenService.getToken(Map.of("username", "johnsmith@gmail.com"));
        MockHttpServletResponse postResponse = mockMvc
                .perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto))
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, postResponse.getStatus());

        MockHttpServletResponse response = mockMvc
                .perform(get("/api/tasks")
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());
        assertThat(response.getContentAsString()).contains(taskDto.getName());
    }

    @Test
    void testCreateTaskNegativeInvalidData() throws Exception {
        // create
        TaskDto taskDto = new TaskDto("invalid", "", null, null, null);
        String token = "Bearer " + tokenService.getToken(Map.of("username", "johnsmith@gmail.com"));
        MockHttpServletResponse postResponse = mockMvc
                .perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto))
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(422, postResponse.getStatus());

        // check
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/tasks")
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());
        assertThat(response.getContentAsString()).doesNotContain(taskDto.getName());
    }

    @Test
    void testCreateTaskNoToken() throws Exception {
        // Create without token header
        TaskDto taskDto = new TaskDto("test task", "test task desc", 1L, 22L, null);
        MockHttpServletResponse postResponse = mockMvc
                .perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto))
                )
                .andReturn()
                .getResponse();

        // forbidden
        assertEquals(403, postResponse.getStatus());

        // Check new status didn't created
        String token = "Bearer " + tokenService.getToken(Map.of("username", "johnsmith@gmail.com"));
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/tasks")
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());
        assertThat(response.getContentAsString()).doesNotContain(taskDto.getName());
    }

    @Test
    void testUpdateTaskStatusPositive() throws Exception {

        // get current status
        String token = "Bearer " + tokenService.getToken(Map.of("username", "johnsmith@gmail.com"));
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/tasks/52")
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());

        // get init task name
        Task initTask = objectMapper.readValue(response.getContentAsString(), Task.class);

        // update
        TaskDto updateTaskDto = new TaskDto("test task", "test task desc", 1L, 22L, null);
        MockHttpServletRequestBuilder request = put("/api/tasks/52")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateTaskDto))
                .header(AUTHORIZATION, token);

        MockHttpServletResponse patchResponse = mockMvc
                .perform(request)
                .andReturn()
                .getResponse();

        assertEquals(200, patchResponse.getStatus());

        // check updated status
        response = mockMvc
                .perform(get("/api/tasks/52")
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertThat(response.getContentAsString()).contains(updateTaskDto.getName());
        assertThat( response.getContentAsString() ).doesNotContain(initTask.getName());
    }

    @Test
    void testUpdateTaskNegativeInvalidData() throws Exception {

        // get current status
        String token = "Bearer " + tokenService.getToken(Map.of("username", "johnsmith@gmail.com"));
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/tasks/52")
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());

        // get init task name
        Task initTask = objectMapper.readValue(response.getContentAsString(), Task.class);

        // update
        TaskDto taskDto = new TaskDto("", "", null, null, null);
        MockHttpServletRequestBuilder request = put("/api/tasks/52")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto))
                .header(AUTHORIZATION, token);

        MockHttpServletResponse patchResponse = mockMvc
                .perform(request)
                .andReturn()
                .getResponse();

        assertEquals(422, patchResponse.getStatus());

        // check updated status
        response = mockMvc
                .perform(get("/api/tasks/52")
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertThat( response.getContentAsString() ).contains(initTask.getName());
    }

    @Test
    void testUpdateTaskNoToken() throws Exception {

        // get current status
        String token = "Bearer " + tokenService.getToken(Map.of("username", "johnsmith@gmail.com"));
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/tasks/52")
                        .header(AUTHORIZATION, token))
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());

        // get init task name
        Task initTask = objectMapper.readValue(response.getContentAsString(), Task.class);

        // update
        TaskDto updateTaskDto = new TaskDto("test task", "test task desc", 1L, 22L, null);
        MockHttpServletRequestBuilder request = put("/api/tasks/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateTaskDto));

        MockHttpServletResponse patchResponse = mockMvc
                .perform(request)
                .andReturn()
                .getResponse();

        assertEquals(403, patchResponse.getStatus());

        // check status
        response = mockMvc
                .perform(get("/api/tasks/52")
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertThat(response.getContentAsString()).doesNotContain(updateTaskDto.getName());
        assertThat( response.getContentAsString() ).contains(initTask.getName());
    }

    @Test
    void testDeleteTaskPositive() throws Exception {
        // get current task
        String token = "Bearer " + tokenService.getToken(Map.of("username", "jassicasimpson@gmail.com"));
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/tasks/52")
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());

        // get init task name
        Task initStatus = objectMapper.readValue(response.getContentAsString(), Task.class);

        // delete

        MockHttpServletResponse patchResponse = mockMvc
                .perform(delete("/api/tasks/52")
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, patchResponse.getStatus());

        // check status
        response = mockMvc
                .perform(get("/api/tasks")
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertThat( response.getContentAsString() ).doesNotContain(initStatus.getName());
    }

    @Test
    void testDeleteTaskStatusInvalidToken() throws Exception {
        // get current status
        String token = "Bearer " + tokenService.getToken(Map.of("username", "johnsmith@gmail.com"));
        String invalidToken = "Bearer " + tokenService.getToken(Map.of("username", "jackdow@gmail.com"));

        MockHttpServletResponse response = mockMvc
                .perform(get("/api/tasks/52")
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());

        // get init status name
        TaskStatus initStatus = objectMapper.readValue(response.getContentAsString(), TaskStatus.class);

        // delete
        MockHttpServletResponse patchResponse = mockMvc
                .perform(delete("/api/tasks/52")
                        .header(AUTHORIZATION, invalidToken)
                )
                .andReturn()
                .getResponse();

        assertEquals(403, patchResponse.getStatus());

        // check status
        response = mockMvc
                .perform(get("/api/tasks")
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertThat( response.getContentAsString() ).contains(initStatus.getName());
    }
}
