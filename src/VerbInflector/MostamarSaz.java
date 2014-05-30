package VerbInflector;

/**
 * Created by Mohammad Sadegh Rasooli.
 * User: Mohammad Sadegh Rasooli
 * Date: 5/29/14
 * Time: 12:05 PM
 * To report any bugs or problems contact rasooli@cs.columbia.edu
 */

public class MostamarSaz {
    public String Type;
    public int Head;
    public VerbInflection Inflection;


    public MostamarSaz (VerbInflection inflec, int head, String type)
    {
        this.Type = type;
        Inflection = inflec;
        Head = head;
    }
}
