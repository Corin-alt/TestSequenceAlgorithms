package fr.corentin;

import java.util.Map;
import java.util.Set;

/**
 * Classe representant une sequence d'entree/sortie dans la machine à etats.
 * Une sequence est composee de:
 * - Une chaîne d'entree (ce qu'on envoie à la machine)
 * - Une chaîne de sortie (ce que la machine produit)
 * - L'ensemble des transitions utilisees
 *
 * Cette classe est utilisee pour:
 * - Stocker les sequences trouvees lors de l'exploration
 * - Garder trace des transitions utilisees
 * - Comparer et selectionner les meilleures sequences
 */
public class Sequence {
    /**
     * La chaîne de caractères envoyee à la machine.
     * Exemple: "abc" signifie qu'on envoie 'a', puis 'b', puis 'c'
     */
    private final String input;

    /**
     * La chaîne de caractères produite par la machine.
     * Exemple: "xyz" signifie que la machine a produit 'x', puis 'y', puis 'z'
     */
    private final String output;

    /**
     * L'ensemble des transitions utilisees pour cette sequence.
     * Chaque transition est une paire (etat, caractère d'entree).
     * Exemple: [(1,'a'), (2,'b')] signifie:
     * - Depuis l'etat 1, on a utilise l'entree 'a'
     * - Depuis l'etat 2, on a utilise l'entree 'b'
     */
    private final Set<Map.Entry<Integer, String>> usedTransitions;

    /**
     * Constructeur qui initialise une nouvelle sequence.
     * Tous les champs sont finaux car une sequence ne doit pas être modifiee
     * une fois creee.
     *
     * @param input La chaîne d'entree à envoyer à la machine
     * @param output La chaîne de sortie produite par la machine
     * @param usedTransitions L'ensemble des transitions utilisees
     */
    public Sequence(String input, String output, Set<Map.Entry<Integer, String>> usedTransitions) {
        this.input = input;
        this.output = output;
        this.usedTransitions = usedTransitions;
    }

    /**
     * @return La chaîne d'entree de la sequence
     */
    public String getInput() {
        return input;
    }

    /**
     * @return La chaîne de sortie de la sequence
     */
    public String getOutput() {
        return output;
    }

    /**
     * @return L'ensemble des transitions utilisees par cette sequence
     */
    public Set<Map.Entry<Integer, String>> getUsedTransitions() {
        return usedTransitions;
    }
}