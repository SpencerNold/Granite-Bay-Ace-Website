package routes;

import com.google.gson.JsonObject;
import com.granitebayace.site.DatabaseLayer;
import com.granitebayace.site.objects.Hashing;
import com.granitebayace.site.objects.Role;
import com.granitebayace.site.objects.Session;
import com.granitebayace.site.objects.UserData;
import me.spencernold.kwaf.Http;
import me.spencernold.kwaf.http.HttpResponse;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class AccountManagementDeleteTest extends AbstractRouteTest {

    private static String adminSession;
    private static String managerSession;

    @BeforeAll
    public static void startServer() {
        // before all tests run, this starts up the http server
        // asynchronously
        startDummyServer();

        DatabaseLayer db = getDatabase();

        // Admin session
        UserData admin = db.queryUserData("admin");
        assertNotNull(admin, "Expected default admin user to exist in dev_sqlite.db");
        assertNotNull(admin.session(), "Expected admin to already have a session in dev_sqlite.db");
        adminSession = admin.session().id();

        // manager session
        String username = "manager_test";
        String password = "ManagerPass1!";

        if (db.containsUserData(username)) {
            db.deleteUser(username);
        }

        Role managerRole = db.queryRole(1);
        assertNotNull(managerRole, "Expected manager role (id=1) to exist");

        String salt = Hashing.generateSalt();
        String passhash = Hashing.hashForStorage(salt, password);
        db.insertUserData(new UserData(username, passhash, salt, (Session) null, managerRole));

        Hashing.AuthResult login = Hashing.login(db, username, password, 0L);
        assertTrue(login.ok());
        managerSession = login.session().id();

        assertNotNull(managerSession, "Expected manager session key to be created");
    }

    // Test for admin deleting a manager account using /api/accounts/delete
    // Creates a new manager account, deletes it, and should return successful
    @Test
    public void testAdminDeleteManager() {
        String username = "delete_" + UUID.randomUUID();
        String password = "DeletePass1!";

        HttpResponse addResponse = sendRequest("/api/accounts/add",
                Http.Method.POST,
                new HashMap<>(),
                cookieHeaders(adminSession),
                jsonBody(username, password)
        );
        assertEquals(200, addResponse.getCode());
        JsonObject addObj = addResponse.getBodyFromJson(JsonObject.class);
        assertEquals("ok", addObj.get("message").getAsString());

        HttpResponse response = sendRequest(
                "/api/accounts/delete",
                Http.Method.POST,
                new HashMap<>(),
                cookieHeaders(adminSession),
                jsonUsername(username)
        );

        assertEquals(200, response.getCode());
        JsonObject obj = response.getBodyFromJson(JsonObject.class);
        assertEquals("ok", obj.get("message").getAsString());
    }

    // Test for Admin deleting another admin account using /api/accounts/delete
    // Creates a new admin account, deletes it, and should return successful
    @Test
    public void testAdminDeleteAdmin() {
        String username = "admin_delete_" + UUID.randomUUID();
        String password = "DeletePass1!";

        createAdminAccount(username, password);

        HttpResponse response = sendRequest(
                "/api/accounts/delete",
                Http.Method.POST,
                new HashMap<>(),
                cookieHeaders(adminSession),
                jsonUsername(username)
        );
        assertEquals(200, response.getCode());
        JsonObject obj = response.getBodyFromJson(JsonObject.class);
        assertEquals("ok", obj.get("message").getAsString());
    }


    // Test for a Manager attempting to delete an account using /api/acccounts/delete
    // Attempts to delete an account, should be forbidden
    @Test
    public void testManagerDeleteAccount() {
        String username = "target_" + UUID.randomUUID();

        HttpResponse response = sendRequest(
                "/api/accounts/delete",
                Http.Method.POST,
                new HashMap<>(),
                cookieHeaders(managerSession),
                jsonUsername(username)
        );

        assertEquals(200, response.getCode());
        JsonObject obj = response.getBodyFromJson(JsonObject.class);
        assertEquals("forbidden", obj.get("message").getAsString());
    }

    // Test for an Admin attempting to delete their own account using /api/accounts/delete
    // Attempts to delete themselves, should not be allowed to
    @Test
    public void testAdminCannotDeleteSelf() {
        HttpResponse response = sendRequest(
                "/api/accounts/delete",
                Http.Method.POST,
                new HashMap<>(),
                cookieHeaders(adminSession),
                jsonUsername("admin")
        );

        assertEquals(200, response.getCode());
        JsonObject obj = response.getBodyFromJson(JsonObject.class);
        assertEquals("cannot delete yourself", obj.get("message").getAsString());
    }

    @AfterAll
    public static void stopServer() {
        // after all tests have been run,
        // this safely kills the server instance
        stopDummyServer();
    }

    // private helper methods
    private void createAdminAccount(String username, String password) {
        sendRequest("/api/accounts/add",
                Http.Method.POST,
                new HashMap<>(),
                cookieHeaders(adminSession),
                jsonBody(username, password)
        );

        JsonObject role = new JsonObject();
        role.addProperty("username", username);
        role.addProperty("roleId", 0);

        sendRequest("/api/accounts/role",
                Http.Method.POST,
                new HashMap<>(),
                cookieHeaders(adminSession),
                role
        );
    }

    private static Map<String, String> cookieHeaders(String session) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Cookie", "session=" + session);
        headers.put("Content-Type", "application/json");
        return headers;
    }

    private static DatabaseLayer getDatabase() {
        return server.service(DatabaseLayer.class);
    }

    private static JsonObject jsonBody(String username, String password) {
        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        body.addProperty("password", password);
        return body;
    }

    private static JsonObject jsonUsername(String username) {
        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        return body;
    }
}
