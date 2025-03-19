package fr.corentin.w.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a node in the discrimination tree produced by the W algorithm.
 * <p>
 * Each node contains a list of states and, if not a terminal node, an input/output pair
 * that is used to discriminate those states. The node also maintains a map of child nodes
 * grouped by their response to the input/output pair.
 * </p>
 *
 * @author Corentin
 * @version 1.0
 * @see Pair
 */
public class Node {
    /** The list of states represented by this node. */
    private final List<Integer> states;

    /** The input/output pair used to discriminate states at this node, null for terminal nodes. */
    private final Pair<Character, Character> inputOutputPair;

    /** Map of child nodes grouped by their response to the input/output pair. */
    private final Map<String, Node> groups;

    /** Flag indicating whether this is a terminal (leaf) node. */
    private final boolean terminal;

    /**
     * Constructs a new Node with the specified states, input/output pair, and terminal status.
     *
     * @param states The list of states represented by this node
     * @param inputOutputPair The input/output pair used for discrimination, null for terminal nodes
     * @param terminal True if this is a terminal (leaf) node, false otherwise
     */
    public Node(List<Integer> states, Pair<Character, Character> inputOutputPair, boolean terminal) {
        this.states = states;
        this.inputOutputPair = inputOutputPair;
        this.groups = new HashMap<>();
        this.terminal = terminal;
    }

    /**
     * Returns the list of states represented by this node.
     *
     * @return The list of states
     */
    public List<Integer> getStates() {
        return states;
    }

    /**
     * Returns the input/output pair used for discrimination at this node.
     *
     * @return The input/output pair, or null if this is a terminal node
     */
    public Pair<Character, Character> getInputOutputPair() {
        return inputOutputPair;
    }

    /**
     * Returns the map of child nodes grouped by their response to the input/output pair.
     *
     * @return The map of groups to child nodes
     */
    public Map<String, Node> getGroups() {
        return groups;
    }

    /**
     * Checks if this node is a terminal (leaf) node.
     *
     * @return True if this is a terminal node, false otherwise
     */
    public boolean isTerminal() {
        return terminal;
    }

    /**
     * Adds a child node with the specified group label.
     *
     * @param label The label identifying the group (response to input/output pair)
     * @param node The child node to add
     */
    public void addGroup(String label, Node node) {
        groups.put(label, node);
    }
}