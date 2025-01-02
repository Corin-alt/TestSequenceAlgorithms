package fr.corentin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Classe StateMachine - Implémente une machine à états qui analyse des séquences d'entrée
 * et génère toutes les séquences de sortie possibles selon les règles suivantes :
 * - Chaque état peut avoir plusieurs transitions possibles
 * - Une transition est définie par un état de départ, un état d'arrivée, un caractère d'entrée et un caractère de sortie
 * - L'algorithme doit gérer les cas de doublements (deux caractères traités ensemble)
 * - Les doublements peuvent être forcés (caractères identiques) ou optionnels (caractères différents)
 */
class StateMachine {

    // Constantes pour la mise en forme de l'affichage console
    private static final String GREEN = "\u001B[32m";   // Couleur verte pour les succès
    private static final String RED = "\u001B[31m";     // Couleur rouge pour les échecs
    private static final String BLUE = "\u001B[34m";    // Couleur bleue pour les transitions
    private static final String RESET = "\u001B[0m";    // Réinitialisation des couleurs
    private static final String CHECK = GREEN + "✓" + RESET;  // Symbole pour succès (✓ vert)
    private static final String CROSS = RED + "✗" + RESET;    // Symbole pour échec (✗ rouge)
    private static final String ARROW = BLUE + "→" + RESET;   // Symbole pour transition (→ bleu)

    /**
     * Méthode centrale d'exploration récursive du graphe d'états.
     * Cette méthode implémente le cœur de l'algorithme qui cherche toutes les solutions possibles.
     * Elle gère quatre cas principaux :
     * 1. Fin de séquence (solution trouvée)
     * 2. Doublement forcé pour caractères identiques
     * 3. Doublement optionnel pour caractères différents (chemin direct ou alternatif)
     * 4. Transition simple d'un seul caractère
     *
     * @param graph          Le graphe représentant la machine à états avec toutes ses transitions possibles
     * @param currentState   Identifiant numérique de l'état actuel dans l'exploration
     * @param remainingInput Chaîne de caractères restant à analyser dans la séquence d'entrée
     * @param currentOutput  Chaîne de sortie construite jusqu'à présent dans ce chemin d'exploration
     * @param solutions      Collection qui accumule toutes les séquences de sortie valides trouvées
     * @param path           Chaîne représentant le chemin parcouru (pour le suivi et le débogage)
     */
    private static void explore(Graph graph, int currentState, String remainingInput,
                                String currentOutput, List<String> solutions, String path) {

        // CAS 1 : Plus de caractères à traiter = solution trouvée
        // On ajoute la séquence de sortie générée à la liste des solutions
        if (remainingInput.isEmpty()) {
            solutions.add(currentOutput);
            return;
        }

        // Extraction du prochain caractère à traiter
        char currentInput = remainingInput.charAt(0);

        // Recherche de toutes les transitions possibles depuis l'état courant
        // qui acceptent le caractère d'entrée actuel
        List<Transition> possibleTransitions = new ArrayList<>();
        for (Transition t : graph.getStates().get(currentState).getTransitions()) {
            if (t.getInput() == currentInput) {
                possibleTransitions.add(t);
            }
        }

        // Aucune transition possible = chemin sans issue
        if (possibleTransitions.isEmpty()) {
            return;
        }

        // Exploration de chaque transition possible
        for (Transition t : possibleTransitions) {
            // Construction du chemin pour cette exploration
            String currentPath = path + " " + currentState + "->" + t.getToState();

            // CAS 2 : Dernier caractère de la séquence
            // Une simple transition pour terminer
            if (remainingInput.length() == 1) {
                explore(graph, t.getToState(), "",
                        currentOutput + t.getOutput(),
                        solutions, currentPath + " (FINAL)");
                continue;
            }

            // Pour les cas suivants, il reste au moins 2 caractères à traiter
            char nextInput = remainingInput.charAt(1);

            // CAS 3 : Doublement forcé pour caractères identiques consécutifs
            // Si deux mêmes caractères se suivent, on les traite ensemble obligatoirement
            if (currentInput == nextInput) {
                explore(graph, t.getToState(),
                        remainingInput.substring(2),  // Skip des deux caractères
                        currentOutput + t.getOutput() + t.getOutput(),  // Double la sortie
                        solutions, currentPath + " (DOUBLE)");
                continue;
            }

            // CAS 4 : Exploration des possibilités de doublement pour caractères différents
            State nextState = graph.getStates().get(t.getToState());

            // 4.1 : Tentative de doublement par chemin direct
            // On cherche une transition directe depuis l'état d'arrivée qui accepte le caractère suivant
            for (Transition next : nextState.getTransitions()) {
                if (next.getInput() == nextInput) {
                    explore(graph, next.getToState(),
                            remainingInput.substring(2),  // Skip des deux caractères
                            currentOutput + t.getOutput() + next.getOutput(),
                            solutions, currentPath + " " + next.getToState());
                }
            }

            // 4.2 : Tentative de doublement par chemins alternatifs
            // On cherche d'autres chemins possibles passant par des états intermédiaires
            for (Transition alt : nextState.getTransitions()) {
                if (alt.getInput() == currentInput) {
                    State altState = graph.getStates().get(alt.getToState());
                    for (Transition next : altState.getTransitions()) {
                        if (next.getInput() == nextInput) {
                            explore(graph, next.getToState(),
                                    remainingInput.substring(2),
                                    currentOutput + t.getOutput() + next.getOutput(),
                                    solutions, currentPath + " ALT:" + alt.getToState());
                        }
                    }
                }
            }

            // 4.3 : Transition simple - on ne traite qu'un seul caractère
            // Cette option est toujours disponible sauf pour les caractères identiques
            explore(graph, t.getToState(),
                    remainingInput.substring(1),  // Skip d'un seul caractère
                    currentOutput + t.getOutput(),
                    solutions, currentPath + " (SIMPLE)");
        }
    }

