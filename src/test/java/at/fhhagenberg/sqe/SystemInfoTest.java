package at.fhhagenberg.sqe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.Test;

public class SystemInfoTest {
    @Test
    public void testJavaVersion() {
        String javaVersion = SystemInfo.javaVersion();

        assertEquals("11", javaVersion.split("\\.")[0]);
    }

    @Test
    public void testJavafxVersion() {
        String javafxVersion = SystemInfo.javafxVersion();

        // if JavaFx has not started yet, the system property is not set
        assumeTrue(javafxVersion != null);

        assertEquals("11", javafxVersion.split("\\.")[0]);
    }
}