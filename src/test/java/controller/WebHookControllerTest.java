package controller;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;
import java.util.Scanner;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import service.MessageProcessingService;
import service.utils.CryptoUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class WebHookControllerTest {

	private MockMvc mockMvc;

	public String TEST_APP_SECRET = "312ea4234af234cbd";
	
	public String TEST_VALIDATION_TOKEN = "134e4c56cde53f4534";
	
	@Mock
	private MessageProcessingService messageProcessingServiceMock;
	
	@InjectMocks
	private WebHookController webHookController = new WebHookController();
	
	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(webHookController).build();
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(webHookController, "APP_SECRET", TEST_APP_SECRET);
		ReflectionTestUtils.setField(webHookController, "VALIDATION_TOKEN", TEST_VALIDATION_TOKEN);
	}
	
	@Test
	public void testWebHookVerify_Success() throws Exception {
		String challenge = "SAMPLE_CHALLENGE";
		mockMvc.perform(get("/webhook")
				.param("hub.verify_token", TEST_VALIDATION_TOKEN)
				.param("hub.challenge", challenge))
		.andExpect(status().is(200))
		.andExpect(content().string(challenge));
	}
	
	@Test
	public void testWebHookVerify_WrongToken() throws Exception {
		String challenge = "SAMPLE_CHALLENGE";
		String token = "12345679";
		
		mockMvc.perform(get("/webhook")
				.param("hub.verify_token", token)
				.param("hub.challenge", challenge))
		.andExpect(status().is(403))
		.andExpect(content().string("Wrong validation token"));
	}
	
	@Test
	public void testWebHookMessage() throws Exception {
		doNothing().when(messageProcessingServiceMock).process(isA(JSONObject.class));
		
		InputStream is = this.getClass().getResourceAsStream("/json/webhookMessage_sha1_1.json");
		Scanner sc = new Scanner(is,"UTF-8");
		String json = sc.useDelimiter("\\Z").next();
		sc.close();
		
		String signature = "sha1=".concat(CryptoUtils.createHMAC(TEST_APP_SECRET, json, "HmacSHA1"));
		
		mockMvc.perform(post("/webhook")
				.header("x-hub-signature", signature)
				.contentType("application/json")
				.content(json))
		.andExpect(status().is(200))
		.andExpect(content().string(""));
		
		verify(messageProcessingServiceMock, times(1)).process(isA(JSONObject.class));
	}
}
