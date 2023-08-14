package ch.zhaw.pm2.amongdigits.model;

import static java.util.Objects.requireNonNull;

import ch.zhaw.pm2.amongdigits.ChallengeType;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * The ChallengesModel class represents a model for Sudoku challenges. It provides methods to load
 * challenges of different types from their corresponding directories and store them in a map.
 */
public class ChallengesModel {

  private final Map<ChallengeType, List<File>> challenges = new EnumMap<>(ChallengeType.class);

  /**
   * Loads challenges of different types from their corresponding directories and stores them in the
   * map.
   */
  public void load() {
    Arrays.stream(ChallengeType.values())
        .forEach(
            challengeType -> challenges.put(challengeType, getSudokuChallenges(challengeType)));
  }

  /**
   * Returns the map that stores all the Sudoku challenges of different types.
   *
   * @return A map that stores challenges of different types.
   */
  public Map<ChallengeType, List<File>> getChallenges() {
    return challenges;
  }

  private List<File> getSudokuChallenges(final ChallengeType challengeType) {
    final File dir =
        new File(
            requireNonNull(getClass().getClassLoader().getResource(challengeType.getDirectory()))
                .getFile());
    if (!dir.isDirectory() || !dir.exists()) {
      return new ArrayList<>();
    }
    return Arrays.asList(requireNonNull(dir.listFiles()));
  }
}
