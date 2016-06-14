package pl.mrugames.mzcreeper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class FriendlyMatchesManager {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final static int MAX_MATCHES_IN_A_WEEK = 8;
    private List<LocalDateTime> plannedDates;
    private List<Long> possibleTargetsIds;

    public void setPlannedDates(List<LocalDateTime> plannedDates) {
        this.plannedDates = plannedDates;
    }

    public void setPossibleTargetsIds(List<Long> possibleTargetsIds) {
        this.possibleTargetsIds = possibleTargetsIds;
    }

    public int availableSlotsFromFriendlies() {
        if (plannedDates == null)
            throw new AssertionError("At first, please load your planned matches");

        return MAX_MATCHES_IN_A_WEEK - plannedDates.size();
    }
}
