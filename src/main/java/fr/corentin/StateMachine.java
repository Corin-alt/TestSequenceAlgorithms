package fr.corentin;

import java.util.*;

/**
 * StateMachine est la classe principale qui gère une machine à etats finis.
 * Elle permet de:
 * - Executer des sequences d'entree et obtenir les sorties correspondantes
 * - Trouver des sequences uniques qui identifient chaque etat
 * - Analyser et verifier les transitions utilisees
 * - Explorer toutes les sequences possibles jusqu'à une longueur donnee
 *
 * La machine à etats est representee par une Map qui associe:
 * - À chaque etat (Integer)
 * - Une Map des transitions possibles depuis cet etat
 *   - Chaque transition est indexee par son caractère d'entree (String)
 *   - Et contient l'etat de destination et le caractère de sortie (Transition)
 */
public class StateMachine {
    /**
     * Longueur maximale des sequences à explorer.
     * Une valeur de 4 signifie qu'on ne cherchera pas de sequences plus longues que 3 caractères.
     * Cette limite permet d'eviter une explosion combinatoire dans l'exploration des sequences.
     */
    private static final int MAX_SEQUENCE_LENGTH = 4;

    /**
     * Execute une sequence d'entree dans la machine à etats.
     * Pour chaque caractère d'entree:
     * 1. Verifie si la transition est possible depuis l'etat actuel
     * 2. Si oui, ajoute le caractère de sortie au resultat et passe à l'etat suivant
     * 3. Si non, retourne null car la sequence est impossible
     *
     * Par exemple:
     * - Etat initial = 1
     * - Sequence "abc"
     * - À chaque etape, on verifie si l'etat actuel accepte le caractère
     * - On accumule les sorties dans un StringBuilder
     *
     * @param state Etat initial de depart
     * @param inputSeq La sequence de caractères d'entree à executer
     * @param transitions La map complète des transitions de la machine
     * @return La sequence de sortie produite, ou null si impossible
     */
    public static String executeSequence(int state, String inputSeq,
                                         Map<Integer, Map<String, Transition>> transitions) {
        // On garde une trace de l'etat courant qui change à chaque transition
        int currentState = state;
        // On utilise un StringBuilder pour construire efficacement la sortie
        StringBuilder outputSeq = new StringBuilder();

        // Pour chaque caractère de la sequence d'entree
        for (char inputChar : inputSeq.toCharArray()) {
            // Convertit le caractère en String pour l'utiliser comme cle
            String input = String.valueOf(inputChar);
            Map<String, Transition> stateTransitions = transitions.get(currentState);

            // Verifie si la transition est possible
            if (stateTransitions == null || !stateTransitions.containsKey(input)) {
                return null; // Sequence impossible
            }

            // Execute la transition
            Transition trans = stateTransitions.get(input);
            outputSeq.append(trans.getOutput()); // Ajoute le caractère de sortie
            currentState = trans.getToState();   // Met à jour l'etat courant
        }

        return outputSeq.toString();
    }

    /**
     * Recupère l'ensemble des transitions utilisees lors de l'execution d'une sequence.
     * Chaque transition est identifiee par une paire (etat, caractère d'entree).
     *
     * Cette methode est utile pour:
     * - Verifier quelles transitions sont utilisees
     * - Eviter de reutiliser les mêmes transitions
     * - Analyser le comportement de la machine
     *
     * Par exemple:
     * - Sequence "ab" depuis l'etat 1
     * - Pourrait utiliser les transitions: [(1,'a'), (5,'b')]
     *
     * @param state Etat initial
     * @param inputSeq Sequence d'entree à analyser
     * @param transitions Map des transitions possibles
     * @return Set des transitions utilisees, chacune etant une paire (etat, entree)
     */
    public static Set<Map.Entry<Integer, String>> getUsedTransitions(int state, String inputSeq,
                                                                     Map<Integer, Map<String, Transition>> transitions) {
        // Stocke les transitions utilisees
        Set<Map.Entry<Integer, String>> used = new HashSet<>();
        int currentState = state;

        // Pour chaque caractère d'entree
        for (char inputChar : inputSeq.toCharArray()) {
            String input = String.valueOf(inputChar);
            Map<String, Transition> stateTransitions = transitions.get(currentState);

            // Si la transition existe
            if (stateTransitions != null && stateTransitions.containsKey(input)) {
                // Ajoute la paire (etat actuel, caractère d'entree)
                used.add(new AbstractMap.SimpleEntry<>(currentState, input));
                // Met à jour l'etat pour la prochaine iteration
                currentState = stateTransitions.get(input).getToState();
            }
        }

        return used;
    }

