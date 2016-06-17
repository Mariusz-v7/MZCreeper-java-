package pl.mrugames.mzcreeper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.mrugames.mzcreeper.parsers.Parser;

import java.util.EnumSet;

@Component
public class TasksRunner {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ApplicationContext applicationContext;

    private final static EnumSet<Link> sendingInvitationsSequence = EnumSet.of(Link.AUTHENTICATION, Link.CHALLENGES, Link.FORUM, Link.PROFILE);
    private final static EnumSet<Link> settingTacticsSequence = EnumSet.of(Link.AUTHENTICATION, Link.INCOMING_MATCHES);

    @Autowired
    public TasksRunner(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Scheduled(fixedDelayString = "#{new Double(${sequences.invitation_sending_interval_hours} * 3600 * 1000).longValue()}")
    private void sendingInvitationsSequence() {
        logger.info("Executing invitations sending sequence");
        execute(sendingInvitationsSequence);
    }

    @Scheduled(fixedDelayString = "#{new Double(${sequences.set_tactics_interval_hours} * 3600 * 1000).longValue()}")
    private void setTactics() {
        logger.info("Executing setting tactics sequence");
        execute(settingTacticsSequence);
    }

    private synchronized void execute(EnumSet<Link> sequence) {
        try {
            sequence.stream()
                    .map(Link::getParser)
                    .map(applicationContext::getBean)
                    .forEach(Parser::parse);
        } catch (Exception e) {
            logger.warn("Failed to finish sequence", e);
        }
    }
}