    /**
     * Point d'entrée principal pour la recherche de séquences.
     * Cette méthode prépare l'environnement pour l'exploration et gère les résultats.
     * <p>
     * Le processus se déroule en plusieurs étapes :
     * 1. Initialisation des structures de données
     * 2. Lancement de l'exploration récursive
     * 3. Élimination des doublons dans les résultats
     * 4. Retour des solutions uniques trouvées
     *
     * @param graph         Le graphe complet de la machine à états
     * @param startState    L'identifiant de l'état initial
     * @param inputSequence La séquence de caractères à analyser
     * @return Un ensemble (Set) contenant toutes les séquences de sortie uniques possibles
     */
    public static Set<String> findAllSequences(Graph graph, int startState, String inputSequence) {
        // ArrayList pour la phase d'exploration (plus efficace pour les ajouts)
        List<String> solutions = new ArrayList<>();

        // Lancement de l'exploration récursive
        explore(graph, startState, inputSequence, "", solutions, "START");

        // Conversion en HashSet pour éliminer les doublons potentiels
        return new HashSet<>(solutions);
    }



    /**
     * Méthode de test qui vérifie la validité des séquences générées.
     * Compare les résultats obtenus avec les résultats attendus.
     */
    public static void testSequence(Graph graph, int startState, String inputSequence,
                                    List<String> expectedOutput) {
        System.out.println("==================================================");
        System.out.println("[*] TEST");
        System.out.printf("    STATE %d | INPUT '%s'%n", startState, inputSequence);

        Set<String> solutions = findAllSequences(graph, startState, inputSequence);

        if (expectedOutput != null) {
            boolean testResult = solutions.containsAll(expectedOutput) &&
                    solutions.size() == expectedOutput.size();

            if (solutions.isEmpty()) {
                System.out.println("Solutions: Not found");
            } else {
                System.out.println("Solutions trouvees :");
                int i = 1;
                for (String solution : solutions) {
                    System.out.printf("  %d. %s%n", i++, solution);
                }
            }

            System.out.printf("Attendu: %s%n", expectedOutput);
            System.out.printf("Resultat %s%n", testResult ? "REUSSI" : "ECHOUE");
            System.out.println("==================================================\n");
        }
    }
}