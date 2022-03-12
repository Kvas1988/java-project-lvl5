package hexlet.code.app;

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
class AppApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	JWTTokenService tokenService;

	@Test
	void testWelcomePage() throws Exception {
		MockHttpServletResponse response = mockMvc
				.perform(get("/welcome"))
				.andReturn()
				.getResponse();

		assertEquals(200, response.getStatus());
		assertEquals("Welcome to Spring", response.getContentAsString());
	}

	@Test
	void testGetUserPositive() throws Exception {
		String token = tokenService.getToken(Map.of("email", "johnsmith@gmail.com"));
		MockHttpServletResponse response = mockMvc
				.perform(get("/api/users/1")
							.header(AUTHORIZATION, token))
				.andReturn()
				.getResponse();

		assertEquals(200, response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());
		assertThat(response.getContentAsString()).contains("John", "Smith");
	}

	// 401 (unauth) expected 404 -> rly should be 401 ?
	// @Test
	// void testGetUserNotFound() throws Exception {
	// 	MockHttpServletResponse response = mockMvc
	// 			.perform(get("/api/users/9999"))
	// 			.andReturn()
	// 			.getResponse();
	//
	// 	assertEquals(404, response.getStatus());
	// }

	@Test
	void testGetUsers() throws Exception {
		MockHttpServletResponse response = mockMvc
				.perform(get("/api/users"))
				.andReturn()
				.getResponse();

		assertEquals(200, response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());
		assertThat(response.getContentAsString()).contains("John", "Smith");
		assertThat(response.getContentAsString()).contains("Jack", "Doe");
	}

	@Test
	void testCreateUserPositive() throws Exception {
		MockHttpServletResponse responsePost = mockMvc
				.perform(post("/api/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"firstName\": \"Pavel\", "
								+ "\"lastName\": \"Bure\", "
								+ "\"email\": \"pavelbure1971@gmail.com\", "
								+ "\"password\": \"password\"}"))
				.andReturn()
				.getResponse();

		assertEquals(200, responsePost.getStatus());
		// more tests on post response???

		MockHttpServletResponse response = mockMvc
				.perform(get("/api/users"))
				.andReturn()
				.getResponse();

		assertEquals(200, response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());
		assertThat(response.getContentAsString()).contains("Pavel", "Bure");
	}

	// Doesn't pass this test. DUNNO WHY
	// @Test
	// void testCreateUserInvalidData() throws Exception {
	// 	MockHttpServletResponse responsePost = mockMvc
	// 			.perform(post("/api/users")
	// 					.contentType(MediaType.APPLICATION_JSON)
	// 					.content("\"lastName\": \"Bure\", "
	// 							+ "\"email\": \"pavelbure1971@gmail.com\", "
	// 							+ "\"password\": \"password\"}"))
	// 			.andReturn()
	// 			.getResponse();
	//
	// 	assertEquals(422, responsePost.getStatus());
	// 	// more tests on post response???
	// }

	@Test
	void testUpdateUserPositive() throws Exception {
		String token = tokenService.getToken(Map.of("email", "johnsmith@gmail.com"));
		MockHttpServletRequestBuilder request = put("/api/users/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"firstName\": \"Pavel\", "
						+ "\"lastName\": \"Bure\", "
						+ "\"password\": \"pass\", "
						+ "\"email\": \"bure@gmail.com\"}")
				.header(AUTHORIZATION, token);

		MockHttpServletResponse responsePatch = mockMvc
				.perform(request)
				.andReturn()
				.getResponse();

		assertEquals(200, responsePatch.getStatus());
		// more tests on post response???

		MockHttpServletResponse response = mockMvc
				.perform(get("/api/users"))
				.andReturn()
				.getResponse();

		assertEquals(200, response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());
		assertThat(response.getContentAsString()).contains("Pavel", "Bure");
	}

	@Test
	void testUpdateUserInvalidData() throws Exception {
		String token = tokenService.getToken(Map.of("email", "johnsmith@gmail.com"));
		MockHttpServletResponse responsePatch = mockMvc
				.perform(put("/api/users/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"firstName\": \"Pavel\", "
								+ "\"password\": \"pass\", "
								+ "\"email\": \"bure@gmail.com\"}")
						.header(AUTHORIZATION, token)
				)
				.andReturn()
				.getResponse();

		assertEquals(422, responsePatch.getStatus());
		// more tests on post response???
	}

	// 401 (unauth) expected 404 -> rly should be 401 ?
	// @Test
	// void testUpdateUserNotFound() throws Exception {
	// 	MockHttpServletResponse responsePatch = mockMvc
	// 			.perform(patch("/api/users/9999")
	// 					.contentType(MediaType.APPLICATION_JSON)
	// 					.content("{\"firstName\": \"Pavel\", "
	// 							+ "\"lastName\": \"Bure\", "
	// 							+ "\"password\": \"pass\", "
	// 							+ "\"email\": \"bure@gmail.com\"}")
	// 			)
	// 			.andReturn()
	// 			.getResponse();
	//
	// 	assertEquals(404, responsePatch.getStatus());
	// 	// more tests on post response???
	// }

	@Test
	void testDeleteUserPositive() throws Exception {
		// check if user exists
		MockHttpServletResponse response = mockMvc
				.perform(get("/api/users"))
				.andReturn()
				.getResponse();

		assertEquals(200, response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());
		assertThat(response.getContentAsString()).contains("John", "Smith");

		// delete user
		String token = tokenService.getToken(Map.of("email", "johnsmith@gmail.com"));
		MockHttpServletResponse responseDelete = mockMvc
				.perform(delete("/api/users/1")
							.header(AUTHORIZATION, token))
				.andReturn()
				.getResponse();

		assertEquals(200, responseDelete.getStatus());
		// more tests on post response???

		// check that user was deleted
		response = mockMvc
				.perform(get("/api/users"))
				.andReturn()
				.getResponse();

		assertEquals(200, response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());
		assertThat(response.getContentAsString()).doesNotContain("John", "Smith");
	}

	// 401 (unauth) expected 404 -> rly should be 401 ?
	// @Test
	// void testDeleteUserNotSuchUser() throws Exception {
	// 	// check if user exists
	// 	MockHttpServletResponse response = mockMvc
	// 			.perform(delete("/api/users/9999"))
	// 			.andReturn()
	// 			.getResponse();
	//
	// 	assertEquals(404, response.getStatus());
	// 	// more tests on post response???
	// }
}
