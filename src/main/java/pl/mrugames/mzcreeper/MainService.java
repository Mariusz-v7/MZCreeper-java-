package pl.mrugames.mzcreeper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MainService implements pl.mrugames.common.MainService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public void main(String[] strings) {
        logger.info("MZCreeper is starting");

        logger.info("MZCreeper finished");
    }
}
