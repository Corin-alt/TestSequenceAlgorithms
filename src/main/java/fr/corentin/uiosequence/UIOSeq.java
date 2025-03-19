package fr.corentin.uiosequence;

import java.util.*;

/**
 * Classe principale demontrant l'utilisation du StateMachineBuilder
 * et l'analyse des sequences d'etats.
 *
 * Cette classe permet de:
 * - Construire une machine à etats complète
 * - Trouver des sequences uniques pour chaque etat
 * - Analyser et afficher les resultats en detail
 * - Verifier l'unicite des transitions
 */
public class UIOSeq {
    /**
     * Point d'entree principal du programme.
     *
     * Processus complet:
     * 1. Construction de la machine à etats avec le builder
     *    - Definition de tous les etats
     *    - Configuration des transitions possibles
     * 2. Recherche des sequences identificatrices
     *    - Pour chaque etat, trouve une sequence unique
     * 3. Analyse et affichage des resultats
     *    - Sequences trouvees
     *    - Etats sans solution
     *    - Verification des transitions
     *
     * @param args arguments de la ligne de commande (non utilises)
     */
    public static void main(String[] args) {
        // Etape 1: Construction de la machine avec le builder
        Map<Integer, Map<String, Transition>> transitions = buildStateMachine();

        // Etape 2: Recherche des sequences identificatrices
        Map<Integer, Map.Entry<String, String>> identifyingSequences =
                StateMachine.findStateIdentifyingSequences(transitions);

        // Etape 3: Analyse et affichage des resultats
        analyzeAndPrintResults(transitions, identifyingSequences);
    }

    /**
     * Construit la machine à etats en utilisant le builder.
     * @return Map des transitions complète de la machine
     */
    private static Map<Integer, Map<String, Transition>> buildStateMachine() {
        return new StateMachineBuilder()
                // Etat 1: Etat initial avec deux transitions possibles
                .state(1)
                .onInput("a", 5, "b")  // Sur 'a' va à 5, produit 'b'
                .onInput("d", 2, "w")  // Sur 'd' va à 2, produit 'w'
                // Etat 2: Une seule transition possible
                .state(2)
                .onInput("v", 3, "v")  // Sur 'v' va à 3, produit 'v'
                // Etat 3: Une seule transition possible
                .state(3)
                .onInput("v", 6, "x")  // Sur 'v' va à 6, produit 'x'
                // Etat 4: Une seule transition possible
                .state(4)
                .onInput("c", 2, "x")  // Sur 'c' va à 2, produit 'x'
                // Etat 5: Deux transitions possibles
                .state(5)
                .onInput("d", 6, "x")  // Sur 'd' va à 6, produit 'x'
                .onInput("t", 4, "v")  // Sur 't' va à 4, produit 'v'
                // Etat 6: Une seule transition possible
                .state(6)
                .onInput("c", 8, "v")  // Sur 'c' va à 8, produit 'v'
                // Etat 7: Deux transitions possibles
                .state(7)
                .onInput("d", 4, "x")  // Sur 'd' va à 4, produit 'x'
                .onInput("t", 5, "v")  // Sur 't' va à 5, produit 'v'
                // Etat 8: Deux transitions possibles
                .state(8)
                .onInput("d", 7, "v")  // Sur 'd' va à 7, produit 'v'
                .onInput("a", 5, "x")  // Sur 'a' va à 5, produit 'x'
                .build();
    }

    /**
     * Analyse et affiche les resultats des sequences trouvees.
     *
     * Cette methode:
     * 1. Identifie les etats sans solution
     * 2. Affiche les sequences trouvees
     * 3. Verifie les transitions utilisees
     *
     * L'analyse est complète et verifie:
     * - La presence de sequences pour chaque etat
     * - L'unicite des transitions
     * - Les details de chaque transition
     *
     * @param transitions La machine à etats complète
     * @param identifyingSequences Les sequences trouvees pour chaque etat
     */
    private static void analyzeAndPrintResults(
            Map<Integer, Map<String, Transition>> transitions,
            Map<Integer, Map.Entry<String, String>> identifyingSequences) {

        // 1. Trouve les etats sans solution
        Set<Integer> statesWithoutSolution = findStatesWithoutSolution(transitions, identifyingSequences);

        // 2. Affiche les sequences trouvees
        printSequences(identifyingSequences, statesWithoutSolution);

        // 3. Verifie les transitions utilisees
        verifyAndPrintTransitions(identifyingSequences, transitions);
    }

    /**
     * Trouve les etats qui n'ont pas de sequence identificatrice.
     *
     * Processus:
     * 1. Obtient tous les etats de la machine
     * 2. Obtient les etats avec une solution
     * 3. Calcule la difference pour trouver ceux sans solution
     *
     * Exemple:
     * - Tous les etats: [1, 2, 3, 4, 5]
     * - Etats avec solution: [1, 2, 4]
     * - Etats sans solution: [3, 5]
     *
     * @param transitions La machine à etats complète
     * @param identifyingSequences Les sequences trouvees
     * @return Set des etats sans sequence identificatrice
     */
    private static Set<Integer> findStatesWithoutSolution(
            Map<Integer, Map<String, Transition>> transitions,
            Map<Integer, Map.Entry<String, String>> identifyingSequences) {
        // Recupère tous les etats de la machine
        Set<Integer> allStates = transitions.keySet();
        // Recupère les etats qui ont une sequence
        Set<Integer> statesWithSolution = identifyingSequences.keySet();
        // Cree une copie pour ne pas modifier l'original
        Set<Integer> statesWithoutSolution = new HashSet<>(allStates);
        // Retire les etats qui ont une solution
        statesWithoutSolution.removeAll(statesWithSolution);
        return statesWithoutSolution;
    }

