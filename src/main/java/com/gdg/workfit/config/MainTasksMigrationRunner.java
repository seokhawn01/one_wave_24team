package com.gdg.workfit.config;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class MainTasksMigrationRunner {

    @Bean
    ApplicationRunner mainTasksMigration(
            @Value("${app.migration.enabled:false}") boolean enabled,
            @Value("${app.migration.drop-legacy-main-tasks:false}") boolean dropLegacy,
            JdbcTemplate jdbcTemplate
    ) {
        return args -> {
            if (!enabled) {
                return;
            }

            String dbName = jdbcTemplate.queryForObject("SELECT DATABASE()", String.class);
            if (dbName == null || dbName.isBlank()) {
                return;
            }

            boolean hasLegacyColumn = Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) > 0 FROM INFORMATION_SCHEMA.COLUMNS " +
                            "WHERE TABLE_SCHEMA = ? AND TABLE_NAME = 'job_posts' AND COLUMN_NAME = 'main_tasks'",
                    Boolean.class,
                    dbName
            ));

            if (!hasLegacyColumn) {
                return;
            }

            boolean hasTasksTable = Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) > 0 FROM INFORMATION_SCHEMA.TABLES " +
                            "WHERE TABLE_SCHEMA = ? AND TABLE_NAME = 'job_post_main_tasks'",
                    Boolean.class,
                    dbName
            ));

            if (!hasTasksTable) {
                return;
            }

            List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                    "SELECT id, main_tasks FROM job_posts WHERE main_tasks IS NOT NULL AND main_tasks <> ''"
            );

            for (Map<String, Object> row : rows) {
                Long jobPostId = ((Number) row.get("id")).longValue();
                String legacy = String.valueOf(row.get("main_tasks"));

                Integer existing = jdbcTemplate.queryForObject(
                        "SELECT COUNT(*) FROM job_post_main_tasks WHERE job_post_id = ?",
                        Integer.class,
                        jobPostId
                );

                if (existing != null && existing > 0) {
                    continue;
                }

                List<String> parts = Arrays.stream(legacy.replace("\r", "\n").split("[\\n,|]"))
                        .map(String::trim)
                        .filter(item -> !item.isEmpty())
                        .toList();

                for (String task : parts) {
                    jdbcTemplate.update(
                            "INSERT INTO job_post_main_tasks (job_post_id, task) VALUES (?, ?)",
                            jobPostId,
                            task
                    );
                }
            }

            if (!dropLegacy) {
                return;
            }

            boolean stillHasLegacy = Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) > 0 FROM INFORMATION_SCHEMA.COLUMNS " +
                            "WHERE TABLE_SCHEMA = ? AND TABLE_NAME = 'job_posts' AND COLUMN_NAME = 'main_tasks'",
                    Boolean.class,
                    dbName
            ));

            if (stillHasLegacy) {
                jdbcTemplate.execute("ALTER TABLE job_posts DROP COLUMN main_tasks");
            }
        };
    }
}
