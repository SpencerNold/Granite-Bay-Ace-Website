package routes;

import com.google.gson.JsonArray;
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

public class AccountManagementAddTest extends AbstractRouteTest {

    private static final String MANAGER_USERNAME = "manager_test";
    private static final String MANAGER_PASSWORD = "ManagerPass1!";

    private static String adminSession;
    private static String managerSession;

    @BeforeAll
    public static void startServerAndPrepareUsers() {
        startDummyServer();

        DatabaseLayer db = getDatabase();

        // Admin session
        UserData admin = db.queryUserData("admin");
        assertNotNull(admin, "Expected default admin user to exist in dev_sqlite.db");
        assertNotNull(admin.session(), "Expected admin to already have a session in dev_sqlite.db");
        adminSession = admin.session().id();

        // Ensure a manager exists and has a valid session cookie for "manager cannot add accounts" test
        if (db.containsUserData(MANAGER_USERNAME)) {
            db.deleteUser(MANAGER_USERNAME);
        }

        Role managerRole = db.queryRole(1);
        assertNotNull(managerRole, "Expected manager role (id=1) to exist");

        String salt = Hashing.generateSalt();
        String passhash = Hashing.hashForStorage(salt, MANAGER_PASSWORD);
        db.insertUserData(new UserData(MANAGER_USERNAME, passhash, salt, (Session) null, managerRole));

        // Create a valid session for the manager by logging in via the same hashing/session flow as production
        Hashing.AuthResult mgrLogin = Hashing.login(db, MANAGER_USERNAME, MANAGER_PASSWORD, 0L);
        assertTrue(mgrLogin.ok(), "Expected manager_test to be able to login");
        managerSession = mgrLogin.session().id();

        assertNotNull(managerSession, "Expected manager session key to be created");
    }

    @AfterAll
    public static void stopServer() {
        stopDummyServer();
    }

    // Test 1: Admin can create a new manager account
    // Output: message = "ok"
    @Test
    public void testAdminCanCreateManagerAccount() {
        String newUsername = "new_manager_" + UUID.randomUUID();
        String newPassword = "NewManagerPass1!";

        HttpResponse response = sendRequest(
                "/api/accounts/add",
                Http.Method.POST,
                new HashMap<>(),
                cookieHeaders(adminSession),
                jsonBodyAdd(newUsername, newPassword)
        );

        assertEquals(200, response.getCode());

        JsonObject obj = response.getBodyFromJson(JsonObject.class);
        assertTrue(obj.has("message"));
        assertEquals("ok", obj.get("message").getAsString());
    }

    // Test 2: Newly created manager appears in the accounts list with roleId = 1
    // Output: Returned users[] contains <newUsername> with roleId = 1
    @Test
    public void testNewManagerAppearsInList() {
        String newUsername = "list_manager_" + UUID.randomUUID();
        String newPassword = "ListManagerPass1!";

        HttpResponse addResp = sendRequest(
                "/api/accounts/add",
                Http.Method.POST,
                new HashMap<>(),
                cookieHeaders(adminSession),
                jsonBodyAdd(newUsername, newPassword)
        );
        assertEquals(200, addResp.getCode());
        assertEquals("ok", addResp.getBodyFromJson(JsonObject.class).get("message").getAsString());

        HttpResponse listResp = sendRequest(
                "/api/accounts/list",
                Http.Method.POST,
                new HashMap<>(),
                cookieHeaders(adminSession),
                new JsonObject()
        );

        assertEquals(200, listResp.getCode());
        JsonObject listObj = listResp.getBodyFromJson(JsonObject.class);

        assertEquals("ok", listObj.get("message").getAsString());
        assertTrue(listObj.has("users"));
        JsonArray users = listObj.getAsJsonArray("users");

        boolean found = false;
        for (int i = 0; i < users.size(); i++) {
            JsonObject u = users.get(i).getAsJsonObject();
            if (newUsername.equals(u.get("username").getAsString())) {
                found = true;
                assertEquals(1, u.get("roleId").getAsInt(), "Expected newly created account to be manager (roleId=1)");
                break;
            }
        }
        assertTrue(found, "Expected new manager username to appear in /api/accounts/list");
    }

    // Test 3: Manager cannot create accounts
    // Output: message = "forbidden"
    @Test
    public void testManagerCannotCreateAccount() {
        String newUsername = "should_fail_" + UUID.randomUUID();
        String newPassword = "DoesNotMatter1!";

        HttpResponse response = sendRequest(
                "/api/accounts/add",
                Http.Method.POST,
                new HashMap<>(),
                cookieHeaders(managerSession),
                jsonBodyAdd(newUsername, newPassword)
        );

        assertEquals(200, response.getCode());

        JsonObject obj = response.getBodyFromJson(JsonObject.class);
        assertEquals("forbidden", obj.get("message").getAsString());
    }

    // Test 4: Creating an account with an existing username is rejected
    // Output: message = "user already exists"
    @Test
    public void testDuplicateUsernameRejected() {
        String dupUsername = "dup_manager_" + UUID.randomUUID();
        String password = "DupManagerPass1!";

        HttpResponse first = sendRequest(
                "/api/accounts/add",
                Http.Method.POST,
                new HashMap<>(),
                cookieHeaders(adminSession),
                jsonBodyAdd(dupUsername, password)
        );
        assertEquals(200, first.getCode());
        assertEquals("ok", first.getBodyFromJson(JsonObject.class).get("message").getAsString());

        HttpResponse second = sendRequest(
                "/api/accounts/add",
                Http.Method.POST,
                new HashMap<>(),
                cookieHeaders(adminSession),
                jsonBodyAdd(dupUsername, "AnotherPassword1!")
        );
        assertEquals(200, second.getCode());
        assertEquals("user already exists", second.getBodyFromJson(JsonObject.class).get("message").getAsString());
    }

    // Helper functions
    private static Map<String, String> cookieHeaders(String session) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Cookie", "session=" + session);
        headers.put("Content-Type", "application/json");
        return headers;
    }

    private static JsonObject jsonBodyAdd(String username, String password) {
        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        body.addProperty("password", password);
        return body;
    }

    private static DatabaseLayer getDatabase() {
        return server.service(DatabaseLayer.class);
    }
}