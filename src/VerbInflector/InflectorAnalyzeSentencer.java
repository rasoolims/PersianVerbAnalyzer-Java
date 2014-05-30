package VerbInflector;

import java.util.Vector;

/**
 * Created by Mohammad Sadegh Rasooli.
 * User: Mohammad Sadegh Rasooli
 * Date: 5/29/14
 * Time: 1:57 AM
 * To report any bugs or problems contact rasooli@cs.columbia.edu
 */

public class InflectorAnalyzeSentencer {
    /***
     * 
     * @param inflection
     * @return   the possible String representation of a specified verb inflection
     */
    public static Vector<String> GetInflections(VerbInflection inflection)
    {
        Vector<String> lstInflections = new Vector<String>();
        if(inflection.TenseForm== TenseFormationType.AMR)
             lstInflections = GetAmrInflections(inflection);
        else if(inflection.TenseForm== TenseFormationType.GOZASHTEH_ESTEMRAARI)
             lstInflections = GetGozashtehEstemrariInflections(inflection);
        else  if(inflection.TenseForm== TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI)
                lstInflections = GetGozashtehNaghliEstemraiSadehInflections(inflection);
        else if(inflection.TenseForm== TenseFormationType.GOZASHTEH_NAGHLI_SADEH)
                lstInflections = GetGozashtehNaghliSadehInflections(inflection);
        else if(inflection.TenseForm==TenseFormationType.GOZASHTEH_SADEH)
                lstInflections = GetGozashtehSadehInflections(inflection);
        else  if(inflection.TenseForm== TenseFormationType.HAAL_ELTEZAMI)
                lstInflections = GetHaalEltezamiInflections(inflection);
        else  if(inflection.TenseForm== TenseFormationType.HAAL_SAADEH) 
                lstInflections = GetHaalSaadehInflections(inflection);
        else  if(inflection.TenseForm== TenseFormationType.HAAL_SAADEH_EKHBARI)
                lstInflections = GetHaalSaadehEkhbaariInflections(inflection);
        else  if(inflection.TenseForm==TenseFormationType.PAYEH_MAFOOLI)
                lstInflections = GetPayehFelInflections(inflection);
        else  if(inflection.TenseForm==TenseFormationType.GOZASHTEH_BAEED_ELTEZAMI){
            //todo
        }
        else  if(inflection.TenseForm==TenseFormationType.GOZASHTEH_BAEED_ESTEMRARI_ELTEZAMI){
            //todo
        }
        return lstInflections;
    }

    private static  Vector<String> GetGozashtehNaghliSadehInflections(VerbInflection inflection)
    {
        Vector<String> lstInflections = new  Vector<String>();
        VerbInflection verbInflection = new VerbInflection(inflection.VerbRoot, AttachedPronounType.AttachedPronoun_NONE,"",
                PersonType.PERSON_NONE, TenseFormationType.PAYEH_MAFOOLI,
                inflection.Positivity);
        Vector<String> tempLst = GetPayehFelInflections(verbInflection);
        String fel = tempLst.elementAt(0);
        if(inflection.Person== PersonType.THIRD_PERSON_PLURAL)
                fel += "‌اند";
              else  if(inflection.Person== PersonType.SECOND_PERSON_SINGULAR)
                fel += "‌ای";
        else  if(inflection.Person==PersonType.SECOND_PERSON_PLURAL)
                fel += "‌اید";
        else  if(inflection.Person== PersonType.FIRST_PERSON_SINGULAR)
                fel += "‌ام";
        else  if(inflection.Person== PersonType.FIRST_PERSON_PLURAL)
                fel += "‌ایم";

        lstInflections.add(AddAttachedPronoun(fel, inflection));
        return lstInflections;
    }

