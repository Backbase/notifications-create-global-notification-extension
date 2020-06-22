package com.backbase.dbs.presentation.services.notifications.validation;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.backbase.dbs.notifications.rest.spec.v2.notifications.NotificationsPostRequestBody;
import com.backbase.dbs.notifications.rest.spec.v2.notifications.SeverityLevel;
import com.backbase.dbs.presentation.services.notifications.Application;
import com.backbase.dbs.presentation.services.notifications.util.ServiceJwtTokenProvider;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {
        Application.class
    })
@DirtiesContext
@ActiveProfiles("it")
@TestPropertySource(properties =
    {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration"
    })
@TestExecutionListeners({
    DirtiesContextTestExecutionListener.class,
    DependencyInjectionTestExecutionListener.class,
    DbUnitTestExecutionListener.class
})
public class ValidatorConfigTest {

    private static final String SERVICE_NOTIFICATIONS_ROOT = "/service-api/v2/notifications";

    @Autowired
    WebApplicationContext wac;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mvc;

    @BeforeClass
    public static void setEnvironmentVariables() {
        System.setProperty("SIG_SECRET_KEY", "JwtSecretKeyPleaseDoNotUseInProduction!");
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
    }

    @Test
    @ExpectedDatabase(value = "classpath:expected-global-notification.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void shouldCreateGlobalNotification() throws Exception {
        NotificationsPostRequestBody requestBody = createNotificationsPostRequestBody();

        mvc.perform(post(SERVICE_NOTIFICATIONS_ROOT)
            .content(objectMapper.writeValueAsBytes(requestBody))
            .header(HttpHeaders.AUTHORIZATION, ServiceJwtTokenProvider.createServiceToken())
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    private NotificationsPostRequestBody createNotificationsPostRequestBody() {
        return new NotificationsPostRequestBody()
            .withMessage("Message")
            .withLevel(SeverityLevel.INFO)
            .withTargetGroup(NotificationsPostRequestBody.TargetGroup.GLOBAL)
            .withOrigin("actions");
    }

}
