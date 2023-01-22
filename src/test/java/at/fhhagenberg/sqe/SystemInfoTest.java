package at.fhhagenberg.sqe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SystemInfoTest {
    @Test
    void testJavaVersion() {
        String javaVersion = SystemInfo.javaVersion();

        int actualVersion = Integer.parseInt(javaVersion.split("\\.")[0]);

        Assertions.assertTrue(11 <= actualVersion);
    }

    @Test
    void testJavafxVersion() {
        String javafxVersion = SystemInfo.javafxVersion();

        // if JavaFx has not started yet, the system property is not set
        assumeTrue(javafxVersion != null);

        assertEquals("11", javafxVersion.split("\\.")[0]);
    }
}