    private static  Vector<String> GetGozashtehNaghliEstemraiSadehInflections(VerbInflection inflection)
    {
        Vector<String> lstInflections = new  Vector<String>();
        StringBuilder verbBuilder = new StringBuilder();
        verbBuilder.append(inflection.VerbRoot.Prefix);

        if(inflection.Positivity== TensePositivity.POSITIVE)
                verbBuilder.append("می‌" );
               else if(inflection.Positivity== TensePositivity.NEGATIVE)
                verbBuilder.append("نمی‌" );

        Verb verb = new Verb("", inflection.VerbRoot.PastTenseRoot,
                inflection.VerbRoot.PresentTenseRoot, "",
                "", inflection.VerbRoot.Transitivity, VerbType.SADEH,
                inflection.VerbRoot.CanBeImperative, inflection.VerbRoot.PresentRootConsonantVowelEndStem,inflection.VerbRoot.PastRootVowelStart,inflection.VerbRoot.PresentRootVowelStart);
        VerbInflection verbInflection = new VerbInflection(verb, AttachedPronounType.AttachedPronoun_NONE,"",
                PersonType.PERSON_NONE, TenseFormationType.PAYEH_MAFOOLI,
                TensePositivity.POSITIVE);

        Vector<String> tempLst = GetPayehFelInflections(verbInflection);
        verbBuilder.append(tempLst.elementAt(0));
        if(inflection.Person== PersonType.THIRD_PERSON_PLURAL)
                verbBuilder.append("‌اند");
        else if(inflection.Person==  PersonType.SECOND_PERSON_SINGULAR)
                verbBuilder.append("‌ای");
        else if(inflection.Person==  PersonType.SECOND_PERSON_PLURAL)
                verbBuilder.append("‌اید");
        else if(inflection.Person==  PersonType.FIRST_PERSON_SINGULAR)
                verbBuilder.append("‌ام");
        else if(inflection.Person==  PersonType.FIRST_PERSON_PLURAL)
                verbBuilder.append("‌ایم");
        lstInflections.add(AddAttachedPronoun(verbBuilder.toString(), inflection));
        return lstInflections;
    }

    private static  Vector<String> GetGozashtehEstemrariInflections(VerbInflection inflection)
    {
        Vector<String> lstInflections = new  Vector<String>();
        StringBuilder verbBuilder = new StringBuilder();
        verbBuilder.append(inflection.VerbRoot.Prefix);
       if(inflection.Positivity== TensePositivity.POSITIVE)
                verbBuilder.append("می‌" + inflection.VerbRoot.PastTenseRoot);
       else if(inflection.Positivity==  TensePositivity.NEGATIVE)
                verbBuilder.append("نمی‌" + inflection.VerbRoot.PastTenseRoot);

        if (inflection.VerbRoot.PastTenseRoot.endsWith("آ"))
        {
            verbBuilder.delete(verbBuilder.length() - 1, verbBuilder.length());
            verbBuilder.append("ی");
        }
        else if (inflection.VerbRoot.PastTenseRoot.endsWith("ا") || inflection.VerbRoot.PastTenseRoot.endsWith("و"))
        {
            verbBuilder.append("ی");
        }
        if(inflection.Person== PersonType.FIRST_PERSON_PLURAL)
                verbBuilder.append("یم");
                else if(inflection.Person==  PersonType.FIRST_PERSON_SINGULAR)
                verbBuilder.append("م");
        else if(inflection.Person==  PersonType.SECOND_PERSON_PLURAL)
                verbBuilder.append("ید");
        else if(inflection.Person==  PersonType.SECOND_PERSON_SINGULAR)
                verbBuilder.append("ی");
        else if(inflection.Person==  PersonType.THIRD_PERSON_PLURAL)
                verbBuilder.append("ند");

        lstInflections.add(AddAttachedPronoun(verbBuilder.toString(), inflection));
        return lstInflections;
    }

