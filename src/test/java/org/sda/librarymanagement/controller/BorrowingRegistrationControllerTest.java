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
public class BorrowingRegistrationControllerTest {

	@Autowired
	private MockMvc mock;

	@Test
	public void addMembership() throws Exception {
		addClient();
		String mockBookCategoryAsJSON = "{\"membershipType\":\"BASIC\",\"startDate\":\"2018-03-13\",\"endDate\":\"2018-04-13\",\"client\":{\"clientId\" :1}}";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/library/membership/")
				.accept(MediaType.APPLICATION_JSON).content(mockBookCategoryAsJSON)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mock.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), is(HttpStatus.CREATED.value()));

		assertThat(response.getHeader(HttpHeaders.LOCATION), is("http://localhost/membership"));
	}

	@Test
	public void addClient() throws Exception {
		addMembership();
		String mockBookCategoryAsJSON = "{\"username\":\"guest\",\"password\":\"guest\",\"firstName\":\"New\",\"lastName\":\"Guest\",\"phone\":\"00000976\",\"email\":\"new.guest@localhost.com\",\"memberships\":[{\"membershipId\" :1}]}";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/library/client/")
				.accept(MediaType.APPLICATION_JSON).content(mockBookCategoryAsJSON)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mock.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), is(HttpStatus.CREATED.value()));

		assertThat(response.getHeader(HttpHeaders.LOCATION), is("http://localhost/client"));
	}

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
	public void addBook() throws Exception {
		addBookCategory();
		String mockBookCategoryAsJSON = "{\"bookName\":\"City of Bones\",\"authorName\":\"Cassandra Clare\",\"borrowingTypeAtHome\":true,\"borrowingPeriod\":\"TWO_WEEKS\",\"bookCategories\":[\"categoryId\":1,\"categoryName\":\"Science Fiction\"]}";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/library/book/").accept(MediaType.APPLICATION_JSON)
				.content(mockBookCategoryAsJSON).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mock.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), is(HttpStatus.CREATED.value()));

		assertThat(response.getHeader(HttpHeaders.LOCATION), is("http://localhost/book"));
	}

	@Test
	public void addBorrowingRegistration() throws Exception {
		addMembership();
		addClient();
		addBook();
		String mockBookCategoryAsJSON = "{\"borrowingRegistrationId\":1,\"client\":{\"clientId\" :1},\"book\":{\"bookId\":1},\"borrowingDate\":\"2018-04-25\",\"returnDate\":\"2018-05-25\"}";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/library/borrowingRegistration/")
				.accept(MediaType.APPLICATION_JSON).content(mockBookCategoryAsJSON)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mock.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), is(HttpStatus.CREATED.value()));

		assertThat(response.getHeader(HttpHeaders.LOCATION), is("http://localhost/borrowingRegistration"));
	}

	@Test
	public void findBorrowingRegistration() throws Exception {
		addBorrowingRegistration();

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/library/borrowingRegistration/1")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult mvcResult = mock.perform(requestBuilder).andReturn();

		String resultExpected = "{\"borrowingRegistrationId\":1,\"client\":null,\"book\":null,\"borrowingDate\":\"2018-04-25\",\"returnDate\":\"2018-05-25\"}";

		assertThat(mvcResult.getResponse().getContentAsString(), is(resultExpected));
	}

	@Test
	public void updateBorrowingRegistration() throws Exception {
		addBorrowingRegistration();

		String mockBookCategoryAsJSON = "{\"borrowingRegistrationId\":1,\"client\":null,\"book\":null,\"borrowingDate\":\"2018-04-03\",\"returnDate\":\"2018-05-25\"}";

		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/library/borrowingRegistration/1")
				.accept(MediaType.APPLICATION_JSON).content(mockBookCategoryAsJSON)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult mvcResult = mock.perform(requestBuilder).andReturn();

		String resultExpected = "{\"borrowingRegistrationId\":1,\"client\":null,\"book\":null,\"borrowingDate\":\"2018-04-03\",\"returnDate\":\"2018-05-25\"}";

		assertThat(mvcResult.getResponse().getContentAsString(), is(resultExpected));
	}

	@Test
	public void deleteBorrowingRegistration() throws Exception {
		addBorrowingRegistration();

		String mockBookCategoryAsJSON = "{\"borrowingRegistrationId\":1,\"client\":null,\"book\":null,\"borrowingDate\":\"2018-04-25\",\"returnDate\":\"2018-05-25\"}";

		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/library/borrowingRegistration/1")
				.accept(MediaType.APPLICATION_JSON).content(mockBookCategoryAsJSON)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult mvcResult = mock.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();

		assertThat(response.getStatus(), is(HttpStatus.NO_CONTENT.value()));
	}
}
