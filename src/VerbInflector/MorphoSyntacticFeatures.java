package VerbInflector;

/**
 * Created by Mohammad Sadegh Rasooli.
 * User)) Mohammad Sadegh Rasooli
 * Date)) 5/29/14
 * Time)) 10))58 PM
 * To report any bugs or problems contact rasooli@cs.columbia.edu
 */

public class MorphoSyntacticFeatures {
    public MorphoSyntacticFeatures(NumberType num, PersonType pers, TenseFormationType tma, TensePositivity posit, TensePassivity voice)
    {
        Number = num;
        Person = pers;
        TenseMoodAspect = tma;
        Positivity = posit;
        Voice = voice;
    }

    public MorphoSyntacticFeatures(String num, String pers, String tma, TensePositivity posit, TensePassivity voice)
    {
        Number = this.StringToNumber(num);
        Person = this.StringToPerson(pers);
        TenseMoodAspect = this.StringToTMA(tma);
        Positivity = posit;
        Voice = voice;
    }

    private TenseFormationType StringToTMA(String tma)
    {
        if(tma.equals( "_"))
                return TenseFormationType.TenseFormationType_NONE;
        if(tma.equals(  "HAAL_SAADEH_EKHBARI")      )
                return TenseFormationType.HAAL_SAADEH_EKHBARI;
        if(tma.equals(  "HAAL_ELTEZAMI")           )
                return TenseFormationType.HAAL_ELTEZAMI;
        if(tma.equals(  "HAAL_SAADEH")            )
                return TenseFormationType.HAAL_SAADEH;
        if(tma.equals(  "AMR")                   )
                return TenseFormationType.AMR;
        if(tma.equals(  "GOZASHTEH_SADEH")      )
                return TenseFormationType.GOZASHTEH_SADEH;
        if(tma.equals(  "GOZASHTEH_ESTEMRAARI"))
                return TenseFormationType.GOZASHTEH_ESTEMRAARI;
        if(tma.equals(  "GOZASHTEH_NAGHLI_SADEH"))
                return TenseFormationType.GOZASHTEH_NAGHLI_SADEH;
        if(tma.equals(  "GOZASHTEH_NAGHLI_ESTEMRAARI"))
                return TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI;
        if(tma.equals(  "GOZASHTEH_BAEED"))
                return TenseFormationType.GOZASHTEH_BAEED;
        if(tma.equals(  "GOZASHTEH_ELTEZAMI"))
                return TenseFormationType.GOZASHTEH_ELTEZAMI;
        if(tma.equals(  "PAYEH_MAFOOLI"))
                return TenseFormationType.PAYEH_MAFOOLI;
        if(tma.equals(  "AAYANDEH"))
                return TenseFormationType.AAYANDEH;
        if(tma.equals(  "GOZASHTEH_ABAD"))
                return TenseFormationType.GOZASHTEH_ABAD;
        else
                return TenseFormationType.TenseFormationType_NONE;
    }

    private PersonType StringToPerson(String pers)
    {
         if(pers.equals( "_"))
                return PersonType.PERSON_NONE;
        if(pers.equals( "AVALSHAKHS_MOFRAD"))
                return PersonType.FIRST_PERSON_SINGULAR;
        if(pers.equals( "DOVVOMSHAKHS_MOFRAD"))
                return PersonType.SECOND_PERSON_SINGULAR;
        if(pers.equals( "SEVVOMSHAKHS_MOFRAD"))
                return PersonType.THIRD_PERSON_SINGULAR;
        if(pers.equals( "AVALSHAKHS_JAM"))
                return PersonType.FIRST_PERSON_PLURAL;
        if(pers.equals( "DOVVOMSHAKHS_JAM"))
                return PersonType.SECOND_PERSON_PLURAL;
        if(pers.equals( "SEVVOMSHAKHS_JAM"))
                return PersonType.THIRD_PERSON_PLURAL;
        else
                return PersonType.PERSON_NONE;
    }

    private NumberType StringToNumber(String num)
    {
            if(num.equals( "_"))
                return NumberType.INVALID;
            if(num.equals(  "SINGULAR"))
                return NumberType.SINGULAR;
            if(num.equals(  "PLURAL"))
                return NumberType.PLURAL;
            else
                return NumberType.INVALID;
    }

    public NumberType Number;
    public PersonType Person;
    public TenseFormationType TenseMoodAspect;
    public TensePositivity Positivity;
    public TensePassivity Voice;

    public MorphoSyntacticFeatures clone()
    {
        return new MorphoSyntacticFeatures(Number, Person, TenseMoodAspect,Positivity,Voice);
    }
}