    private static  Vector<String> GetGozashtehSadehInflections(VerbInflection inflection)
    {
        Vector<String> lstInflections = new  Vector<String>();
        StringBuilder verbBuilder = new StringBuilder();
        verbBuilder.append(inflection.VerbRoot.Prefix);
        if (inflection.Positivity == TensePositivity.NEGATIVE)
        {
            verbBuilder.append("ن");
        }

        if (inflection.VerbRoot.PastRootVowelStart.equals("A") && inflection.Positivity == TensePositivity.NEGATIVE)
        {
            if (!inflection.VerbRoot.PastTenseRoot.startsWith("آ"))
                verbBuilder.append("ی");
            else
                verbBuilder.append("یا");
            verbBuilder.append(inflection.VerbRoot.PastTenseRoot.substring(1));
        }

        else
        {
            verbBuilder.append(inflection.VerbRoot.PastTenseRoot);
        }

        if (inflection.VerbRoot.PastTenseRoot.endsWith("آ"))
        {
            verbBuilder.delete(verbBuilder.length() - 1, verbBuilder.length());
            verbBuilder.append("ی");
        }
        else if (inflection.VerbRoot.PastTenseRoot.endsWith("ا") || inflection.VerbRoot.PastTenseRoot.endsWith("و"))
        {
            verbBuilder.append("ی");
        }
        if (inflection.Person == PersonType.FIRST_PERSON_PLURAL)
                verbBuilder.append("یم");
                else  if (inflection.Person ==  PersonType.FIRST_PERSON_SINGULAR)
                verbBuilder.append("م");
        else  if (inflection.Person ==  PersonType.SECOND_PERSON_PLURAL)
                verbBuilder.append("ید");
        else  if (inflection.Person == PersonType.SECOND_PERSON_SINGULAR)
                verbBuilder.append("ی");
        else  if (inflection.Person ==  PersonType.THIRD_PERSON_PLURAL)
                verbBuilder.append("ند");

        lstInflections.add(AddAttachedPronoun(verbBuilder.toString(), inflection));
        return lstInflections;
    }

    private static  Vector<String> GetHaalSaadehEkhbaariInflections(VerbInflection inflection)
    {
        Vector<String> lstInflections = new  Vector<String>();
        StringBuilder verbBuilder = new StringBuilder();
        verbBuilder.append(inflection.VerbRoot.Prefix);
        if(inflection.VerbRoot.PresentTenseRoot .equals("است"))
        {
            verbBuilder.append(inflection.VerbRoot.PresentTenseRoot);
            lstInflections.add(verbBuilder.toString());
            return lstInflections;
        }
        if (inflection.VerbRoot.PresentTenseRoot .equals("هست")   )
        {
            if (inflection.Positivity== TensePositivity.POSITIVE)
                    verbBuilder.append(inflection.VerbRoot.PresentTenseRoot);
                   else if (inflection.Positivity== TensePositivity.NEGATIVE)
                    verbBuilder.append("نیست");
        }
        else
        {
            if (inflection.Positivity==  TensePositivity.POSITIVE)
                    verbBuilder.append("می‌" + inflection.VerbRoot.PresentTenseRoot);
                  else if (inflection.Positivity==  TensePositivity.NEGATIVE)
                    verbBuilder.append("نمی‌" + inflection.VerbRoot.PresentTenseRoot);
        }
        if (inflection.VerbRoot.PresentRootConsonantVowelEndStem .equals("A")  )
        {
            if (inflection.VerbRoot.PresentTenseRoot.length()>1)
            {
                verbBuilder.delete(verbBuilder.length() - 1, verbBuilder.length());
                verbBuilder.append("ای");
            }
            else
                verbBuilder.append("ی");
        }
        else if (!inflection.VerbRoot.PresentRootConsonantVowelEndStem .equals( "?"))
        {
            verbBuilder.append("ی");
        }
       if(inflection.Person== PersonType.FIRST_PERSON_PLURAL)
                verbBuilder.append("یم");
               else if(inflection.Person==  PersonType.FIRST_PERSON_SINGULAR )
                verbBuilder.append("م");
       else if(inflection.Person==  PersonType.SECOND_PERSON_PLURAL)
                verbBuilder.append("ید");
       else if(inflection.Person==  PersonType.SECOND_PERSON_SINGULAR)
                verbBuilder.append("ی");
       else if(inflection.Person==   PersonType.THIRD_PERSON_PLURAL)
                verbBuilder.append("ند");
       else if(inflection.Person==   PersonType.THIRD_PERSON_SINGULAR)
                if (!inflection.VerbRoot.PresentTenseRoot .equals( "باید") && !inflection.VerbRoot.PresentTenseRoot .equals( "هست") )
                    verbBuilder.append("د");

        lstInflections.add(AddAttachedPronoun(verbBuilder.toString(), inflection));
        return lstInflections;
    }

