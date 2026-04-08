package unit;

import com.granitebayace.site.DatabaseLayer;
import com.granitebayace.site.objects.Hashing;
import com.granitebayace.site.objects.Role;
import com.granitebayace.site.objects.UserData;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;

public class DatabaseLayerModuleTest extends AbstractModuleUnitTester {

    @BeforeAll
    public static void startServer() {
        startDummyServer();
    }

    @Test
    public void roleUnitTest() {
        DatabaseLayer db = getDatabase();
        // Test Insert of Role
        Role r1 = new Role(2, "testRole", 3);
        db.insertRole(r1);

        // Test Query of Role
        Role r2 = db.queryRole(2);
        Assertions.assertEquals(r1.id(), r2.id());
        Assertions.assertEquals(r1.name(), r2.name());
        Assertions.assertEquals(r1.inheritance(), r2.inheritance());

        // Cleanup of Role
        // Remove from DB
        String query = "DELETE FROM roles WHERE id = 2";
        try {
            Statement statement = db.getConnection().createStatement();
            statement.execute(query);
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Remove from cache
        try {
            Field field = DatabaseLayer.class.getDeclaredField("roleCacheMap");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<Integer, Role> cache = (HashMap<Integer, Role>) field.get(db);
            cache.remove(2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Make sure it deleted
        r2 = db.queryRole(2);
        Assertions.assertNull(r2);
    }

    @Test
    public void userDataUnitTest() {
        DatabaseLayer db = getDatabase();
        // Init User Data
        String username = "ud_" + UUID.randomUUID();
        String password = "TestPasswordHere";
        String salt = Hashing.generateSalt();
        String hash = Hashing.hashForStorage(salt, password);
        UserData data = new UserData(username, hash, salt, null, db.queryRole(0));

        // Test Insert of UserData
        db.insertUserData(data);

        // Test Query of UserData
        UserData d2 = db.queryUserData(username);
        Assertions.assertEquals(data.username(), d2.username());
        Assertions.assertEquals(data.passhash(), d2.passhash());
        Assertions.assertEquals(data.salt(), d2.salt());
        Assertions.assertEquals(data.role().id(), d2.role().id());

        // Test Delete of UserData
        db.deleteUser(username);
        d2 = db.queryUserData(username);
        Assertions.assertNull(d2);
    }

    @AfterAll
    public static void stopServer() {
        stopDummyServer();
    }

    private static DatabaseLayer getDatabase() {
        return server.service(DatabaseLayer.class);
    }
}
