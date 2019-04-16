package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository {


    private JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO time_entries (project_id, user_id, date, hours) VALUES (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, timeEntry.getProjectId());
            preparedStatement.setLong(2, timeEntry.getUserId());
            preparedStatement.setDate(3, Date.valueOf(timeEntry.getDate()));
            preparedStatement.setInt(4, timeEntry.getHours());
            return preparedStatement;
        }, keyHolder);

        return find(keyHolder.getKey().longValue());
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        return jdbcTemplate.query("SELECT id,project_id,user_id,date,hours FROM time_entries WHERE id = ?;",
                Arrays.asList(timeEntryId).toArray(),
                extractor);
    }

    @Override
    public List<TimeEntry> list() {
        return jdbcTemplate.query("SELECT id, project_id, user_id, date, hours FROM time_entries", mapper);
    }

    @Override
    public TimeEntry update(long timeEntryId, TimeEntry timeEntry) {
        jdbcTemplate.update("UPDATE time_entries " +
                        "SET project_id = ?, user_id = ?, date = ?,  hours = ? " +
                        "WHERE id = ?",
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                Date.valueOf(timeEntry.getDate()),
                timeEntry.getHours(),
                timeEntryId);

        return find(timeEntryId);
    }

    @Override
    public void delete(long timeEntryId) {
        jdbcTemplate.update("DELETE FROM time_entries WHERE id = ?", timeEntryId);
    }

    private final RowMapper<TimeEntry> mapper = (rs, rowNum) -> new TimeEntry(
            rs.getLong("id"),
            rs.getLong("project_id"),
            rs.getLong("user_id"),
            rs.getDate("date").toLocalDate(),
            rs.getInt("hours")
    );

    private final ResultSetExtractor<TimeEntry> extractor =
            (rs) -> rs.next() ? mapper.mapRow(rs, 1) : null;
}
