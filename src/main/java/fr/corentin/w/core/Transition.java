package fr.corentin.w.core;

/**
 * Represents a transition in the graph.
 * <p>
 * A transition consists of a target state and an output that is produced
 * when the transition is taken. This record is used to define the state
 * transition system for the W algorithm.
 * </p>
 *
 * @author Corentin
 * @version 1.0
 */
public record Transition(int nextState, char output) {
    // As this is a simple record with no additional methods or customized behavior,
    // the compiler automatically generates the accessors, equals, hashCode, and toString methods.
}