package VerbInflector;

/**
 * Created by Mohammad Sadegh Rasooli.
 * User: Mohammad Sadegh Rasooli
 * Date: 5/29/14
 * Time: 12:40 AM
 * To report any bugs or problems contact rasooli@cs.columbia.edu
 */

public class Verb implements Comparable{
    /**
    Shows the preposition of the verb in the cases of compound verbs with prepositional phrases
    **/
    public String PrepositionOfVerb;

    /**
       non-verbal element
    **/
    public String NonVerbalElement;

    /**
       Prefix of the verb
    **/
    public String Prefix;

    /**
       Past tense root of the verb (in Persian there are two types of root for each verb; i.e. present tense and past tense
    **/
    public String PastTenseRoot;

    /**
       Present tense root of the verb (in Persian there are two types of root for each verb; i.e. present tense and past tense
    **/
    public String PresentTenseRoot;

    /** 
       Shows whether a verb is Transitive or not
    **/
    public VerbTransitivity Transitivity;

    /**
       Shows verb type; i.e. 
    **/
    public VerbType Type;

    /**
       shows whether the verb can be used in imperative form or not
    **/
    public boolean CanBeImperative;

    /**
       Shows the type of vowel at the end of the present tense root
    **/
    public String PresentRootConsonantVowelEndStem;

    /**
       Shows the type of vowel at the start of the past tense root
    **/
    public String PastRootVowelStart;

    /**
       Shows the type of vowel at the start of the present tense root
    **/
    public String PresentRootVowelStart;

    public Verb(String hz, String bonmazi, String bonmozareh, String psh, String flyar, VerbTransitivity trnst, VerbType type, boolean amrshdn, String vowelEnd,String maziVowelStart,String mozarehVowelStart)
    {
        PrepositionOfVerb = hz;
        NonVerbalElement = flyar;
        Prefix = psh;
        PastTenseRoot = bonmazi;
        PresentTenseRoot = bonmozareh;
        Transitivity = trnst;
        Type = type;
        CanBeImperative = amrshdn;
        PresentRootConsonantVowelEndStem = vowelEnd;
        PastRootVowelStart = maziVowelStart;
        PresentRootVowelStart = mozarehVowelStart;
    }

    /**
     *  Shows whether can have an object attached to it as a pronoun
     * @return true if verb is not intransitive</returns>
     */
    public boolean IsZamirPeyvastehValid()
    {
        return Transitivity!=VerbTransitivity.InTransitive;
    }


    @Override
    public  String toString()
    {
        String verbStr;
        if (!Prefix.equals(""))
            verbStr = PrepositionOfVerb + " " + NonVerbalElement + " " + Prefix + "#" + PastTenseRoot + "---" + PresentTenseRoot;
        else
            verbStr = PrepositionOfVerb + " " + NonVerbalElement + " " + PastTenseRoot + "---" + PresentTenseRoot;

        verbStr = verbStr.trim();
        verbStr += "\t" + Transitivity + "\t" + Type;
        return verbStr;
    }

    /**
       A special version of ToString Method. Details are omitted.
    **/
    public String SimpleToString()
    {
        String verbStr;
        if (!Prefix.equals(""))
            verbStr = PrepositionOfVerb + " " + NonVerbalElement + " " + Prefix + "#" + PastTenseRoot + "#" + PresentTenseRoot;
        else
            verbStr = PrepositionOfVerb + " " + NonVerbalElement + " " + PastTenseRoot + "#" + PresentTenseRoot;
        verbStr = verbStr.trim();
        return verbStr;
    }

    /**
        An exact copy of the object with different memory reference values</returns>
     **/
    public Verb clone()
    {
        Verb vrb=new Verb(PrepositionOfVerb,PastTenseRoot,PresentTenseRoot,Prefix,NonVerbalElement,Transitivity,Type,CanBeImperative,PresentRootConsonantVowelEndStem,PastRootVowelStart,PresentRootVowelStart);
        return vrb;
    }

    public int compareTo(Object obj)
    {
        if (this.equals(obj))
            return 0;
        if (this.hashCode() > obj.hashCode())
            return 1;
        return -1;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Verb))
            return false;
        Verb verb = (Verb) obj;
        if(verb.PastTenseRoot.equals(PastTenseRoot) && verb.PresentTenseRoot.equals(PresentTenseRoot) && verb.Prefix.equals(Prefix) &&
                verb.PrepositionOfVerb.equals(PrepositionOfVerb) && verb.NonVerbalElement.equals(NonVerbalElement) &&
                verb.Transitivity.equals(Transitivity) && verb.CanBeImperative==CanBeImperative)
            return true;
        return false;
    }

    @Override
    public int hashCode()
    {
        return PrepositionOfVerb.hashCode() + NonVerbalElement.hashCode() + Prefix.hashCode() + PastTenseRoot.hashCode() +
                PresentTenseRoot.hashCode() + Transitivity.value() + Type.value() +
                (CanBeImperative==true?1:0);
    }

}
