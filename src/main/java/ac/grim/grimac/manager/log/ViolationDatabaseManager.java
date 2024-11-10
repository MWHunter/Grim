package ac.grim.grimac.manager.log;

import ac.grim.grimac.manager.init.Initable;
import ac.grim.grimac.player.GrimPlayer;
import io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class ViolationDatabaseManager implements Initable {

    private final Plugin plugin;

    private Connection openConnection;

    public ViolationDatabaseManager(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void start() {
        try {
            // Init sqlite
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not load SQLite driver", e);
        }
        try (
                Connection connection = getConnection();
                PreparedStatement createTable = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS violations(" +
                                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                                "uuid CHARACTER(36) NOT NULL, " +
                                "check_name TEXT NOT NULL, " +
                                "verbose TEXT NOT NULL, " +
                                "vl TEXT NOT NULL, " +
                                "created_at BIGINT NOT NULL" +
                                ")"
                );
        ) {
            createTable.execute();

            PreparedStatement createIndex = connection.prepareStatement(
                    "CREATE INDEX IF NOT EXISTS idx_violations_uuid ON violations(uuid)"
            );
            createIndex.execute();
            createIndex.close();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "Failed to generate violations database:", ex);
        }
    }

    public void logAlert(GrimPlayer player, String verbose, String checkName, String violations) {
        FoliaScheduler.getAsyncScheduler().runNow(plugin, __ -> logAlertSync(player, verbose, checkName, violations));
    }

    public synchronized void logAlertSync(GrimPlayer player, String verbose, String checkName, String violations) {
        try (
                Connection connection = getConnection();
                PreparedStatement insertLog = connection.prepareStatement(
                        "INSERT INTO violations (uuid, check_name, verbose, vl, created_at) VALUES (?, ?, ?, ?, ?)"
                )
        ) {
            insertLog.setString(1, player.getUniqueId().toString());
            insertLog.setString(2, verbose);
            insertLog.setString(3, checkName);
            insertLog.setString(4, violations);
            insertLog.setLong(5, System.currentTimeMillis());

            insertLog.executeUpdate();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "Failed to insert violation:", ex);
        }
    }

    public int getLogCount(UUID player) {
        try (
                Connection connection = getConnection();
                PreparedStatement fetchLogs = connection.prepareStatement(
                        "SELECT COUNT(*) FROM violations WHERE uuid = ?"
                )
        ) {
            fetchLogs.setString(1, player.toString());
            ResultSet resultSet = fetchLogs.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "Failed to fetch number of violations:", ex);
        }
        return 0;
    }

    public List<Violation> getViolations(UUID player, int page, int limit) {
        List<Violation> violations = new ArrayList<>();
        try (
                Connection connection = getConnection();
                PreparedStatement fetchLogs = connection.prepareStatement(
                        "SELECT check_name, verbose, vl, created_at FROM violations" +
                        " WHERE uuid = ? ORDER BY created_at DESC LIMIT ? OFFSET ?"
                )
        ) {
            fetchLogs.setString(1, player.toString());
            fetchLogs.setInt(2, limit);
            fetchLogs.setInt(3, (page - 1) * limit);

            ResultSet resultSet = fetchLogs.executeQuery();
            while (resultSet.next()) {
                String checkName = resultSet.getString("check_name");
                String verbose = resultSet.getString("verbose");
                String vl = resultSet.getString("vl");
                Date createdAt = new Date(resultSet.getLong("created_at"));

                violations.add(new Violation(UUID.randomUUID(), checkName, verbose, vl, createdAt));
            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "Failed to fetch violations:", ex);
        }

        return violations;
    }


    protected synchronized Connection getConnection() throws SQLException {
        if (openConnection == null || openConnection.isClosed()) {
            openConnection = openConnection();
        }
        return openConnection;
    }

    protected Connection openConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder().getAbsolutePath() + File.separator + "violations.sqlite");
    }

}
