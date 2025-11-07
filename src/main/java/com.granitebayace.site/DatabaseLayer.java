package com.granitebayace.site;

import com.granitebayace.site.objects.Role;
import com.granitebayace.site.objects.Session;
import com.granitebayace.site.objects.UserData;
import me.spencernold.kwaf.database.Driver;
import me.spencernold.kwaf.database.impl.SQLiteDatabase;
import me.spencernold.kwaf.logger.Logger;
import me.spencernold.kwaf.services.Service;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;

@Service.Database(driver = Driver.Type.SQLITE, url = "jdbc:sqlite:dev_sqlite.db")
public class DatabaseLayer extends SQLiteDatabase {

    private final HashMap<Integer, Role> roleCacheMap = new HashMap<>();

    @Override
    public void open() {
        super.open();
        try {
            // Set to initial state, will not be in production since all of these will be in the cloud database already
            // is currently in development, as it ensures the environment is the same for each of the developers
            // without having the SQLite database sitting in the github repo
            enableForeignKeys();
            createSessionsTableSafe();
            createRolesTableSafe();
            createUserDataTableSafe();

            insertRole(new Role(0, "admin", 1));
            insertRole(new Role(1, "manager", 2));

            insertUserData(new UserData("admin", "12345", null, queryRole(0)));
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
        String query = "INSERT OR REPLACE INTO user_data (username, passhash, role_id, session_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setString(1, user.username());
            statement.setString(2, user.passhash());
            statement.setInt(3, user.role().id());
            Session session = user.session();
            if (session != null && session.id() != null)
                statement.setString(4, session.id());
            else
                statement.setNull(4, Types.VARCHAR);
            statement.executeUpdate();
        } catch (SQLException e) {
            error(e);
        }
    }

    public UserData queryUserData(String username) {
        String query = """
                SELECT
                    u.username,
                    u.passhash,
                    r.id AS role_id,
                    r.name AS role_name,
                    r.inheritance AS role_inheritance,
                    s.id AS session_id,
                    s.expiration AS session_expiration
                FROM user_data u
                JOIN roles r ON u.role_id = r.id
                LEFT JOIN sessions s ON u.session_id = s.id
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
                    String sessionId = result.getString("session_id");
                    String expiration = result.getString("session_expiration");
                    Session session = null;
                    if (sessionId != null && expiration != null)
                        session = new Session(sessionId, LocalDateTime.parse(expiration.replace(' ', 'T')));
                    return new UserData(username, passhash, session, role);
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

    public void insertSession(String username, Session session) {
        insertSessionToSessions(session);
        insertSessionToUserData(username, session.id());
    }

    private void insertSessionToUserData(String username, String id) {
        String query = "UPDATE user_data SET session_id = ? WHERE username = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setString(1, id);
            statement.setString(2, username);
            statement.executeUpdate();
        } catch (SQLException e) {
            error(e);
        }
    }

    private void insertSessionToSessions(Session session) {
        String query = "INSERT OR REPLACE INTO sessions (id, expiration) VALUES (?, ?)";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setString(1, session.id());
            statement.setString(2, session.expiration().toString().replace('T', ' ')); // store as "YYYY-MM-DD HH:MM:SS"
            statement.executeUpdate();
        } catch (SQLException e) {
            error(e);
        }
    }

    public Session querySession(String id) {
        String query = "SELECT id, expiration FROM sessions WHERE id = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setString(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    String expiration = result.getString("expiration");
                    LocalDateTime date = LocalDateTime.parse(expiration.replace(' ', 'T'));
                    return new Session(id, date);
                }
            }
        } catch (SQLException e) {
            error(e);
        }
        return null;
    }

    public boolean containsSession(String id) {
        String query = "SELECT 1 FROM sessions WHERE id = ? LIMIT 1";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setString(1, id);
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
                    session_id TEXT,
                    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
                    FOREIGN KEY (session_id) REFERENCES sessions(id)
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

    private void createSessionsTableSafe() throws SQLException {
        String query = """
                CREATE TABLE IF NOT EXISTS sessions (
                    id TEXT PRIMARY KEY,
                    expiration DATE NOT NULL
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
