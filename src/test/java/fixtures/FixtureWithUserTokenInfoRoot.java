package fixtures;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import models.Auth.User;
import services.auth.UserService;
import utils.config.IConfig;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FixtureWithUserTokenInfoRoot extends TestFixtures implements IConfig {

    // This fixture logs in with new User
    private String token;
    private String localStorage;

    public String getToken() {
        return token;
    }

    @Override
    @BeforeEach
    // This method creates a new user and use the auth token to view the singed in state
    void createContextAndPage() {
        if (localStorage == null) {
            try {
                User newUser = new UserService().createNewRandomUser(getRequest());
                token = newUser.getToken();
                localStorage = getState(token);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        context = browser.newContext(new Browser.NewContextOptions().setStorageState(localStorage));
        page = context.newPage();
    }

    private String getState(String jwtToken) {

        return "{\"cookies\":[],\"origins\":[{\"origin\":\"" +
                APP_URL +
                "\",\"localStorage\":[{\"name\":\"jwt\",\"value\":\"" + jwtToken + "\"}]}]}";
    }

}