    private static  Vector<String> GetHaalEltezamiInflections(VerbInflection inflection)
    {
        Vector<String> lstInflections = new  Vector<String>();
        StringBuilder verbBuilder = new StringBuilder();
        StringBuilder verbBuilder2 = new StringBuilder();
        StringBuilder verbBuilder3 = new StringBuilder();
        boolean thirdInflec = false;
        verbBuilder.append(inflection.VerbRoot.Prefix);
        verbBuilder2.append(inflection.VerbRoot.Prefix);

        if (inflection.Positivity== TensePositivity.POSITIVE)
                if (!(inflection.VerbRoot.PresentTenseRoot .equals("باشد") || inflection.VerbRoot.PresentTenseRoot .equals("باید")))
                    verbBuilder.append("ب");
        else if (inflection.Positivity== TensePositivity.NEGATIVE)
                verbBuilder.append("ن");

        if (inflection.VerbRoot.PresentRootVowelStart.equals("A")   )
        {
            if (inflection.VerbRoot.PresentTenseRoot.startsWith("آ"))
            {
                verbBuilder.append("یا");
                verbBuilder.append(inflection.VerbRoot.PresentTenseRoot.substring(1));
                verbBuilder2.append(inflection.VerbRoot.PresentTenseRoot);
            }
            else
            {
                thirdInflec = true;
                verbBuilder3.append(verbBuilder.toString());
                verbBuilder.append("ی");
                verbBuilder3.append("یا");

                verbBuilder.append(inflection.VerbRoot.PresentTenseRoot.substring(1));
                verbBuilder3.append(inflection.VerbRoot.PresentTenseRoot.substring(1));
                verbBuilder2.append(inflection.VerbRoot.PresentTenseRoot);
            }
        }
        else
        {
            verbBuilder.append(inflection.VerbRoot.PresentTenseRoot);
            verbBuilder2.append(inflection.VerbRoot.PresentTenseRoot);
        }

        if (inflection.VerbRoot.PresentRootConsonantVowelEndStem.equals("A")  )
        {
            if (verbBuilder.length() > 1)
            {
                verbBuilder.delete(verbBuilder.length() - 1, verbBuilder.length());
                verbBuilder.append("ای");
                if (thirdInflec)
                {
                    verbBuilder3.delete(verbBuilder3.length() - 1, verbBuilder3.length());
                    verbBuilder3.append("ای");
                }
                if (inflection.VerbRoot.PresentTenseRoot.length() > 1)
                {
                    verbBuilder2.delete(verbBuilder2.length() - 1, verbBuilder2.length());
                    verbBuilder2.append("ای");
                }
                else
                {
                    verbBuilder2.append("ی");
                }
            }
            else
            {
                verbBuilder.append("ی");
                verbBuilder3.append("ی");
                verbBuilder2.append("ی");
            }
        }
        else if (!inflection.VerbRoot.PresentRootConsonantVowelEndStem .equals("?"))
        {
            if (!inflection.VerbRoot.PastTenseRoot .equals( "رفت") && !inflection.VerbRoot.PastTenseRoot .equals( "شد")  )
            {
                verbBuilder.append("ی");
                verbBuilder3.append("ی");
                verbBuilder2.append("ی");
            }
        }
        if (inflection.Person== PersonType.FIRST_PERSON_PLURAL){
                verbBuilder.append("یم");
                verbBuilder3.append("یم");
                verbBuilder2.append("یم");
        }
        else  if (inflection.Person==  PersonType.FIRST_PERSON_SINGULAR){
                verbBuilder.append("م");
                verbBuilder3.append("م");
                verbBuilder2.append("م");
        }
        else  if (inflection.Person==   PersonType.SECOND_PERSON_PLURAL){
                verbBuilder.append("ید");
                verbBuilder3.append("ید");
                verbBuilder2.append("ید");
            }
        else  if (inflection.Person==   PersonType.SECOND_PERSON_SINGULAR){
                verbBuilder.append("ی");
                verbBuilder3.append("ی");
                verbBuilder2.append("ی");
        }
        else  if (inflection.Person==   PersonType.THIRD_PERSON_PLURAL){
                verbBuilder.append("ند");
                verbBuilder3.append("ند");
                verbBuilder2.append("ند");
        }
        else  if (inflection.Person==   PersonType.THIRD_PERSON_SINGULAR) {
                verbBuilder.append("د");
                verbBuilder3.append("د");
                verbBuilder2.append("د");
        }
        
        lstInflections.add(AddAttachedPronoun(verbBuilder.toString(), inflection));
        if(thirdInflec)
            lstInflections.add(AddAttachedPronoun(verbBuilder3.toString(), inflection));
        if (inflection.Positivity == TensePositivity.POSITIVE && (inflection.VerbRoot.PresentTenseRoot.length() >= 2 || inflection.VerbRoot.Type == VerbType.PISHVANDI))
            lstInflections.add(AddAttachedPronoun(verbBuilder2.toString(), inflection));
        return lstInflections;
    }

