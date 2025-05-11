package gtp.hms.util;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSeeder {
    public static void seedDatabase() {
        System.out.println("Starting database seeding process...");

        // First try classpath resource (works in JAR and IDE)
        try (InputStream is = DatabaseSeeder.class.getClassLoader()
                .getResourceAsStream("db/seed_data.sql")) {

            if (is == null) {
                // Fallback to direct filesystem path for development
                System.out.println("Classpath resource not found, trying filesystem...");
                tryFilesystemPath();
                return;
            }

            System.out.println("Found seed file in classpath, proceeding...");
            String sqlScript = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            executeSqlScript(sqlScript);

        } catch (Exception e) {
            System.err.println("Failed to seed database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void tryFilesystemPath() throws Exception {
        Path devPath = Paths.get("src/main/resources/db/seed_data.sql");
        if (Files.exists(devPath)) {
            System.out.println("Found seed file at: " + devPath.toAbsolutePath());
            String sqlScript = Files.readString(devPath, StandardCharsets.UTF_8);
            executeSqlScript(sqlScript);
        } else {
            // Try target directory directly
            Path targetPath = Paths.get("target/classes/db/seed_data.sql");
            if (Files.exists(targetPath)) {
                System.out.println("Found seed file at: " + targetPath.toAbsolutePath());
                String sqlScript = Files.readString(targetPath, StandardCharsets.UTF_8);
                executeSqlScript(sqlScript);
            } else {
                throw new RuntimeException("Seed file not found in any location");
            }
        }
    }

    private static void executeSqlScript(String sqlScript) throws SQLException {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {

            // Split and execute each statement
            String[] statements = sqlScript.split(";(?=\\s*[A-Z])");
            for (String statement : statements) {
                if (!statement.trim().isEmpty()) {
                    stmt.execute(statement);
                }
            }
            System.out.println("✅ Database seeded successfully!");
        }
    }
}