    /**
     * Fournit les details complets de chaque transition utilisee dans une sequence.
     * Pour chaque transition, on capture:
     * - L'etat de depart
     * - L'etat d'arrivee
     * - Le caractère d'entree
     * - Le caractère de sortie produit
     *
     * Cette methode est particulièrement utile pour:
     * - Le debogage
     * - La visualisation du chemin emprunte
     * - La verification des sequences
     *
     * @param state Etat initial
     * @param inputSeq Sequence à analyser
     * @param transitions Map des transitions possibles
     * @return Liste ordonnee des details de chaque transition
     */
    public static List<TransitionDetail> getDetailedTransitions(int state, String inputSeq,
                                                                Map<Integer, Map<String, Transition>> transitions) {
        List<TransitionDetail> detailed = new ArrayList<>();
        int currentState = state;

        // Pour chaque caractère de la sequence
        for (char inputChar : inputSeq.toCharArray()) {
            String input = String.valueOf(inputChar);
            Map<String, Transition> stateTransitions = transitions.get(currentState);

            if (stateTransitions != null && stateTransitions.containsKey(input)) {
                Transition trans = stateTransitions.get(input);
                // Cree un objet TransitionDetail pour cette etape
                detailed.add(new TransitionDetail(currentState, trans.getToState(), input, trans.getOutput()));
                currentState = trans.getToState();
            }
        }

        return detailed;
    }

    /**
     * Genère toutes les sequences possibles pour un etat donne.
     * La methode respecte plusieurs contraintes:
     * - N'utilise pas les transitions dejà utilisees ailleurs
     * - Ne depasse pas la longueur maximale specifiee
     * - Explore toutes les combinaisons valides
     *
     * Utile pour:
     * - Trouver des sequences identificatrices
     * - Analyser le comportement de l'etat
     * - Tester la couverture des transitions
     *
     * @param state Etat de depart
     * @param transitions Toutes les transitions possibles
     * @param usedTransitions Transitions à eviter (dejà utilisees)
     * @param maxLength Longueur maximale des sequences à generer
     * @return Liste de toutes les sequences valides trouvees
     */
    public static List<Sequence> getAllSequencesForState(int state,
                                                         Map<Integer, Map<String, Transition>> transitions,
                                                         Set<Map.Entry<Integer, String>> usedTransitions,
                                                         int maxLength) {
        List<Sequence> sequences = new ArrayList<>();
        // Lance l'exploration recursive avec des sequences vides
        explore(state, "", "", new HashSet<>(), sequences, transitions, usedTransitions, maxLength);
        return sequences;
    }

