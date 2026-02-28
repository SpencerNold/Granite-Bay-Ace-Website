package routes;

import com.granitebayace.site.DatabaseLayer;
import com.granitebayace.site.objects.UserData;
import me.spencernold.kwaf.Http;
import me.spencernold.kwaf.Resource;
import me.spencernold.kwaf.http.HttpResponse;
import me.spencernold.kwaf.util.InputStreams;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

// Integration tests here follow the same principle as the LoginControllerTest.java class, see there for more info
public class SecurePageControllerTest extends AbstractRouteTest {

    @BeforeAll
    public static void startServer() {
        startDummyServer();
    }

    @Test
    public void testAdminPageValid() {
        testPageValid("/admin", "pages/admin.html");
    }

    @Test
    public void testAdminPageInvalid() {
        testPageInvalid("/admin");
    }

    @Test
    public void testRolesPageValid() {
        testPageValid("/roles", "pages/roles.html");
    }

    @Test
    public void testRolesPageInvalid() {
        testPageInvalid("/roles");
    }

    @Test
    public void testManageAccountValid() {
        testPageValid("/manage-account", "pages/manage-account.html");
    }

    @Test
    public void testManageAccountInvalid() {
        testPageInvalid("/manage-account");
    }

    private void testPageValid(String path, String expected) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Cookie", String.format("session=%s", getValidSessionKey()));
        HttpResponse response = sendRequest(path, Http.Method.GET, new HashMap<>(), headers, null);
        assertEquals(200, response.getCode());
        assertIsPage(response.getBody(), expected);
    }

    private void testPageInvalid(String path) {
        HttpResponse response = sendRequest(path, Http.Method.GET, new HashMap<>(), new HashMap<>(), null);
        assertEquals(200, response.getCode());
        assertIsPage(response.getBody(), "pages/redirect.html");
    }

    @AfterAll
    public static void stopServer() {
        stopDummyServer();
    }

    private void assertIsPage(byte[] body, String page) {
        try {
            InputStream resource = Resource.Companion.get(page);
            assertNotNull(resource);
            byte[] bytes = InputStreams.Companion.readAllBytes(resource);
            assertArrayEquals(bytes, body);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getValidSessionKey() {
        DatabaseLayer db = getDatabase();
        UserData user = db.queryUserData("admin");
        return user.session().id();
    }

    private DatabaseLayer getDatabase() {
        return server.service(DatabaseLayer.class);
    }
}
