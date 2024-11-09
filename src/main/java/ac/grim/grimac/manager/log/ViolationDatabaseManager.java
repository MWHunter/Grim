package ac.grim.grimac.manager.log;

import ac.grim.grimac.manager.init.Initable;
import ac.grim.grimac.player.GrimPlayer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
            //Init sqlite
            Class.forName("org.sqlite.JDBC");
        } catch(ClassNotFoundException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not load SQLite driver", e);
        }
        try(
                Connection connection = getConnection();
                PreparedStatement createTable = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS violations(" +
                                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                                "uuid CHARACTER(36) NOT NULL, " +
                                "check_name TEXT NOT NULL, " +
                                "verbose TEXT NOT NULL, " +
                                "vl TEXT NOT NULL, " +
                                "created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP" +
                                ")"
                );
        ) {
            createTable.execute();

            PreparedStatement createIndex = connection.prepareStatement(
                    "CREATE INDEX IF NOT EXISTS idx_violations_uuid ON violations(uuid)"
            );
            createIndex.execute();
        } catch(SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "Failed to generate violations database:", ex);
        }
    }

    public void logAlert(GrimPlayer player, String verbose, String checkName, String violations) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try(
                    Connection connection = getConnection();
                    PreparedStatement insertLog = connection.prepareStatement(
                            "INSERT INTO violations (uuid, check_name, verbose, vl) VALUES (?, ?, ?, ?)"
                    )
            ) {
                insertLog.setString(1, player.getUniqueId().toString());
                insertLog.setString(2, verbose);
                insertLog.setString(3, checkName);
                insertLog.setString(4, violations);

                insertLog.executeUpdate();
            } catch(SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, "Failed to insert violation:", ex);
            }
        });
    }


    protected Connection getConnection() throws SQLException {
        if(openConnection == null || openConnection.isClosed()) {
            openConnection = openConnection();
        }
        return openConnection;
    }

    protected Connection openConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder().getAbsolutePath() + File.separator + "violations.sqlite");
    }

}