    private static  Vector<String> GetHaalSaadehInflections(VerbInflection inflection)
    {
        Vector<String> lstInflections = new  Vector<String>();
        if (inflection.VerbRoot.PastTenseRoot.equals("خواست")  || inflection.VerbRoot.PastTenseRoot .equals("خواست" ) || inflection.VerbRoot.PastTenseRoot .equals("داشت" ) || inflection.VerbRoot.PastTenseRoot .equals("بایست") || inflection.VerbRoot.Type==VerbType.AYANDEH_PISHVANDI)
        {
        StringBuilder verbBuilder = new StringBuilder();
            verbBuilder.append(inflection.VerbRoot.Prefix);
            if (inflection.Positivity==TensePositivity.NEGATIVE)
            {
                verbBuilder.append("ن");
            }
            verbBuilder.append(inflection.VerbRoot.PresentTenseRoot);

            if (inflection.VerbRoot.PresentRootConsonantVowelEndStem .equals("A")  )
            {
                if (verbBuilder.length() > 1)
                {
                    verbBuilder.delete(verbBuilder.length() - 1, verbBuilder.length());
                    verbBuilder.append("ای");
                }
                else
                    verbBuilder.append("ی");
            }
            else if (!inflection.VerbRoot.PresentRootConsonantVowelEndStem.equals("?"))
            {
                verbBuilder.append("ی");
            }


          if(inflection.Person== PersonType.FIRST_PERSON_PLURAL)
                    verbBuilder.append("یم");
        else if(inflection.Person== PersonType.FIRST_PERSON_SINGULAR)
                    verbBuilder.append("م");
          else if(inflection.Person== PersonType.SECOND_PERSON_PLURAL)
                    verbBuilder.append("ید");
          else if(inflection.Person== PersonType.SECOND_PERSON_SINGULAR)
                    verbBuilder.append("ی");
          else if(inflection.Person== PersonType.THIRD_PERSON_PLURAL)
                    verbBuilder.append("ند");
          else if(inflection.Person== PersonType.THIRD_PERSON_SINGULAR)
                    if (!inflection.VerbRoot.PastTenseRoot .equals( "بایست") )
                        verbBuilder.append("د");

            lstInflections.add(AddAttachedPronoun(verbBuilder.toString(), inflection));
        }

        return lstInflections;
    }

