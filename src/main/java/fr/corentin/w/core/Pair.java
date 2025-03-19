package fr.corentin.w.core;

/**
 * Utility class to represent a pair of values.
 * <p>
 * This record is used throughout the W algorithm to represent input/output pairs
 * and other paired values. It provides a convenient way to group two related
 * values together.
 * </p>
 *
 * @param <K> The type of the first element in the pair
 * @param <V> The type of the second element in the pair
 *
 * @author Corentin
 * @version 1.0
 */
public record Pair<K, V>(K first, V second) {

    /**
     * Returns a string representation of this pair.
     * <p>
     * The string representation is in the format "first/second".
     * This is particularly useful for representing input/output pairs
     * in the W algorithm.
     * </p>
     *
     * @return A string representation of this pair
     */
    @Override
    public String toString() {
        return first + "/" + second;
    }
}