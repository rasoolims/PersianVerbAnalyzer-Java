package VerbInflector;

/**
 * Created by Mohammad Sadegh Rasooli.
 * User: Mohammad Sadegh Rasooli
 * Date: 5/29/14
 * Time: 10:57 PM
 * To report any bugs or problems contact rasooli@cs.columbia.edu
 */

public class DependencyBasedToken
{
    public DependencyBasedToken(int pos, String word,String lemm,String cpos,String fpos,int head,String depRel,int wCount,MorphoSyntacticFeatures feats,Chasbidegi chasbidegi)
    {
        Position = pos;
        WordForm = word;
        Lemma = lemm;
        CPOSTag = cpos;
        FPOSTag = fpos;
        HeadNumber = head;
        DependencyRelation = depRel;
        TokenCount = wCount;
        MorphoSyntacticFeats = feats;
        ChasbidegiType = chasbidegi;
    }

    @Override
    public String toString()
{
    StringBuilder result = new StringBuilder(200);
    result.append("word: ").append(WordForm.toString()).append(" | ");
    result.append("position: ").append(Position).append(" | ");
    result.append("lemma: ").append(Lemma.toString()).append(" | ");
    result.append("cpos: ").append(CPOSTag.toString()).append(" | ");
    result.append("parent: ").append(HeadNumber).append(" | ");
    result.append("count: ").append(TokenCount).append(" | ").append("chasbidegi:").append(ChasbidegiType.toString());
    return result.toString();
}

    public int Position ;
    public String WordForm ;
    public String Lemma ;
    public String CPOSTag ;
    public String FPOSTag ;
    public int HeadNumber ;
    public String DependencyRelation ;
    public int TokenCount ;
    public Chasbidegi ChasbidegiType ;
    public MorphoSyntacticFeatures MorphoSyntacticFeats ;
}
