package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private Map<Long, TimeEntry> timeEntries = new HashMap<>();
    private AtomicLong idSequence = new AtomicLong(1);

    public TimeEntry create(TimeEntry timeEntry) {
        timeEntry.setId(idSequence.getAndIncrement());
        timeEntries.put(timeEntry.getId(), timeEntry);
        return timeEntry;
    }

    public TimeEntry find(long id) {
        return timeEntries.get(id);
    }

    public List<TimeEntry> list() {
        return new ArrayList<>(timeEntries.values());
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
        TimeEntry toBeUpdated = timeEntries.get(id);
        if(toBeUpdated != null) {
            toBeUpdated.setDate(timeEntry.getDate());
            toBeUpdated.setHours(timeEntry.getHours());
            toBeUpdated.setProjectId(timeEntry.getProjectId());
            toBeUpdated.setUserId(timeEntry.getUserId());
        }
        return timeEntries.put(id, toBeUpdated);
    }

    public void delete(long id) {
        timeEntries.remove(id);
    }
}
