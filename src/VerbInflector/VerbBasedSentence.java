package VerbInflector;

import java.util.Vector;

/**
 * Created by Mohammad Sadegh Rasooli.
 * User: Mohammad Sadegh Rasooli
 * Date: 5/29/14
 * Time: 11:09 PM
 * To report any bugs or problems contact rasooli@cs.columbia.edu
 */

public class VerbBasedSentence {
    public Vector<DependencyBasedToken> SentenceTokens;
    public Vector<VerbInSentence> VerbsInSentence ;

    public VerbBasedSentence(Vector<DependencyBasedToken> tokens)
    {
        SentenceTokens = tokens;
        VerbsInSentence=new Vector<VerbInSentence>();
        for (int i = tokens.size()-1; i>=0; i--)
        {
            if(tokens.elementAt(i).CPOSTag.equals("V") && !tokens.elementAt(i).DependencyRelation.equals("PROG"))
            {
                VerbInSentence vis=new VerbInSentence(i);
                VerbsInSentence.add(vis);
            }
            if(tokens.elementAt(i).DependencyRelation.equals("POSDEP"))
            {
                for (VerbInSentence verbInSentence : VerbsInSentence)
                {
                    if (verbInSentence.LightVerbIndex == tokens.elementAt(i - 1).HeadNumber)
                    {
                        verbInSentence.NonVerbalElementIndex = i;
                        verbInSentence.VerbalPrepositionIndex = i - 1;
                    }
                }
            }
            else if(tokens.elementAt(i).DependencyRelation=="NVE")
            {
                for (VerbInSentence verbInSentence : VerbsInSentence)
                {
                    if (verbInSentence.LightVerbIndex == tokens.elementAt(i).HeadNumber)
                    {
                        verbInSentence.NonVerbalElementIndex = i;
                    }
                }
            }
        }
    }
}


