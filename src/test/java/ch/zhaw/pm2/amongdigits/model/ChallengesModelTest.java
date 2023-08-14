package ch.zhaw.pm2.amongdigits.model;

import static ch.zhaw.pm2.amongdigits.ChallengeType.PRE_GENERATED;
import static ch.zhaw.pm2.amongdigits.ChallengeType.USER_GENERATED;
import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.zhaw.pm2.amongdigits.ChallengeType;
import java.io.File;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the ChallengesModel class.
 *
 * <p>The ChallengesModelTest class contains unit tests to verify the functionality of the
 * ChallengesModel class. It tests the loading of challenges and verifies that the loaded challenges
 * match the expected challenges.
 */
class ChallengesModelTest {

  private ChallengesModel model;

  /** Sets up the test environment before each test method is executed. */
  @BeforeEach
  void setUp() {
    model = new ChallengesModel();
  }

  /**
   * Tests the loading of challenges and verifies that the loaded challenges match the expected
   * challenges.
   */
  @Test
  void testLoad() {
    model.load();
    final Map<ChallengeType, List<File>> challenges = model.getChallenges();
    final List<File> preGeneratedChallenges = challenges.get(PRE_GENERATED);
    final List<File> userGeneratedChallenges = challenges.get(USER_GENERATED);
    final File preGeneratedDir = getDirOf(PRE_GENERATED);
    final File userGeneratedDir = getDirOf(USER_GENERATED);
    assertEquals(asList(requireNonNull(preGeneratedDir.listFiles())), preGeneratedChallenges);
    assertEquals(asList(requireNonNull(userGeneratedDir.listFiles())), userGeneratedChallenges);
  }

  private File getDirOf(ChallengeType preGenerated) {
    return new File(
        requireNonNull(getClass().getClassLoader().getResource(preGenerated.getDirectory()))
            .getFile());
  }
}
