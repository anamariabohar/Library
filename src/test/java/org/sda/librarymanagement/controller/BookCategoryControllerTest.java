package org.sda.librarymanagement.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
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
public class BookCategoryControllerTest {

	@Autowired
	private MockMvc mock;

	@Test
	public void addBookCategory() throws Exception {
		String mockBookCategoryAsJSON = "{\"categoryId\":1,\"books\":[],\"categoryName\":\"Science Fiction\"}";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/library/bookCategory/")
				.accept(MediaType.APPLICATION_JSON).content(mockBookCategoryAsJSON)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mock.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), is(HttpStatus.CREATED.value()));

		assertThat(response.getHeader(HttpHeaders.LOCATION), is("http://localhost/bookCategory"));
	}

	@Test
	public void findBookCategory() throws Exception {
		addBookCategory();

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/library/bookCategory/1")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult mvcResult = mock.perform(requestBuilder).andReturn();

		String resultExpected = "{\"categoryId\":1,\"books\":[],\"categoryName\":\"Science Fiction\"}";

		assertThat(mvcResult.getResponse().getContentAsString(), is(resultExpected));
	}

	@Test
	public void updateBookCategory() throws Exception {
		addBookCategory();

		String mockBookCategoryAsJSON = "{\"categoryId\":1,\"books\":[],\"categoryName\":\"Drama\"}";

		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/library/bookCategory/1")
				.accept(MediaType.APPLICATION_JSON).content(mockBookCategoryAsJSON)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult mvcResult = mock.perform(requestBuilder).andReturn();

		String resultExpected = "{\"categoryId\":1,\"books\":[],\"categoryName\":\"Drama\"}";

		assertThat(mvcResult.getResponse().getContentAsString(), is(resultExpected));
	}

	@Test
	public void deleteBookCategory() throws Exception {
		addBookCategory();

		String mockBookCategoryAsJSON = "{\"categoryId\":1,\"books\":[],\"categoryName\":\"Drama\"}";

		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/library/bookCategory/1")
				.accept(MediaType.APPLICATION_JSON).content(mockBookCategoryAsJSON)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult mvcResult = mock.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();

		assertThat(response.getStatus(), is(HttpStatus.NO_CONTENT.value()));
	}
}
