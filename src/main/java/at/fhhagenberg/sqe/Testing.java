package at.fhhagenberg.sqe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Testing {
    public static void main(String[] args) {
        final Logger log = LoggerFactory.getLogger("Test");
        if (args.length < 1) {
            log.info("Usage PROGRAM host:port [-d]");
            return;
        }
        for(String s : args) {
            log.info(s);
            log.debug(s);
        }
    }
}
