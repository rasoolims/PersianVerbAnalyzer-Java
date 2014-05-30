package VerbInflector;

/**
 * Created by Mohammad Sadegh Rasooli.
 * User: Mohammad Sadegh Rasooli
 * Date: 5/29/14
 * Time: 1:20 AM
 * To report any bugs or problems contact rasooli@cs.columbia.edu
 */

public class VerbInflection implements Comparable{
    /**
     verb root
    **/
    public Verb VerbRoot;

    /**
        Type of the attached pronoun
    **/
    public AttachedPronounType ZamirPeyvasteh;

    /**
         String representation of the attached pronoun
    **/
    public  String AttachedPronounString;

    /**
        Person type of the verb inflection
    **/
    public PersonType Person;

    /**
        Represents the formation of the tense
    **/
    public TenseFormationType TenseForm;

    /**
        Positive/Negative tense
    **/
    public TensePositivity Positivity;

    /**
        Active/Passive tense
    **/
    public TensePassivity Passivity;

    public VerbInflection(Verb vrb, AttachedPronounType zamirType, String zamirString, PersonType shakhstype, TenseFormationType tenseFormationType, TensePositivity positivity)
    {
        VerbRoot = vrb;
        ZamirPeyvasteh = zamirType;
        AttachedPronounString = zamirString;
        Person = shakhstype;
        TenseForm = tenseFormationType;
        Positivity = positivity;
        Passivity = TensePassivity.ACTIVE;
    }

    public VerbInflection(Verb vrb, AttachedPronounType zamir, String zamirPeyvastehString, PersonType shakhstype,TenseFormationType tenseFormationType,TensePositivity positivity, TensePassivity passivity)
    {
        VerbRoot = vrb;
        ZamirPeyvasteh = zamir;
        AttachedPronounString = zamirPeyvastehString;
        Person = shakhstype;
        TenseForm = tenseFormationType;
        Positivity = positivity;
        Passivity = passivity;
    }

    public boolean IsPayehFelMasdari()
    {
        if(Person==PersonType.THIRD_PERSON_SINGULAR && TenseForm==TenseFormationType.GOZASHTEH_SADEH)//TODO 
            return true;
        return false;
    }

    /**
        Shows whether negative form is valid or not
    **/
    private boolean IsNegativeFormValid()
    {
        return true;
    }

    /**
        Shows whether the mentioned person is valid or not
    **/
    private boolean IsPersonValid()
    {
        if (TenseForm==TenseFormationType.HAAL_SAADEH_EKHBARI &&  VerbRoot.PresentTenseRoot.equals("است") && Person!=PersonType.THIRD_PERSON_SINGULAR)
            return false;
        if (TenseForm==TenseFormationType.PAYEH_MAFOOLI && Person!=PersonType.PERSON_NONE)
            return false;
        if (TenseForm!=TenseFormationType.PAYEH_MAFOOLI && Person==PersonType.PERSON_NONE)
            return false;
        if (TenseForm==TenseFormationType.GOZASHTEH_NAGHLI_SADEH && Person==PersonType.THIRD_PERSON_SINGULAR)
            return false;
        if (TenseForm==TenseFormationType.AMR &&
                !(Person==PersonType.SECOND_PERSON_PLURAL || Person==PersonType.SECOND_PERSON_SINGULAR))
            return false;
        return true;
    }

    /**
        checks whether the attached pronoun is valid or not
    **/
    private boolean IsAttachedPronounValid()
    {
        if (TenseForm==TenseFormationType.AMR && (ZamirPeyvasteh==AttachedPronounType.SECOND_PERSON_SINGULAR || ZamirPeyvasteh==AttachedPronounType.SECOND_PERSON_PLURAL || ZamirPeyvasteh==AttachedPronounType.FIRST_PERSON_PLURAL || ZamirPeyvasteh==AttachedPronounType.FIRST_PERSON_SINGULAR))
            return false;
        if(IsEqualPersonPronoun(ZamirPeyvasteh,Person))
            return false;
        if (ZamirPeyvasteh==AttachedPronounType.AttachedPronoun_NONE && TenseForm==TenseFormationType.PAYEH_MAFOOLI)
            return true;
        if(ZamirPeyvasteh==AttachedPronounType.AttachedPronoun_NONE)
            return true;
        return VerbRoot.IsZamirPeyvastehValid() && TenseForm!=TenseFormationType.PAYEH_MAFOOLI;
    }

