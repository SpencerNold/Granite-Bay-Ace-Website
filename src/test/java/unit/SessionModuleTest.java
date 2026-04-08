package unit;

import com.granitebayace.site.DatabaseLayer;
import com.granitebayace.site.SessionManager;
import com.granitebayace.site.objects.Hashing;
import com.granitebayace.site.objects.Session;
import com.granitebayace.site.objects.UserData;
import org.junit.jupiter.api.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SessionModuleTest extends AbstractModuleUnitTester {

    private static final String USERNAME = "SessionModuleTestAccount";

    @BeforeAll
    public static void startServer() {
        startDummyServer();

        DatabaseLayer db = getDatabase();

        String salt = Hashing.generateSalt();
        String hash = Hashing.hashForStorage(salt, "12345");

        UserData data = new UserData(USERNAME, hash, salt, null, db.queryRole(1));
        db.insertUserData(data);
    }

    @Test
    @Order(1)
    public void isSessionInvalidUnitTest() {
        DatabaseLayer db = getDatabase();
        UserData data = db.queryUserData(USERNAME);
        Assertions.assertNotNull(data);
        Assertions.assertNull(data.session());
    }

    @Test
    @Order(2)
    public void createSessionUnitTest() {
        DatabaseLayer db = getDatabase();
        Session session = SessionManager.createSession(db, USERNAME);
        Assertions.assertNotNull(session);
        UserData data = db.queryUserData(USERNAME);
        Assertions.assertNotNull(data);
        Assertions.assertEquals(session.id(), data.session().id());
    }

    @Test
    @Order(3)
    public void isSessionValidUnitTest() {
        DatabaseLayer db = getDatabase();
        UserData data = db.queryUserData(USERNAME);
        Assertions.assertNotNull(data);
        Session session = data.session();
        Assertions.assertNotNull(session);
        Assertions.assertTrue(SessionManager.isSessionValid(db, session.id()));
    }

    @AfterAll
    public static void stopServer() {
        stopDummyServer();
        getDatabase().deleteUser(USERNAME);
    }

    private static DatabaseLayer getDatabase() {
        return server.service(DatabaseLayer.class);
    }
}
