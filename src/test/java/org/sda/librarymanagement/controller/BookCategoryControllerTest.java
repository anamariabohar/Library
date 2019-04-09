package org.sda.librarymanagement.controller;

import static org.junit.Assert.assertEquals;

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
		String mockBookCategoryAsJSON = "{\"categoryName\":\"Science Fiction\"}";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/library/bookCategory/")
				.accept(MediaType.APPLICATION_JSON).content(mockBookCategoryAsJSON)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mock.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.CREATED.value(), response.getStatus());

		assertEquals("http://localhost/bookCategory", response.getHeader(HttpHeaders.LOCATION));
	}

	@Test
	public void findBookCategory() throws Exception {
		addBookCategory();

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/library/bookCategory/1")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult mvcResult = mock.perform(requestBuilder).andReturn();

		String resultExpected = "{\"categoryId\":1,\"books\":[],\"categoryName\":\"Science Fiction\"}";

		assertEquals(resultExpected, mvcResult.getResponse().getContentAsString());
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

		assertEquals(resultExpected, mvcResult.getResponse().getContentAsString());
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

		assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
	}
}
