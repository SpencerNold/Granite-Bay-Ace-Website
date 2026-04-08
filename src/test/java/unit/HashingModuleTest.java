package unit;

import com.granitebayace.site.objects.Hashing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class HashingModuleTest {

    private static final int SALT_CHECK_COUNT = 128;

    @Test
    public void hashForStorageUnitTest() {
        String salt = "VG90YWxseVJhbmRvbVNhbHRIZXJlIQ==";
        String password = "TotallySecurePasswordHere!";
        String hash = Hashing.hashForStorage(salt, password);
        Assertions.assertEquals("qnDftewZzgVQNfHyfvsBKfXWQ7QeQJiTXeJgESFUasw=", hash);
    }

    @Test
    public void randomSaltUnitTest() {
        String[] array = new String[SALT_CHECK_COUNT];
        for (int i = 0; i < SALT_CHECK_COUNT; i++)
            array[i] = Hashing.generateSalt();
        // Checks for salt collisions to size n, where n is SALT_CHECK_COUNT
        // SALT_CHECK_COUNT can be set to however many iterations you want to check
        // This lazy algorithm is in T(n) = Theta(n^2) time
        // The "operation" is very expensive
        // PLEASE FOR THE LOVE OF ALL THINGS GOOD IN THIS WORLD don't make SALT_CHECK_COUNT too large
        for (int i = 0; i < (array.length - 1); i++) {
            for (int j = (i + 1); j < array.length; j++) {
                Assertions.assertNotEquals(array[i], array[j]);
            }
        }
    }

    @Test
    public void constantTimeEqualsUnitTest() {
        // Reflection as constantTimeEquals(Ljava/lang/String;Ljava/lang/String;)Z is private
        // I'm not going to expose it just for a unit test
        try {
            Method method = Hashing.class.getDeclaredMethod("constantTimeEquals", String.class, String.class);
            method.setAccessible(true);

            Object result = method.invoke(null, "TestValueA", "TestValueA");
            Assertions.assertInstanceOf(Boolean.class, result);
            Assertions.assertTrue((Boolean) result);

            result = method.invoke(null, "TestValueA", "TestValueB");
            Assertions.assertInstanceOf(Boolean.class, result);
            Assertions.assertFalse((Boolean) result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
