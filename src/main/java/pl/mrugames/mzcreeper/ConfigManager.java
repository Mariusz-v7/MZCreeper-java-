package pl.mrugames.mzcreeper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;

@Component
public class ConfigManager {
    private final EnumSet<DayOfWeek> DAYS_WHEN_MATCHES_CAN_BE_PLANNED;
    private final int[] HOURS_ON_WHICH_MATCHES_ARE_PLANNED;
    private final DateTimeFormatter DATE_TIME_FORMATTER;
    private final EnumMap<DayOfWeek, String> tacticsForFriendlies = new EnumMap<>(DayOfWeek.class);

    @Autowired
    public ConfigManager(Environment environment) {
        DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(environment.getProperty("mz.date_time_format"));

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

        DAYS_WHEN_MATCHES_CAN_BE_PLANNED.forEach(day -> tacticsForFriendlies.put(day, environment.getProperty(String.format("friendly_tactics.%s", day))));
    }

    public EnumSet<DayOfWeek> getDaysWhenMatchesCanBePlayed() {
        return EnumSet.copyOf(DAYS_WHEN_MATCHES_CAN_BE_PLANNED);
    }

    public int[] getHoursOfFriendlyMatches() {
        return Arrays.copyOf(HOURS_ON_WHICH_MATCHES_ARE_PLANNED, HOURS_ON_WHICH_MATCHES_ARE_PLANNED.length);
    }

    public DateTimeFormatter getDateTimeFormatter() {
        return DATE_TIME_FORMATTER;
    }

    public EnumMap<DayOfWeek, String> getTacticsForFriendlies() {
        return tacticsForFriendlies;
    }
}
