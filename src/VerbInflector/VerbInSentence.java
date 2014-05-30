package VerbInflector;

/**
 * Created by Mohammad Sadegh Rasooli.
 * User: Mohammad Sadegh Rasooli
 * Date: 5/29/14
 * Time: 11:10 PM
 * To report any bugs or problems contact rasooli@cs.columbia.edu
 */

public class VerbInSentence implements Comparable {
    public int LightVerbIndex;
    public int NonVerbalElementIndex;
    public int VerbalPrepositionIndex;

    public VerbInSentence(int lvIndex, int nveIndex, int vpIndex) {
        LightVerbIndex = lvIndex;
        NonVerbalElementIndex = nveIndex;
        VerbalPrepositionIndex = vpIndex;
    }

    public VerbInSentence(int lvIndex, int nveIndex) {
        this(lvIndex, nveIndex, -1);

    }

    public VerbInSentence(int lvIndex) {
        this(lvIndex, -1, -1);

    }

    @Override
    public String toString() {
        return LightVerbIndex + "\t" + NonVerbalElementIndex + "\t" + VerbalPrepositionIndex;
    }

    @Override
    public int compareTo(Object obj) {
        return toString().compareTo(obj.toString());
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null || !(obj instanceof VerbInSentence)) {
            return false;
        }
        return toString().equals(obj.toString());
    }


    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}