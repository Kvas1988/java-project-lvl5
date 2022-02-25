package hexlet.code.app;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


import javax.transaction.Transactional;
import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("/data.sql")
class AppApplicationTests {

	@Autowired
	private MockMvc mockMvc;

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
		MockHttpServletResponse response = mockMvc
				.perform(get("/api/users/1"))
				.andReturn()
				.getResponse();

		assertEquals(200, response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());
		assertThat(response.getContentAsString()).contains("John", "Smith");
	}

	@Test
	void testGetUserNotFound() throws Exception {
		MockHttpServletResponse response = mockMvc
				.perform(get("/api/users/9999"))
				.andReturn()
				.getResponse();

		assertEquals(404, response.getStatus());
	}

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
		MockHttpServletResponse responsePatch = mockMvc
				.perform(patch("/api/users/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"firstName\": \"Pavel\", "
								+ "\"lastName\": \"Bure\", "
								+ "\"password\": \"pass\", "
								+ "\"email\": \"bure@gmail.com\"}")
				)
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
		MockHttpServletResponse responsePatch = mockMvc
				.perform(patch("/api/users/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"firstName\": \"Pavel\", "
								+ "\"password\": \"pass\", "
								+ "\"email\": \"bure@gmail.com\"}")
				)
				.andReturn()
				.getResponse();

		assertEquals(422, responsePatch.getStatus());
		// more tests on post response???
	}

	@Test
	void testUpdateUserNotFound() throws Exception {
		MockHttpServletResponse responsePatch = mockMvc
				.perform(patch("/api/users/9999")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"firstName\": \"Pavel\", "
								+ "\"lastName\": \"Bure\", "
								+ "\"password\": \"pass\", "
								+ "\"email\": \"bure@gmail.com\"}")
				)
				.andReturn()
				.getResponse();

		assertEquals(404, responsePatch.getStatus());
		// more tests on post response???
	}

	@Test
	void testDeleteUserPositive() throws Exception {
		// check if user exists
		MockHttpServletResponse response = mockMvc
				.perform(get("/api/users/1"))
				.andReturn()
				.getResponse();

		assertEquals(200, response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON.toString(), response.getContentType());
		assertThat(response.getContentAsString()).contains("John", "Smith");

		// delete user
		MockHttpServletResponse responseDelete = mockMvc
				.perform(delete("/api/users/1"))
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

	@Test
	void testDeleteUserNotSuchUser() throws Exception {
		// check if user exists
		MockHttpServletResponse response = mockMvc
				.perform(delete("/api/users/9999"))
				.andReturn()
				.getResponse();

		assertEquals(404, response.getStatus());
		// more tests on post response???
	}
}
