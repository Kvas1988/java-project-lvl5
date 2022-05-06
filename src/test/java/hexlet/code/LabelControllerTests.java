package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.security.TokenService;
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
public class LabelControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenService tokenService;

    @Autowired
    ObjectMapper objectMapper;

    // testGetAllLabelsPositive
    // testGetAllLabelsNoToken
    // testGetLabelPositive
    // testGetLabelNoToken
    // testCreateLabelPositive
    // testCreateLabelNegativeInvalidData
    // testCreateLabelNoToken
    // testUpdateLabelPositive
    // testUpdateLabelNegativeInvalidData
    // testUpdateLabelNoToken
    // testDeleteLabelPositive
    // testDeleteLabelNoToken

    @Test
    void testGetAllLabelsPositive() throws Exception {
        String token = "Bearer " + tokenService.getToken(Map.of("username", "johnsmith@gmail.com"));
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/labels")
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());
        assertThat(response.getContentAsString()).contains("BUG");
        assertThat(response.getContentAsString()).contains("DOC");
    }

    void testGetAllLabelsNoToken() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/labels"))
                .andReturn()
                .getResponse();

        assertEquals(422, response.getStatus());
        assertThat(response.getContentAsString()).doesNotContain("BUG");
        assertThat(response.getContentAsString()).doesNotContain("DOC");
    }

    @Test
    void testGetLabelPositive() throws Exception {
        String token = "Bearer " + tokenService.getToken(Map.of("username", "johnsmith@gmail.com"));
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/labels/54")
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());
        assertThat(response.getContentAsString()).contains("first good issue");
    }

    @Test
    void testGetLabelNoToken() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/labels/54"))
                .andReturn()
                .getResponse();

        assertEquals(403, response.getStatus());
        assertThat(response.getContentAsString()).doesNotContain("first good issue");
    }

    @Test
    void testCreateLabelPositive() throws Exception {
        LabelDto labelDto = new LabelDto("test label");
        String token = "Bearer " + tokenService.getToken(Map.of("username", "johnsmith@gmail.com"));
        MockHttpServletResponse postResponse = mockMvc
                .perform(post("/api/labels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(labelDto))
                        .header(AUTHORIZATION, token)
                )
                .andReturn().getResponse();

        assertEquals(201, postResponse.getStatus());

        MockHttpServletResponse response = mockMvc
                .perform(get("/api/labels")
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());
        assertThat(response.getContentAsString()).contains(labelDto.getName());
    }

    @Test
    void testCreateLabelNegativeInvalidData() throws Exception {
        LabelDto labelDto = new LabelDto(" ");
        String token = "Bearer " + tokenService.getToken(Map.of("username", "johnsmith@gmail.com"));
        MockHttpServletResponse postResponse = mockMvc
                .perform(post("/api/labels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(labelDto))
                        .header(AUTHORIZATION, token)
                )
                .andReturn().getResponse();

        assertEquals(422, postResponse.getStatus());

        // MockHttpServletResponse response = mockMvc
        //         .perform(get("/api/labels")
        //                 .header(AUTHORIZATION, token)
        //         )
        //         .andReturn()
        //         .getResponse();
        //
        // assertEquals(200, response.getStatus());
        // assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());
        // assertThat(response.getContentAsString()).doesNotContain(labelDto.getName());
    }

    @Test
    void testCreateLabelNoToken() throws Exception {
        LabelDto labelDto = new LabelDto("test label");

        MockHttpServletResponse postResponse = mockMvc
                .perform(post("/api/labels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(labelDto))
                )
                .andReturn().getResponse();

        assertEquals(403, postResponse.getStatus());

        String token = "Bearer " + tokenService.getToken(Map.of("username", "johnsmith@gmail.com"));
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/labels")
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());
        assertThat(response.getContentAsString()).doesNotContain(labelDto.getName());
    }

    @Test
    void testUpdateLabelPositive() throws Exception {
        // get current status
        String token = "Bearer " + tokenService.getToken(Map.of("username", "johnsmith@gmail.com"));
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/labels/51")
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());

        // get init label name
        Label initLabel = objectMapper.readValue(response.getContentAsString(), Label.class);

        // update
        LabelDto labelDto = new LabelDto("test label");
        MockHttpServletResponse postResponse = mockMvc
                .perform(put("/api/labels/51")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(labelDto))
                        .header(AUTHORIZATION, token)
                )
                .andReturn().getResponse();

        assertEquals(200, postResponse.getStatus());
        assertEquals(MediaType.APPLICATION_JSON.toString(), postResponse.getContentType());
        assertThat(postResponse.getContentAsString()).contains(labelDto.getName());

        // check update status
        response = mockMvc
                .perform(get("/api/labels/51")
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());
        assertThat(response.getContentAsString()).contains(labelDto.getName());
        assertThat(response.getContentAsString()).doesNotContain(initLabel.getName());
    }

    @Test
    void testUpdateLabelNegativeInvalidData() throws Exception {
        // get current status
        String token = "Bearer " + tokenService.getToken(Map.of("username", "johnsmith@gmail.com"));
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/labels/51")
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());

        // get init status name
        Label initLabel = objectMapper.readValue(response.getContentAsString(), Label.class);

        // update
        LabelDto updateLabelDto = new LabelDto("");

        MockHttpServletRequestBuilder request = put("/api/labels/51")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateLabelDto))
                .header(AUTHORIZATION, token);

        MockHttpServletResponse patchResponse = mockMvc
                .perform(request)
                .andReturn()
                .getResponse();

        assertEquals(422, patchResponse.getStatus());

        // check updated status
        response = mockMvc
                .perform(get("/api/labels/51")
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertThat( response.getContentAsString() ).contains(initLabel.getName());
    }

    @Test
    void testUpdateLabelNoToken() throws Exception {
        // get current status
        String token = "Bearer " + tokenService.getToken(Map.of("username", "johnsmith@gmail.com"));
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/labels/51")
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());

        // get init status name
        Label initLabel = objectMapper.readValue(response.getContentAsString(), Label.class);

        // update
        LabelDto updateLabelDto = new LabelDto("test label");

        MockHttpServletRequestBuilder request = put("/api/labels/51")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateLabelDto));

        MockHttpServletResponse patchResponse = mockMvc
                .perform(request)
                .andReturn()
                .getResponse();

        assertEquals(403, patchResponse.getStatus());

        // check updated status
        response = mockMvc
                .perform(get("/api/labels/51")
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertThat( response.getContentAsString() ).contains(initLabel.getName());
        assertThat( response.getContentAsString() ).doesNotContain(updateLabelDto.getName());
    }

    @Test
    void testDeleteLabelPositive() throws Exception {
        // get current status
        String token = "Bearer " + tokenService.getToken(Map.of("username", "johnsmith@gmail.com"));
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/labels/54")
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());

        // get init status name
        Label initLabel = objectMapper.readValue(response.getContentAsString(), Label.class);

        // delete

        MockHttpServletResponse patchResponse = mockMvc
                .perform(delete("/api/labels/54")
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, patchResponse.getStatus());

        // check status
        response = mockMvc
                .perform(get("/api/labels")
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertThat( response.getContentAsString() ).doesNotContain(initLabel.getName());
    }

    @Test
    void testDeleteLabelNoToken() throws Exception {
        // get current status
        String token = "Bearer " + tokenService.getToken(Map.of("username", "johnsmith@gmail.com"));
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/labels/54")
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());

        // get init status name
        Label initLabel = objectMapper.readValue(response.getContentAsString(), Label.class);

        // delete
        MockHttpServletResponse patchResponse = mockMvc
                .perform(delete("/api/labels/54"))
                .andReturn()
                .getResponse();

        assertEquals(403, patchResponse.getStatus());

        // check status
        response = mockMvc
                .perform(get("/api/labels")
                        .header(AUTHORIZATION, token)
                )
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        assertThat( response.getContentAsString() ).contains(initLabel.getName());
    }
}