    /**
     * Methode recursive qui explore toutes les sequences possibles.
     * Utilise un algorithme de backtracking:
     * 1. Ajoute la sequence actuelle si elle n'est pas vide
     * 2. Si la longueur max est atteinte, termine cette branche
     * 3. Sinon, essaie chaque transition possible
     * 4. Pour chaque transition valide, fait un appel recursif
     *
     * Points importants:
     * - Evite les transitions dejà utilisees
     * - Conserve une trace des transitions utilisees
     * - Construit les sequences caractère par caractère
     *
     * @param currentState Etat actuel dans l'exploration
     * @param currentInput Sequence d'entree construite jusqu'ici
     * @param currentOutput Sequence de sortie correspondante
     * @param currentUsed Transitions utilisees dans cette branche
     * @param sequences Liste où stocker les sequences trouvees
     * @param transitions Toutes les transitions possibles
     * @param usedTransitions Transitions à eviter
     * @param maxLength Longueur maximale à ne pas depasser
     */
    private static void explore(int currentState, String currentInput, String currentOutput,
                                Set<Map.Entry<Integer, String>> currentUsed, List<Sequence> sequences,
                                Map<Integer, Map<String, Transition>> transitions,
                                Set<Map.Entry<Integer, String>> usedTransitions, int maxLength) {
        // Si on a une sequence non vide, on l'ajoute
        if (!currentInput.isEmpty()) {
            sequences.add(new Sequence(currentInput, currentOutput, new HashSet<>(currentUsed)));
        }

        // Si on atteint la longueur max, on arrête cette branche
        if (currentInput.length() >= maxLength) {
            return;
        }

        // Recupère les transitions possibles depuis l'etat actuel
        Map<String, Transition> stateTransitions = transitions.get(currentState);
        if (stateTransitions != null) {
            // Pour chaque transition possible
            for (Map.Entry<String, Transition> entry : stateTransitions.entrySet()) {
                // Cree une nouvelle transition à tester
                Map.Entry<Integer, String> newTransition =
                        new AbstractMap.SimpleEntry<>(currentState, entry.getKey());

                // Si cette transition n'est pas interdite
                if (!usedTransitions.contains(newTransition)) {
                    // Prepare les nouvelles sequences
                    Set<Map.Entry<Integer, String>> newUsed = new HashSet<>(currentUsed);
                    newUsed.add(newTransition);

                    // Appel recursif avec la nouvelle transition
                    explore(entry.getValue().getToState(),
                            currentInput + entry.getKey(),
                            currentOutput + entry.getValue().getOutput(),
                            newUsed, sequences, transitions, usedTransitions, maxLength);
                }
            }
        }
    }

