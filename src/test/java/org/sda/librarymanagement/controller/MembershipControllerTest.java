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
public class MembershipControllerTest {

	@Autowired
	private MockMvc mock;

	@Test
	public void addMembership() throws Exception {
		String mockBookCategoryAsJSON = "{\"membershipType\":\"BASIC\",\"startDate\":\"2018-03-13\",\"endDate\":\"2018-04-13\",\"client\":null}";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/library/membership/")
				.accept(MediaType.APPLICATION_JSON).content(mockBookCategoryAsJSON)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mock.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertThat(response.getStatus(), is(HttpStatus.CREATED.value()));

		assertThat(response.getHeader(HttpHeaders.LOCATION), is("http://localhost/membership"));
	}

	@Test
	public void findMembership() throws Exception {
		addMembership();

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/library/membership/1")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult mvcResult = mock.perform(requestBuilder).andReturn();

		String resultExpected = "{\"membershipID\":1,\"membershipType\":\"BASIC\",\"startDate\":\"2018-03-13\",\"endDate\":\"2018-04-13\",\"client\":null}";

		assertThat(mvcResult.getResponse().getContentAsString(), is(resultExpected));
	}

	@Test
	public void updateMembership() throws Exception {
		addMembership();

		String mockBookCategoryAsJSON = "{\"membershipID\":1,\"membershipType\":\"BASIC\",\"startDate\":\"2018-03-13\",\"endDate\":\"2018-04-13\",\"client\":null}";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/library/membership/1")
				.accept(MediaType.APPLICATION_JSON).content(mockBookCategoryAsJSON)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult mvcResult = mock.perform(requestBuilder).andReturn();

		String resultExpected = "{\"membershipID\":1,\"membershipType\":\"BASIC\",\"startDate\":\"2018-03-13\",\"endDate\":\"2018-04-13\",\"client\":null}";
		assertThat(mvcResult.getResponse().getContentAsString(), is(resultExpected));
	}

	@Test
	public void deleteMembership() throws Exception {
		addMembership();

		String mockBookCategoryAsJSON = "{\"membershipID\":1,\"membershipType\":\"BASIC\",\"startDate\":\"2018-03-13\",\"endDate\":\"2018-04-13\",\"client\":null}";

		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/library/membership/1")
				.accept(MediaType.APPLICATION_JSON).content(mockBookCategoryAsJSON)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult mvcResult = mock.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();

		assertThat(response.getStatus(), is(HttpStatus.NO_CONTENT.value()));
	}
}
