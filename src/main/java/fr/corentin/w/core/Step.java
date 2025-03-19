package fr.corentin.w.core;

import java.util.List;
import java.util.Map;

/**
 * Represents a step in the discrimination algorithm.
 * <p>
 * Each step captures a decision point in the W algorithm where a group of states
 * is partitioned based on a specific input/output pair. This record stores the
 * information about the starting group of states, the input/output pair used for
 * discrimination, and the resulting groups after the discrimination is applied.
 * </p>
 *
 * @author Corentin
 * @version 1.0
 * @see Pair
 */
public record Step(
        List<Integer> startingGroup,
        Pair<Character, Character> inputOutputPair,
        Map<String, List<Integer>> resultingGroups
) {
    // As this is a simple record with no additional methods or customized behavior,
    // the compiler automatically generates the accessors, equals, hashCode, and toString methods.
}