    private static  Vector<String> GetAmrInflections(VerbInflection inflection)
    {
        Vector<String> lstInflections = new  Vector<String>();
        StringBuilder verbBuilder1 = new StringBuilder();
        StringBuilder verbBuilder2 = new StringBuilder();
        StringBuilder verbBuilder3 = new StringBuilder();
        StringBuilder verbBuilder4 = new StringBuilder();
        boolean fourthInflec = false;
        if (!inflection.VerbRoot.Prefix.equals( ""))
        {
            verbBuilder1.append(inflection.VerbRoot.Prefix);
            if (inflection.Positivity == TensePositivity.NEGATIVE)
            {
                verbBuilder3.append(inflection.VerbRoot.Prefix);
            }
            if (inflection.Positivity == TensePositivity.POSITIVE)
            {
                verbBuilder2.append(inflection.VerbRoot.Prefix);
            }
        }
       if(inflection.Positivity== TensePositivity.POSITIVE)
                if (!(inflection.VerbRoot.PresentTenseRoot .equals("باش" ) || inflection.VerbRoot.PresentTenseRoot .equals("باید")))
                    verbBuilder1.append("ب");
              else  if(inflection.Positivity== TensePositivity.NEGATIVE){
                verbBuilder1.append("ن");
                verbBuilder3.append("م");
             }
        if (inflection.VerbRoot.PresentRootVowelStart .equals("A"))
        {
            if (inflection.VerbRoot.PresentTenseRoot.startsWith("آ"))
            {
                verbBuilder1.append("یا");
                verbBuilder1.append(inflection.VerbRoot.PresentTenseRoot.substring( 1));
                if (inflection.Positivity == TensePositivity.NEGATIVE)
                {
                    verbBuilder3.append("یا");
                    verbBuilder3.append(inflection.VerbRoot.PresentTenseRoot.substring(1));
                }
                if (inflection.Positivity == TensePositivity.POSITIVE)
                {
                    verbBuilder2.append(inflection.VerbRoot.PresentTenseRoot);
                }
            }
            else
            {
                fourthInflec = true;
                verbBuilder4.append(verbBuilder1.toString());
                verbBuilder1.append("ی");
                verbBuilder4.append("یا");
                verbBuilder1.append(inflection.VerbRoot.PresentTenseRoot.substring( 1));
                verbBuilder4.append(inflection.VerbRoot.PresentTenseRoot.substring(1));
                if (inflection.Positivity == TensePositivity.NEGATIVE)
                {
                    verbBuilder3.append("ی");
                    verbBuilder3.append(inflection.VerbRoot.PresentTenseRoot.substring( 1));
                }
                if (inflection.Positivity == TensePositivity.POSITIVE)
                {
                    verbBuilder2.append(inflection.VerbRoot.PresentTenseRoot);
                }
            }
        }
        else
        {
            verbBuilder1.append(inflection.VerbRoot.PresentTenseRoot);
            if (inflection.Positivity == TensePositivity.POSITIVE/* && inflection.VerbStem.Type==VerbType.PISHVANDI*/)
            {
                verbBuilder2.append(inflection.VerbRoot.PresentTenseRoot);
            }
            if (inflection.Positivity == TensePositivity.NEGATIVE)
            {
                verbBuilder3.append(inflection.VerbRoot.PresentTenseRoot);
            }
        }

        if(inflection.Person== PersonType.SECOND_PERSON_PLURAL)
                if (!inflection.VerbRoot.PresentRootConsonantVowelEndStem.equals("?"))
                {
                    verbBuilder1.append("یید");
                    verbBuilder4.append("یید");
                    if (inflection.Positivity == TensePositivity.NEGATIVE)
                    {
                        verbBuilder3.append("یید");
                    }
                    if (inflection.Positivity == TensePositivity.POSITIVE && inflection.VerbRoot.Type==VerbType.PISHVANDI)
                    {
                        verbBuilder2.append("یید");
                    }
                }
                else
                {
                    verbBuilder1.append("ید");
                    verbBuilder4.append("ید");
                    if (inflection.Positivity == TensePositivity.NEGATIVE)
                    {
                        verbBuilder3.append("ید");
                    }
                    if (inflection.Positivity == TensePositivity.POSITIVE && inflection.VerbRoot.Type == VerbType.PISHVANDI)
                    {
                        verbBuilder2.append("ید");
                    }
                }

        if (inflection.ZamirPeyvasteh == AttachedPronounType.AttachedPronoun_NONE)
        {
            if (!(inflection.VerbRoot.PresentTenseRoot .equals("نه") && inflection.Positivity == TensePositivity.NEGATIVE))
                lstInflections.add(verbBuilder1.toString());
            if(fourthInflec)
                lstInflections.add(verbBuilder4.toString());
            if (inflection.Positivity == TensePositivity.NEGATIVE)
                lstInflections.add(verbBuilder3.toString());
            if (inflection.Positivity == TensePositivity.POSITIVE && (inflection.VerbRoot.Type == VerbType.PISHVANDI || inflection.VerbRoot.PastTenseRoot.equals("کرد" ) || inflection.VerbRoot.PastTenseRoot.equals("نمود" ) || inflection.VerbRoot.PastTenseRoot.equals("فرمود")))
                lstInflections.add(verbBuilder2.toString());
            if (inflection.VerbRoot.Type == VerbType.PISHVANDI && inflection.Person == PersonType.SECOND_PERSON_SINGULAR && inflection.Positivity == TensePositivity.POSITIVE &&
                    !inflection.VerbRoot.PresentRootConsonantVowelEndStem.equals("?"))
            {
                lstInflections.add(verbBuilder2.append("ی").toString());
            }
        }
        else
        {
            if (!(inflection.VerbRoot.PresentTenseRoot .equals("نه") && inflection.Positivity == TensePositivity.NEGATIVE))
                lstInflections.add(AddAttachedPronoun(verbBuilder1.toString(), inflection));
            if (fourthInflec)
                lstInflections.add(AddAttachedPronoun(verbBuilder4.toString(), inflection));
            if (inflection.Positivity == TensePositivity.NEGATIVE)
                lstInflections.add(AddAttachedPronoun(verbBuilder3.toString(),inflection));
            if (inflection.VerbRoot.Type == VerbType.PISHVANDI && inflection.Positivity == TensePositivity.POSITIVE)
                lstInflections.add(AddAttachedPronoun(verbBuilder2.toString(), inflection));
            if (inflection.VerbRoot.Type == VerbType.PISHVANDI && inflection.Person==PersonType.SECOND_PERSON_SINGULAR && inflection.Positivity == TensePositivity.POSITIVE &&
                    !inflection.VerbRoot.PresentRootConsonantVowelEndStem.equals( "?"))
            {
                lstInflections.add(AddAttachedPronoun(verbBuilder2.append("ی").toString(), inflection));
            }
        }
        return lstInflections;
    }