    /**
        Checks whether two parameter persons are equal or not (it is common in Persian language
        that the verb with a special person type can not have a same attached pronoun person type)
         <param name="zamirPeyvastehType">Attached pronoun type</param>
         <param name="shakhsType">Person type</param>
         <returns>true if persons are equal</returns>
    **/
    private static boolean IsEqualPersonPronoun(AttachedPronounType zamirPeyvastehType, PersonType shakhsType)
    {
        if (zamirPeyvastehType==AttachedPronounType.SECOND_PERSON_SINGULAR && shakhsType==PersonType.SECOND_PERSON_SINGULAR)
            return true;
        if (zamirPeyvastehType==AttachedPronounType.SECOND_PERSON_PLURAL && shakhsType==PersonType.SECOND_PERSON_PLURAL)
            return true;
        if (zamirPeyvastehType==AttachedPronounType.FIRST_PERSON_SINGULAR && shakhsType==PersonType.FIRST_PERSON_SINGULAR)
            return true;
        if (zamirPeyvastehType==AttachedPronounType.FIRST_PERSON_PLURAL && shakhsType==PersonType.FIRST_PERSON_PLURAL)
            return true;
        if (zamirPeyvastehType==AttachedPronounType.SECOND_PERSON_PLURAL && shakhsType==PersonType.SECOND_PERSON_SINGULAR)
            return true;
        if (zamirPeyvastehType==AttachedPronounType.SECOND_PERSON_SINGULAR && shakhsType==PersonType.SECOND_PERSON_PLURAL)
            return true;
        if (zamirPeyvastehType==AttachedPronounType.FIRST_PERSON_PLURAL && shakhsType==PersonType.FIRST_PERSON_SINGULAR)
            return true;
        if (zamirPeyvastehType==AttachedPronounType.FIRST_PERSON_SINGULAR && shakhsType==PersonType.FIRST_PERSON_PLURAL)
            return true;
        return false;
    }

    /**
        overrided method of To String
    **/
    @Override
    public   String toString()
    {
        return VerbRoot + "\t" + TenseForm + "\t" + Person + "\t" + ZamirPeyvasteh + "\t" + Positivity;
    }

    /**
        represent an abstract  String representation of the verb discarding its root details
    **/
    public  String AbstarctString()
    {
        return TenseForm + "\t" + ZamirPeyvasteh;
    }

    /**
        Checks whether the inflection if valid in Persian or not
    **/
    public boolean IsValid()
    {
        if ((TenseForm==TenseFormationType.HAAL_ELTEZAMI || TenseForm==TenseFormationType.AMR || TenseForm==TenseFormationType.PAYEH_MAFOOLI) && (VerbRoot.PresentTenseRoot.equals( "هست" ) || VerbRoot.PresentTenseRoot.equals("است")))
            return false;
        if (Positivity==TensePositivity.NEGATIVE && VerbRoot.PresentTenseRoot.equals( "است"))
            return false;
        if (TenseForm==TenseFormationType.HAAL_SAADEH_EKHBARI || TenseForm==TenseFormationType.HAAL_ELTEZAMI || TenseForm==TenseFormationType.AMR)
            if( VerbRoot.PresentTenseRoot==null || VerbRoot.PresentTenseRoot.equals(""))
                return false;
        if (TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI || TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI || TenseForm == TenseFormationType.GOZASHTEH_NAGHLI_SADEH || TenseForm == TenseFormationType.GOZASHTEH_SADEH || TenseForm == TenseFormationType.PAYEH_MAFOOLI)
            if ( VerbRoot.PastTenseRoot==null || VerbRoot.PastTenseRoot.equals(""))
                return false;
        if(TenseForm==TenseFormationType.AMR && VerbRoot.CanBeImperative==false)
            return false;
        if (TenseForm!=TenseFormationType.HAAL_SAADEH && VerbRoot.Type==VerbType.AYANDEH_PISHVANDI)
            return false;

        if(VerbRoot.PastTenseRoot.equals("بایست") )
        {
            if (TenseForm == TenseFormationType.HAAL_SAADEH && Person == PersonType.THIRD_PERSON_SINGULAR)
                return true;
            if (TenseForm == TenseFormationType.HAAL_SAADEH_EKHBARI && Person == PersonType.THIRD_PERSON_SINGULAR)
                return true;
            if (TenseForm == TenseFormationType.GOZASHTEH_SADEH && (Person == PersonType.THIRD_PERSON_SINGULAR || Person == PersonType.SECOND_PERSON_SINGULAR))
                return true;
            if (TenseForm == TenseFormationType.GOZASHTEH_ESTEMRAARI && (Person == PersonType.THIRD_PERSON_SINGULAR || Person == PersonType.SECOND_PERSON_SINGULAR))
                return true;
            return false;
        }
        return (IsAttachedPronounValid() && IsPersonValid() && IsNegativeFormValid());
    }

    public int compareTo(Object obj)
    {
        if (this.equals(obj))
            return 0;
        else
            return this.hashCode()-obj.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof VerbInflection))
        return false;
        VerbInflection inflection = (VerbInflection) obj;
        if (inflection.VerbRoot.equals(VerbRoot) && inflection.ZamirPeyvasteh == ZamirPeyvasteh && inflection.Person == Person && inflection.TenseForm == TenseForm && inflection.Positivity == Positivity)
            return true;
        return false;
    }

    @Override
    public int hashCode()
    {
        return VerbRoot.hashCode() + ZamirPeyvasteh.hashCode() + Person.hashCode() +
                TenseForm.hashCode() + Positivity.hashCode();
    }
}
