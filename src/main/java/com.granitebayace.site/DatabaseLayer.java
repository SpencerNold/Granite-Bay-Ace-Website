package com.granitebayace.site;

import com.granitebayace.site.objects.Role;
import com.granitebayace.site.objects.UserData;
import me.spencernold.kwaf.database.Driver;
import me.spencernold.kwaf.database.impl.SQLiteDatabase;
import me.spencernold.kwaf.logger.Logger;
import me.spencernold.kwaf.services.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

@Service.Database(driver = Driver.Type.SQLITE, url = "jdbc:sqlite:dev_sqlite.db")
public class DatabaseLayer extends SQLiteDatabase {

    private final HashMap<Integer, Role> roleCacheMap = new HashMap<>();

    @Override
    public void open() {
        super.open();
        try {
            // Initialization
            enableForeignKeys();
            createRolesTableSafe();
            createUserDataTableSafe();

            insertRole(new Role(0, "admin", 1));
            insertRole(new Role(1, "manager", 2));
        } catch (SQLException e) {
            error(e);
        }
    }

    public void insertRole(Role role) {
        // INSERT OR IGNORE to not replace existing roles
        String query = "INSERT OR IGNORE INTO roles (id, name, inheritance) VALUES (?, ?, ?)";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setInt(1, role.id());
            statement.setString(2, role.name());
            statement.setInt(3, role.inheritance());
            statement.execute();
        } catch (SQLException e) {
            error(e);
        }
        roleCacheMap.put(role.id(), role);
    }

    public Role queryRole(int id) {
        if (roleCacheMap.containsKey(id))
            return roleCacheMap.get(id);
        String query = "SELECT id, name, inheritance FROM roles WHERE id = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    id = result.getInt("id");
                    String name = result.getString("name");
                    int inheritance = result.getInt("inheritance");
                    Role role = new Role(id, name, inheritance);
                    roleCacheMap.put(role.id(), role);
                    return role;
                }
            }
        } catch (SQLException e) {
            error(e);
        }
        return null;
    }

    public void insertUserData(UserData user) {
        String query = "INSERT OR REPLACE INTO user_data (username, passhash, role_id) VALUES (?, ?, ?)";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setString(1, user.username());
            statement.setString(2, user.passhash());
            statement.setInt(3, user.role().id());
            statement.executeUpdate();
        } catch (SQLException e) {
            error(e);
        }
    }

    public UserData queryUserData(String username) {
        String query = """
                SELECT u.username, u.passhash, r.id AS role_id, r.name AS role_name, r.inheritance AS role_inheritance
                FROM user_data u
                JOIN roles r ON u.role_id = r.id
                WHERE u.username = ?
                """;
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    username = result.getString("username");
                    String passhash = result.getString("passhash");
                    int roleId = result.getInt("role_id");
                    String roleName = result.getString("role_name");
                    int inheritance = result.getInt("role_inheritance");
                    Role role = new Role(roleId, roleName, inheritance);
                    return new UserData(username, passhash, role);
                }
            }
        } catch (SQLException e) {
            error(e);
        }
        return null;
    }

    public boolean containsUserData(String username) {
        String query = "SELECT 1 FROM user_data WHERE username = ? LIMIT 1";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        } catch (SQLException e) {
            error(e);
        }
        return false;
    }

    private void enableForeignKeys() throws SQLException {
        String query = "PRAGMA foreign_keys = ON";
        Statement statement = getConnection().createStatement();
        statement.execute(query);
        statement.close();
    }

    private void createUserDataTableSafe() throws SQLException {
        String query = """
                CREATE TABLE IF NOT EXISTS user_data (
                    username TEXT PRIMARY KEY,
                    passhash TEXT NOT NULL,
                    role_id INTEGER NOT NULL,
                    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
                )
                """;
        Statement statement = getConnection().createStatement();
        statement.execute(query);
        statement.close();
    }

    private void createRolesTableSafe() throws SQLException {
        String query = """
                CREATE TABLE IF NOT EXISTS roles (
                    id INTEGER PRIMARY KEY,
                    name TEXT NOT NULL UNIQUE,
                    inheritance INTEGER NOT NULL
                )
                """;
        Statement statement = getConnection().createStatement();
        statement.execute(query);
        statement.close();
    }

    private void error(Throwable e) {
        Logger.Companion.getSystemLogger().error(e.getMessage());
    }
}
