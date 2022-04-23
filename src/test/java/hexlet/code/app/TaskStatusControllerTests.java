package hexlet.code.app;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    private TokenService tokenService;

    @Autowired
    ObjectMapper mapper;// = new ObjectMapper();

    // testGetAllTaskStatuses
    // testGetTaskStatusPositive
    // testCreateTaskStatusPositive
    // testCreateTaskStatusNegativeInvalidData
    // testCreateTaskStatusNoToken
    // testUpdateTaskStatusPositive
    // testUpdateTaskStatusNegativeInvalidData
    // testUpdateTaskStatusNoToken


    @Test
    void testGetAllTaskStatuses() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/statuses"))
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());
        assertThat(response.getContentAsString()).contains("new");
        // assertThat(response.getContentAsString()).contains("В работе");
    }

    @Test
    void testGetTaskStatusPositive() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/statuses"))
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());
        assertThat(response.getContentAsString()).contains("new");
    }

    // void testGetTaskStatusNegative

    @Test
    void testCreateTaskStatusPositive() throws Exception {
        String token = "Bearer " + tokenService.getToken(Map.of("username", "johnsmith@gmail.com"));
        MockHttpServletResponse postResponse = mockMvc
                .perform(post("/api/statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"New Status For Tests\"}") // TODO: make data from dto object.toJson() ???
                        .header(AUTHORIZATION, token))
                .andReturn()
                .getResponse();

        assertEquals(200, postResponse.getStatus());

        // get id from postResponse
        // TaskStatus createdTaskStatus = mapper.readValue(postResponse.getContentAsString(), TaskStatus.class);
        // Long taskStatusId = createdTaskStatus.getId();
        // TODO: check id ???

        MockHttpServletResponse response = mockMvc
                .perform(get("/api/statuses"))
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());
        assertThat(response.getContentAsString()).contains("New Status For Tests");
    }

    @Test
    void testCreateTaskStatusNegativeInvalidData() throws Exception {
        String token = "Bearer " + tokenService.getToken(Map.of("username", "johnsmith@gmail.com"));
        MockHttpServletResponse postResponse = mockMvc
                .perform(post("/api/statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"\"}")
                        .header(AUTHORIZATION, token))
                .andReturn()
                .getResponse();

        assertEquals(422, postResponse.getStatus());
    }

    @Test
    void testCreateTaskStatusNoToken() throws Exception {
        // Create without token header
        MockHttpServletResponse postResponse = mockMvc
                .perform(post("/api/statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"New Status For Tests\"}") // TODO: make data from dto object.toJson() ???
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
        assertThat(response.getContentAsString()).doesNotContain("New Status For Tests");
    }

    @Test
    void testUpdateTaskStatusPositive() throws Exception {

        // get current status
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/statuses/1"))
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());

        // get init status name
        TaskStatus initStatus = mapper.readValue(response.getContentAsString(), TaskStatus.class);

        // update
        String newName = "New Status For Tests"; // TDO!!!
        String token = "Bearer " + tokenService.getToken(Map.of("username", "johnsmith@gmail.com"));
        MockHttpServletRequestBuilder request = put("/api/statuses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"New Status For Tests\"}")
                .header(AUTHORIZATION, token);

        MockHttpServletResponse patchResponse = mockMvc
                .perform(request)
                .andReturn()
                .getResponse();

        assertEquals(200, patchResponse.getStatus());

        // check updated status
        response = mockMvc
                .perform(get("/api/statuses/1"))
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertThat(response.getContentAsString()).contains(newName);
        assertThat( response.getContentAsString() ).doesNotContain(initStatus.getName());
    }

    @Test
    void testUpdateTaskStatusNegativeInvalidData() throws Exception {

        // get current status
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/statuses/1"))
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());

        // get init status name
        TaskStatus initStatus = mapper.readValue(response.getContentAsString(), TaskStatus.class);

        // update
        String token = "Bearer " + tokenService.getToken(Map.of("username", "johnsmith@gmail.com"));
        MockHttpServletRequestBuilder request = put("/api/statuses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"\"}")
                .header(AUTHORIZATION, token);

        MockHttpServletResponse patchResponse = mockMvc
                .perform(request)
                .andReturn()
                .getResponse();

        assertEquals(422, patchResponse.getStatus());

        // check updated status
        response = mockMvc
                .perform(get("/api/statuses/1"))
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertThat( response.getContentAsString() ).contains(initStatus.getName());
    }

    @Test
    void testUpdateTaskStatusNoToken() throws Exception {

        // get current status
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/statuses/1"))
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());

        // get init status name
        TaskStatus initStatus = mapper.readValue(response.getContentAsString(), TaskStatus.class);

        // update
        String newName = "New Status For Tests"; // TDO!!!
        MockHttpServletRequestBuilder request = put("/api/statuses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"New Status For Tests\"}");

        MockHttpServletResponse patchResponse = mockMvc
                .perform(request)
                .andReturn()
                .getResponse();

        assertEquals(403, patchResponse.getStatus());

        // check status
        response = mockMvc
                .perform(get("/api/statuses/1"))
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertThat(response.getContentAsString()).doesNotContain(newName);
        assertThat( response.getContentAsString() ).contains(initStatus.getName());
    }
}
