package pl.mrugames.mzcreeper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Component
public class FriendlyMatchesManager {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ConfigManager configManager;

    private List<LocalDateTime> plannedDates;
    private List<Long> possibleTargetsIds;

    @Autowired
    public FriendlyMatchesManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public void setPlannedDates(List<LocalDateTime> plannedDates) {
        this.plannedDates = plannedDates;
    }

    public void setPossibleTargetsIds(List<Long> possibleTargetsIds) {
        this.possibleTargetsIds = possibleTargetsIds;
    }

    public List<LocalDateTime> getDatesWithoutMatch() {
        return getDatesWithoutMatch(plannedDates);
    }

    public List<LocalDateTime> getDatesWithoutMatch(List<LocalDateTime> plannedDates) {
        if (plannedDates == null)
            throw new AssertionError("At first, please load your planned matches");

        List<LocalDateTime> dates = getAllPossibleDatesInWeek();
        dates.removeAll(plannedDates);

        return dates;
    }

    public void clear() {
        plannedDates = null;
        possibleTargetsIds = null;
    }

    public List<Long> getPossibleTargetsIds() {
        if (possibleTargetsIds == null)
            throw new AssertionError("At first, please load possible targets");

        return possibleTargetsIds;
    }

    private List<LocalDateTime> getAllPossibleDatesInWeek() {
        List<LocalDateTime> dates = new LinkedList<>();

        configManager.getDaysWhenMatchesCanBePlayed().stream()
                .filter(day -> LocalDateTime.now().getDayOfWeek() != day)
                .forEach(day -> {
            Arrays.stream(configManager.getHoursOfFriendlyMatches())
                    .forEach(hour -> dates.add(this.nextWeekDay(day, hour)));
        });

        return dates;
    }

    private LocalDateTime nextWeekDay(DayOfWeek day, int hour) {
         return LocalDateTime.now().with(TemporalAdjusters.next(day)).withHour(hour).withMinute(0).withSecond(0).withNano(0);
    }
}
