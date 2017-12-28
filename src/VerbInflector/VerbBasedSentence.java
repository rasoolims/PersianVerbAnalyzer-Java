package VerbInflector;

import java.util.ArrayList;

/**
 * Created by Mohammad Sadegh Rasooli.
 * User: Mohammad Sadegh Rasooli
 * Date: 5/29/14
 * Time: 11:09 PM
 * To report any bugs or problems contact rasooli@cs.columbia.edu
 */

public class VerbBasedSentence {
    public DependencyBasedToken[] sentenceTokens;
    public ArrayList<VerbInSentence> VerbsInSentence ;

    public VerbBasedSentence(ArrayList<DependencyBasedToken> tokens)
    {
        sentenceTokens = new DependencyBasedToken[tokens.size()];
        for(int i=0; i<tokens.size();i++)
            sentenceTokens[tokens.get(i).Position-1] = tokens.get(i);

        VerbsInSentence=new ArrayList<VerbInSentence>();
        for (int i = tokens.size()-1; i>=0; i--)
        {
            if(tokens.get(i).CPOSTag.equals("V") && !tokens.get(i).DependencyRelation.equals("PROG"))
            {
                VerbInSentence vis=new VerbInSentence(i);
                VerbsInSentence.add(vis);
            }
            if(tokens.get(i).DependencyRelation.equals("POSDEP"))
            {
                for (VerbInSentence verbInSentence : VerbsInSentence)
                {
                    if (verbInSentence.LightVerbIndex == tokens.get(i - 1).HeadNumber)
                    {
                        verbInSentence.NonVerbalElementIndex = i;
                        verbInSentence.VerbalPrepositionIndex = i - 1;
                    }
                }
            }
            else if(tokens.get(i).DependencyRelation=="NVE")
            {
                for (VerbInSentence verbInSentence : VerbsInSentence)
                {
                    if (verbInSentence.LightVerbIndex == tokens.get(i).HeadNumber)
                    {
                        verbInSentence.NonVerbalElementIndex = i;
                    }
                }
            }
        }
    }
}


