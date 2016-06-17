package pl.mrugames.mzcreeper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

@Component
public class FriendlyMatchesManager {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final EnumSet<DayOfWeek> DAYS_WHEN_MATCHES_CAN_BE_PLANNED;
    private final int[] HOURS_ON_WHICH_MATCHES_ARE_PLANNED;

    private List<LocalDateTime> plannedDates;
    private List<Long> possibleTargetsIds;

    @Autowired
    public FriendlyMatchesManager(Environment environment) {
        String[] strHours = environment.getProperty("hours_of_matches_planned").split(",");
        String[] strDays = environment.getProperty("days_to_plan_matches").split(",");

        HOURS_ON_WHICH_MATCHES_ARE_PLANNED = new int[strHours.length];

        for (int i = 0; i < strHours.length; ++i) {
            HOURS_ON_WHICH_MATCHES_ARE_PLANNED[i] = Integer.parseInt(strHours[i]);
        }

        DayOfWeek[] dayOfWeeks = new DayOfWeek[strDays.length];
        for (int i = 0; i < strDays.length; ++i) {
            dayOfWeeks[i] = DayOfWeek.valueOf(strDays[i]);
        }

        DAYS_WHEN_MATCHES_CAN_BE_PLANNED = EnumSet.copyOf(Arrays.asList(dayOfWeeks));
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

    public int[] getHoursOnWhichMatchesArePlanned() {
        return HOURS_ON_WHICH_MATCHES_ARE_PLANNED;
    }

    private List<LocalDateTime> getAllPossibleDatesInWeek() {
        List<LocalDateTime> dates = new LinkedList<>();

        DAYS_WHEN_MATCHES_CAN_BE_PLANNED.stream()
                .filter(day -> LocalDateTime.now().getDayOfWeek() != day)
                .forEach(day -> {
            Arrays.stream(HOURS_ON_WHICH_MATCHES_ARE_PLANNED)
                    .forEach(hour -> dates.add(this.nextWeekDay(day, hour)));
        });

        return dates;
    }

    private LocalDateTime nextWeekDay(DayOfWeek day, int hour) {
         return LocalDateTime.now().with(TemporalAdjusters.next(day)).withHour(hour).withMinute(0).withSecond(0).withNano(0);
    }
}