    /**
     * Verifie si une sequence est unique pour un etat donne.
     * Une sequence est unique si:
     * - Elle produit une sortie specifique depuis cet etat
     * - Aucun autre etat ne produit la même sortie avec cette entree
     *
     * Cette methode est cruciale pour:
     * - Identifier des sequences caracteristiques
     * - Distinguer les etats entre eux
     * - Valider l'unicite des sequences
     *
     * @param testState Etat à tester
     * @param inputSeq Sequence d'entree à verifier
     * @param outputSeq Sortie attendue
     * @param transitions Toutes les transitions possibles
     * @return true si la sequence est unique pour cet etat
     */
    public static boolean isSequenceUniqueForState(int testState, String inputSeq, String outputSeq,
                                                   Map<Integer, Map<String, Transition>> transitions) {
        // Teste la sequence depuis chaque autre etat
        for (int state : transitions.keySet()) {
            if (state != testState) {
                String result = executeSequence(state, inputSeq, transitions);
                // Si un autre etat produit la même sortie, la sequence n'est pas unique
                if (outputSeq.equals(result)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Methode principale qui trouve des sequences uniques pour identifier chaque etat.
     * Pour chaque etat, elle cherche la plus courte sequence qui:
     * 1. Produit une sortie unique à cet etat
     * 2. N'utilise que des transitions non utilisees par d'autres sequences
     *
     * L'algorithme:
     * - Commence par des sequences courtes (longueur 1)
     * - Augmente progressivement la longueur si necessaire
     * - Traite les etats dans l'ordre
     * - Garde une trace des transitions utilisees
     *
     * @param transitions Map complète des transitions de la machine
     * @return Map associant chaque etat à sa sequence identificatrice
     */
    public static Map<Integer, Map.Entry<String, String>> findStateIdentifyingSequences(
            Map<Integer, Map<String, Transition>> transitions) {
        // Stocke les sequences trouvees pour chaque etat
        Map<Integer, Map.Entry<String, String>> uniqueSequences = new HashMap<>();
        // Garde une trace des transitions dejà utilisees
        Set<Map.Entry<Integer, String>> usedTransitions = new HashSet<>();

        // Essaie des sequences de longueur croissante
        for (int maxLen = 1; maxLen < MAX_SEQUENCE_LENGTH; maxLen++) {
            // Traite les etats dans l'ordre
            List<Integer> states = new ArrayList<>(transitions.keySet());
            Collections.sort(states);

            // Pour chaque etat sans sequence
            for (int state : states) {
                if (!uniqueSequences.containsKey(state)) {
                    findUniqueSequenceForState(state, transitions, usedTransitions,
                            uniqueSequences, maxLen);
                }
            }
        }

        return uniqueSequences;
    }

    /**
     * Trouve une sequence unique pour un etat specifique.
     * Processus:
     * 1. Genère toutes les sequences possibles jusqu'à maxLen
     * 2. Filtre celles qui sont uniques à l'etat
     * 3. Choisit la plus courte parmi les sequences valides
     *
     * Important:
     * - Verifie l'unicite de chaque sequence
     * - Evite les transitions dejà utilisees
     * - Prefère les sequences courtes
     *
     * @param state Etat pour lequel trouver une sequence
     * @param transitions Map des transitions disponibles
     * @param usedTransitions Set des transitions dejà utilisees à eviter
     * @param uniqueSequences Map où stocker la sequence trouvee
     * @param maxLen Longueur maximale de sequence à essayer
     */
    private static void findUniqueSequenceForState(int state,
                                                   Map<Integer, Map<String, Transition>> transitions,
                                                   Set<Map.Entry<Integer, String>> usedTransitions,
                                                   Map<Integer, Map.Entry<String, String>> uniqueSequences,
                                                   int maxLen) {
        // Genère toutes les sequences possibles jusqu'à maxLen
        List<Sequence> sequences = getAllSequencesForState(state, transitions,
                usedTransitions, maxLen);
        List<Sequence> uniqueForState = new ArrayList<>();

        // Filtre pour ne garder que les sequences uniques
        for (Sequence seq : sequences) {
            // Verifie deux conditions:
            // 1. La sequence est unique pour cet etat
            // 2. Elle n'utilise que des transitions disponibles
            if (isSequenceUniqueForState(state, seq.getInput(), seq.getOutput(), transitions) &&
                    Collections.disjoint(seq.getUsedTransitions(), usedTransitions)) {
                uniqueForState.add(seq);
            }
        }

        // Si on a trouve des sequences valides
        if (!uniqueForState.isEmpty()) {
            // Prend la plus courte
            Sequence shortest = findShortestSequence(uniqueForState);
            // L'ajoute aux resultats
            uniqueSequences.put(state,
                    new AbstractMap.SimpleEntry<>(shortest.getInput(), shortest.getOutput()));
            // Marque ses transitions comme utilisees
            usedTransitions.addAll(shortest.getUsedTransitions());
        }
    }

    /**
     * Trouve la sequence la plus courte parmi une liste de sequences.
     * Utilise un stream pour:
     * 1. Comparer les longueurs des sequences d'entree
     * 2. Selectionner celle avec la plus petite longueur
     *
     * @param sequences Liste des sequences à comparer
     * @return La sequence ayant la plus courte chaîne d'entree
     * @throws IllegalArgumentException si la liste est vide
     */
    private static Sequence findShortestSequence(List<Sequence> sequences) {
        return sequences.stream()
                .min(Comparator.comparingInt(seq -> seq.getInput().length()))
                .orElseThrow(() -> new IllegalArgumentException("La liste des sequences est vide"));
    }
}