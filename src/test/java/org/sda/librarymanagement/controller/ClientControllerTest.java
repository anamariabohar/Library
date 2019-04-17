package org.sda.librarymanagement.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(secure = false)
public class ClientControllerTest {
	@Autowired
	private MockMvc mock;

	@Autowired
	private Mock mockRepository;

	@Test
	public void addClient() throws Exception {
		String mockBookCategoryAsJSON = "{\"username\":\"guest\",\"password\":\"guest\",\"firstName\":\"New\",\"lastName\":\"Guest\",\"phone\":\"00000976\",\"email\":\"new.guest@localhost.com\",\"memberships\":[]}";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/library/client/")
				.accept(MediaType.APPLICATION_JSON).content(mockBookCategoryAsJSON)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mock.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), is(HttpStatus.CREATED.value()));

		assertThat(response.getHeader(HttpHeaders.LOCATION), is("http://localhost/client"));
	}

	@Test
	public void findClient() throws Exception {
		addClient();

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/library/client/1")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult mvcResult = mock.perform(requestBuilder).andReturn();

		String resultExpected = "{\"clientId\":1,\"username\":\"guest\",\"password\":\"guest\",\"firstName\":\"New\",\"lastName\":\"Guest\",\"phone\":\"00000976\",\"email\":\"new.guest@localhost.com\",\"memberships\":[]}";

		assertThat(mvcResult.getResponse().getContentAsString(), is(resultExpected));
	}

	@Test
	public void updateClient() throws Exception {
		addClient();

		String mockBookCategoryAsJSON = "{\"clientId\":1,\"username\":\"guest\",\"password\":\"guest\",\"firstName\":\"New\",\"lastName\":\"Guest\",\"phone\":\"00000976\",\"email\":\"new.guest@localhost.com\",\"memberships\":[]}";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/library/client/1")
				.accept(MediaType.APPLICATION_JSON).content(mockBookCategoryAsJSON)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult mvcResult = mock.perform(requestBuilder).andReturn();

		String resultExpected = "{\"clientId\":1,\"username\":\"guest\",\"password\":\"guest\",\"firstName\":\"New\",\"lastName\":\"Guest\",\"phone\":\"00000976\",\"email\":\"new.guest@localhost.com\",\"memberships\":[]}";
		assertThat(mvcResult.getResponse().getContentAsString(), is(resultExpected));
	}

	@Test
	public void deleteClient() throws Exception {
		addClient();

		String mockBookCategoryAsJSON = "{\"clientId\":1,\"username\":\"guest\",\"password\":\"guest\",\"firstName\":\"New\",\"lastName\":\"Guest\",\"phone\":\"00000976\",\"email\":\"new.guest@localhost.com\",\"memberships\":[]}";

		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/library/client/1")
				.accept(MediaType.APPLICATION_JSON).content(mockBookCategoryAsJSON)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult mvcResult = mock.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();

		assertThat(response.getStatus(), is(HttpStatus.NO_CONTENT.value()));
	}
}