    private static  Vector<String> GetPayehFelInflections(VerbInflection inflection)
    {
        Vector<String> lstInflections = new  Vector<String>();
        if (inflection.Positivity== TensePositivity.POSITIVE)
                lstInflections.add(inflection.VerbRoot.Prefix+ inflection.VerbRoot.PastTenseRoot + "ه");
        else if (inflection.Positivity== TensePositivity.NEGATIVE)          {
                if (inflection.VerbRoot.PastRootVowelStart .equals("A" ) && inflection.Positivity == TensePositivity.NEGATIVE)
                {
                    StringBuilder verbBuilder = new StringBuilder();
                    verbBuilder.append(inflection.VerbRoot.Prefix + "ن");
                    if (!inflection.VerbRoot.PastTenseRoot.startsWith("آ"))
                        verbBuilder.append("ی");
                    else
                        verbBuilder.append("یا");
                    verbBuilder.append(inflection.VerbRoot.PastTenseRoot.substring(1));
                    verbBuilder.append("ه");
                    lstInflections.add(verbBuilder.toString());

                }
                else
                {
                    lstInflections.add(inflection.VerbRoot.Prefix + "ن" + inflection.VerbRoot.PastTenseRoot + "ه");
                }
        }
        return lstInflections;
    }

    private static String AddAttachedPronoun(String verb, VerbInflection inflection)
    {
        String inflectedVerb = verb;
        if (inflection.ZamirPeyvasteh== AttachedPronounType.THIRD_PERSON_SINGULAR){
                if (verb.endsWith("آ") || verb.endsWith("ا") || verb.endsWith("و"))
                {
                    inflection.AttachedPronounString = "یش";
                    inflectedVerb += "یش";
                }
                else if (verb.endsWith("ه") && !verb.endsWith("اه") && !verb.endsWith("وه"))
                {
                    inflection.AttachedPronounString = "‌اش";
                    inflectedVerb += "‌اش";
                }
                else if (verb.endsWith("ی") && !verb.endsWith("ای") && !verb.endsWith("وی"))
                {
                    inflection.AttachedPronounString = "‌اش";
                    inflectedVerb += "‌اش";
                }
                else if (verb.endsWith("‌ای"))
                {
                    inflection.AttachedPronounString = "‌اش";
                    inflectedVerb += "‌اش";
                }
                else
                {
                    inflection.AttachedPronounString = "ش";
                    inflectedVerb += "ش";
                }
        }
            else  if (inflection.ZamirPeyvasteh== AttachedPronounType.THIRD_PERSON_PLURAL){
                if (verb.endsWith("آ") || verb.endsWith("ا") || verb.endsWith("و"))
                {
                    inflection.AttachedPronounString = "یشان";
                    inflectedVerb += "یشان";
                }
                else if (verb.endsWith("ه") && !verb.endsWith("اه") && !verb.endsWith("وه"))
                {
                    inflection.AttachedPronounString = "‌شان";
                    inflectedVerb += "‌شان";
                }
                else
                {
                    inflection.AttachedPronounString = "شان";
                    inflectedVerb += "شان";
                }
        }
        else  if (inflection.ZamirPeyvasteh== AttachedPronounType.SECOND_PERSON_PLURAL){
                if (verb.endsWith("آ") || verb.endsWith("ا") || verb.endsWith("و"))
                {
                    inflection.AttachedPronounString = "یتان";
                    inflectedVerb += "یتان";
                }
                else if (verb.endsWith("ه") && !verb.endsWith("اه") && !verb.endsWith("وه"))
                {
                    inflection.AttachedPronounString = "‌تان";
                    inflectedVerb += "‌تان";
                }
                else
                {
                    inflection.AttachedPronounString = "تان";
                    inflectedVerb += "تان";
                }
        }
        else  if (inflection.ZamirPeyvasteh== AttachedPronounType.SECOND_PERSON_SINGULAR){
                if (verb.endsWith("آ") || verb.endsWith("ا") || verb.endsWith("و"))
                {
                    inflection.AttachedPronounString = "یت";
                    inflectedVerb += "یت";
                }
                else if (verb.endsWith("ه") && !verb.endsWith("اه") && !verb.endsWith("وه"))
                {
                    inflection.AttachedPronounString = "‌ات";
                    inflectedVerb += "‌ات";
                }
                else if (verb.endsWith("ی") && !verb.endsWith("ای") && !verb.endsWith("وی"))
                {
                    inflection.AttachedPronounString = "‌ات";
                    inflectedVerb += "‌ات";
                }
                else if (verb.endsWith("‌ای"))
                {
                    inflection.AttachedPronounString = "‌ات";
                    inflectedVerb += "‌ات";
                }
                else
                {
                    inflection.AttachedPronounString = "ت";
                    inflectedVerb += "ت";
                }
        }
        else  if (inflection.ZamirPeyvasteh== AttachedPronounType.FIRST_PERSON_PLURAL){
                if (verb.endsWith("آ") || verb.endsWith("ا") || verb.endsWith("و"))
                {
                    inflection.AttachedPronounString = "یمان";
                    inflectedVerb += "یمان";
                }
                else if (verb.endsWith("ه") && !verb.endsWith("اه") && !verb.endsWith("وه"))
                {
                    inflection.AttachedPronounString = "‌مان";
                    inflectedVerb += "‌مان";
                }
                else
                {
                    inflection.AttachedPronounString = "مان";
                    inflectedVerb += "مان";
                }
        }
        else  if (inflection.ZamirPeyvasteh== AttachedPronounType.FIRST_PERSON_SINGULAR){
                if (verb.endsWith("آ") || verb.endsWith("ا") || verb.endsWith("و"))
                {
                    inflection.AttachedPronounString = "یم";
                    inflectedVerb += "یم";
                }
                else if (verb.endsWith("ه") && !verb.endsWith("اه") && !verb.endsWith("وه"))
                {
                    inflection.AttachedPronounString = "‌ام";
                    inflectedVerb += "‌ام";
                }
                else if (verb.endsWith("ی") && !verb.endsWith("ای") && !verb.endsWith("وی"))
                {
                    inflection.AttachedPronounString = "‌ام";
                    inflectedVerb += "‌ام";
                }
                else if (verb.endsWith("‌ای"))
                {
                    inflection.AttachedPronounString = "‌ام";
                    inflectedVerb += "‌ام";
                }
                else
                {
                    inflection.AttachedPronounString = "م";
                    inflectedVerb += "م";
                }
        }
        return inflectedVerb;
    }
}

