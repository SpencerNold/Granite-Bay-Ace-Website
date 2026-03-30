package routes;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.granitebayace.site.DatabaseLayer;
import com.google.gson.JsonObject;
import me.spencernold.kwaf.Http;
import me.spencernold.kwaf.http.HttpResponse;
import me.spencernold.kwaf.Resource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRoleIDTest extends AbstractRouteTest {


    @BeforeAll
    public static void startServer() {
        // before all tests run, this starts up the http server
        // asynchronously
        startDummyServer();
    }

    @Test
    public void testCorrectPasswordLogin() {
        // Integration test for correct credentials to the /api/login route
        // sends an HTTP POST request and asserts on the response, "Is this what I want coming back?"
        JsonObject body = new JsonObject();
        body.addProperty("username", "admin");
        body.addProperty("password", "WiderSacramentoAces37!");
        HttpResponse response = sendRequest("/api/login", Http.Method.POST, new HashMap<>(), new HashMap<>(), body);
        assertEquals(200, response.getCode());
        JsonObject object = response.getBodyFromJson(JsonObject.class);
        assertTrue(object.has("username"));
        assertTrue(object.has("key"));
        assertTrue(object.has("message"));
        assertEquals("ok", object.get("message").getAsString());


        String username = object.get("username").getAsString();
        assertEquals("admin", username);

        DatabaseLayer db = getDatabase();

        Integer roleId = db.getUserRoleId(username);
        assertNotNull(roleId);
        assertEquals(0, roleId);
    }

    @AfterAll
    public static void stopServer() {
        // after all tests have been run,
        // this safely kills the server instance
        stopDummyServer();
    }

    private DatabaseLayer getDatabase() {
        return server.service(DatabaseLayer.class);
    }

}