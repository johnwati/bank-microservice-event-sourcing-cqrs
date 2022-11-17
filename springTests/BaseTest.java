package ;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.SpringApplication;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import .ContentType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = SpringApplication.class)
@Ignore
@ActiveProfiles("unittest")
public abstract class BaseTest {

  @Autowired
  private WebApplicationContext wac;
 
  protected MockMvc mockMvc;
  
  @ClassRule
  public static WireMockClassRule wireMockRule = new WireMockClassRule(8089);

  @Rule
  public WireMockClassRule instanceRule = wireMockRule;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	// Custom AccessToken implementation if any, goes here!!
	//protected String generateJWTToken() {}
	    
	public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		//mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.writeValueAsBytes(object);
	}
  
	private void executeRequest(MockHttpServletRequestBuilder builder, ContentType contentType, 
			byte[] content, ResultMatcher... matchers) throws Exception{
		if(contentType == ContentType.JSON){
			builder.contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName(utf8)));
		}
		if(content != null){
			builder.content(content);
		}
		ResultActions results = mockMvc.perform(builder);
		for(ResultMatcher matcher: matchers){
			results.andExpect(matcher);  
		} 
	}
  
	public void executeGetRequest(String url, ContentType contentType, 
									ResultMatcher... matchers) throws Exception{
		MockHttpServletRequestBuilder builder = get(url);
		executeRequest(builder, contentType, null, matchers);
	}
  

	public void executePostRequest(String url, ContentType contentType, 
									byte[] content, ResultMatcher... matchers) throws Exception{
		MockHttpServletRequestBuilder builder = post(url);
		executeRequest(builder, contentType, content, matchers);
	}
  
	public void executePutRequest(String url, ContentType contentType, 
									byte[] content, ResultMatcher... matchers) throws Exception{
		MockHttpServletRequestBuilder builder = put(url);
		executeRequest(builder, contentType, content, matchers);
	}

		public void executeDeleteRequest(String url, ContentType contentType, 
			ResultMatcher... matchers) throws Exception {
		MockHttpServletRequestBuilder builder = delete(url);
		executeRequest(builder, contentType, null, matchers);
	}
	
	public void mockRemotePutService(String uri,byte[] bytes,int status) throws IOException
    {	
	    stubFor(com.github.tomakehurst.wiremock.client.WireMock.put(urlEqualTo(uri)) 
	        .willReturn(aResponse()
	            .withStatus(status)
	            .withHeader("Content-Type", "application/json").withBody(bytes))); 
	}
	
	public void mockRemoteDeleteService(String uri,byte[] bytes,int status) throws IOException
    {	
	    stubFor(com.github.tomakehurst.wiremock.client.WireMock.delete(urlEqualTo(uri)) 
	        .willReturn(aResponse()
	            .withStatus(status)
	            .withHeader("Content-Type", "application/json").withBody(bytes))); 
	}
	
	public void mockRemoteGetService(String uri,byte[] bytes,int status) throws IOException
    {	
	    stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(urlEqualTo(uri)) 
	        .willReturn(aResponse()
	            .withStatus(status)
	            .withHeader("Content-Type", "application/json").withBody(bytes))); 
	}
	
	public void mockRemotePostService(String uri,byte[] bytes,int status) throws IOException
    {	
	    stubFor(com.github.tomakehurst.wiremock.client.WireMock.post(urlEqualTo(uri)) 
	        .willReturn(aResponse()
	            .withStatus(status)
	            .withHeader("Content-Type", "application/json").withBody(bytes))); 
	}
}
