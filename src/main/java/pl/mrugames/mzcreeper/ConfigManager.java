package pl.mrugames.mzcreeper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
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
    ConfigManager(@Value("${mz.date_time_format}") String dateTimeFormat,
                  @Value("${hours_of_matches_planned}") String hours,
                  @Value("${days_to_plan_matches}") String days,
                  ApplicationContext context) {
        DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(dateTimeFormat);

        String[] strHours = hours.split(",");
        String[] strDays = days.split(",");

        HOURS_ON_WHICH_MATCHES_ARE_PLANNED = new int[strHours.length];

        for (int i = 0; i < strHours.length; ++i) {
            HOURS_ON_WHICH_MATCHES_ARE_PLANNED[i] = Integer.parseInt(strHours[i]);
        }

        DayOfWeek[] dayOfWeeks = new DayOfWeek[strDays.length];
        for (int i = 0; i < strDays.length; ++i) {
            dayOfWeeks[i] = DayOfWeek.valueOf(strDays[i]);
        }

        DAYS_WHEN_MATCHES_CAN_BE_PLANNED = EnumSet.copyOf(Arrays.asList(dayOfWeeks));

        DAYS_WHEN_MATCHES_CAN_BE_PLANNED.forEach(day -> tacticsForFriendlies.put(day, context.getEnvironment().getProperty(String.format("friendly_tactics.%s", day))));
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