    /**
     * Affiche les sequences trouvees et les etats sans solution.
     *
     * Format d'affichage:
     * Pour chaque etat avec solution:
     * - Numero de l'etat
     * - Sequence d'entree
     * - Sequence de sortie correspondante
     *
     * Pour les etats sans solution:
     * - Liste des etats
     * - Message explicatif
     *
     * @param identifyingSequences Map des sequences trouvees par etat
     * @param statesWithoutSolution Set des etats sans solution
     */
    private static void printSequences(
            Map<Integer, Map.Entry<String, String>> identifyingSequences,
            Set<Integer> statesWithoutSolution) {

        // Affiche d'abord les sequences trouvees
        System.out.println("Sequences uniques identifiant chaque etat:");
        // Utilise TreeSet pour afficher les etats dans l'ordre
        for (int state : new TreeSet<>(identifyingSequences.keySet())) {
            Map.Entry<String, String> seq = identifyingSequences.get(state);
            System.out.printf("Etat %d: entree='%s', sortie='%s'%n",
                    state, seq.getKey(), seq.getValue());
        }

        // Affiche ensuite les etats sans solution
        if (!statesWithoutSolution.isEmpty()) {
            System.out.println("\nEtats sans sequence identificatrice trouvee:");
            for (int state : new TreeSet<>(statesWithoutSolution)) {
                System.out.printf("Etat %d: Aucune sequence unique possible " +
                        "avec les contraintes donnees%n", state);
            }
        }
    }

    /**
     * Verifie et affiche les details des transitions utilisees.
     *
     * Pour chaque etat avec une sequence:
     * 1. Obtient les details des transitions
     * 2. Affiche le chemin complet
     * 3. Verifie s'il y a des transitions dupliquees
     *
     * Exemple de transition:
     * Etat 1 -> Etat 5 sur entree 'a', sortie: 'b'
     *
     * @param identifyingSequences Les sequences à verifier
     * @param transitions La machine à etats complète
     */
    private static void verifyAndPrintTransitions(
            Map<Integer, Map.Entry<String, String>> identifyingSequences,
            Map<Integer, Map<String, Transition>> transitions) {

        System.out.println("\nVerification des transitions utilisees:");
        // Garde trace de toutes les transitions utilisees
        Set<Map.Entry<Integer, String>> allUsedTransitions = new HashSet<>();

        // Analyse chaque etat dans l'ordre
        for (int testState : new TreeSet<>(identifyingSequences.keySet())) {
            Map.Entry<String, String> seq = identifyingSequences.get(testState);

            // Obtient les details des transitions pour cet etat
            List<TransitionDetail> detailedTransitions =
                    StateMachine.getDetailedTransitions(testState, seq.getKey(), transitions);
            Set<Map.Entry<Integer, String>> transitionsUsed =
                    StateMachine.getUsedTransitions(testState, seq.getKey(), transitions);

            // Affiche les details et verifie les doublons
            printTransitionDetails(testState, seq, detailedTransitions);
            checkDuplicateTransitions(transitionsUsed, allUsedTransitions);

            // Ajoute les transitions à l'ensemble global
            allUsedTransitions.addAll(transitionsUsed);
        }
    }

    /**
     * Affiche les details d'une sequence de transitions.
     *
     * Format d'affichage pour chaque transition:
     * (etat_depart, entree) : etat_depart -> etat_arrivee, entree/sortie
     *
     * Exemple:
     * (1, a) : etat 1 -> etat 5, entree/sortie: a/b
     *
     * @param testState Etat en cours d'analyse
     * @param seq Sequence à afficher
     * @param detailedTransitions Liste des transitions à detailler
     */
    private static void printTransitionDetails(
            int testState,
            Map.Entry<String, String> seq,
            List<TransitionDetail> detailedTransitions) {

        System.out.printf("%nPour l'etat %d - sequence: entree='%s', sortie='%s'%n",
                testState, seq.getKey(), seq.getValue());
        System.out.println("Transitions utilisees:");

        // Affiche chaque transition en detail
        for (TransitionDetail trans : detailedTransitions) {
            System.out.printf("  (%d, %s) : etat %d -> etat %d, entree/sortie: %s/%s%n",
                    trans.fromState(), trans.input(), trans.fromState(),
                    trans.toState(), trans.input(), trans.output());
        }
    }

    /**
     * Verifie et affiche les transitions dupliquees.
     *
     * Une transition est dupliquee si:
     * - Elle apparaît dans les nouvelles transitions
     * - ET elle existe dejà dans les transitions precedentes
     *
     * But: S'assurer que chaque transition n'est utilisee qu'une fois
     * pour garantir l'unicite des sequences.
     *
     * @param newTransitions Nouvelles transitions à verifier
     * @param existingTransitions Transitions dejà utilisees
     */
    private static void checkDuplicateTransitions(
            Set<Map.Entry<Integer, String>> newTransitions,
            Set<Map.Entry<Integer, String>> existingTransitions) {

        // Cree une copie pour la verification
        Set<Map.Entry<Integer, String>> duplicates = new HashSet<>(newTransitions);
        // Garde seulement les transitions qui existent dejà
        duplicates.retainAll(existingTransitions);

        // Affiche un avertissement si des doublons sont trouves
        if (!duplicates.isEmpty()) {
            System.out.println("ATTENTION: Transitions reutilisees: " + duplicates);
        }
    }
}