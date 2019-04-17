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
public class BookControllerTest {
	@Autowired
	private MockMvc mock;

	@Test
	public void addBook() throws Exception {
		String mockBookCategoryAsJSON = "{\"bookName\":\"City of Bones\",\"authorName\":\"Cassandra Clare\",\"borrowingTypeAtHome\":true,\"borrowingPeriod\":\"TWO_WEEKS\",\"bookCategories\":[]}";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/library/book/").accept(MediaType.APPLICATION_JSON)
				.content(mockBookCategoryAsJSON).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mock.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), is(HttpStatus.CREATED.value()));

		assertThat(response.getHeader(HttpHeaders.LOCATION), is("http://localhost/book"));
	}

	@Test
	public void findBook() throws Exception {
		addBook();

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/library/book/1")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult mvcResult = mock.perform(requestBuilder).andReturn();

		String resultExpected = "{\"bookId\":1,\"bookName\":\"City of Bones\",\"authorName\":\"Cassandra Clare\",\"borrowingTypeAtHome\":true,\"borrowingPeriod\":\"TWO_WEEKS\",\"bookCategories\":[]}";

		assertThat(mvcResult.getResponse().getContentAsString(), is(resultExpected));
	}

	@Test
	public void updateBook() throws Exception {
		addBook();

		String mockBookCategoryAsJSON = "{\"bookId\":1,\"bookName\":\"City of Bones\",\"authorName\":\"Francine Rivers\",\"borrowingTypeAtHome\":false,\"borrowingPeriod\":\"ONE_MONTH\",\"bookCategories\":[]}";

		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/library/book/1").accept(MediaType.APPLICATION_JSON)
				.content(mockBookCategoryAsJSON).contentType(MediaType.APPLICATION_JSON);

		MvcResult mvcResult = mock.perform(requestBuilder).andReturn();

		String resultExpected = "{\"bookId\":1,\"bookName\":\"City of Bones\",\"authorName\":\"Francine Rivers\",\"borrowingTypeAtHome\":false,\"borrowingPeriod\":\"ONE_MONTH\",\"bookCategories\":[]}";

		assertThat(mvcResult.getResponse().getContentAsString(), is(resultExpected));
	}

	@Test
	public void deleteBook() throws Exception {
		addBook();

		String mockBookCategoryAsJSON = "{\"bookId\":1,\"bookName\":\"City of Bones\",\"authorName\":\"Cassandra Clare\",\"borrowingTypeAtHome\":true,\"borrowingPeriod\":\"TWO_WEEKS\",\"bookCategories\":[]}";

		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/library/book/1")
				.accept(MediaType.APPLICATION_JSON).content(mockBookCategoryAsJSON)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult mvcResult = mock.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();

		assertThat(response.getStatus(), is(HttpStatus.NO_CONTENT.value()));
	}
}
