package VerbInflector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by Mohammad Sadegh Rasooli.
 * User: Mohammad Sadegh Rasooli
 * Date: 5/29/14
 * Time: 11:19 PM
 * To report any bugs or problems contact rasooli@cs.columbia.edu
 */

public class SentenceAnalyzer {
    private static String verbDicPath;

    public SentenceAnalyzer(String dicPath)
    {
        verbDicPath = dicPath;
    }

    public void ResetVerbDicPath(String newPath)
    {
        verbDicPath = newPath;
    }

    /***
   This method segments the verb into its parts
    @param  withPlus If set to true with plus, this will add plus before prefixes and after suffixes.
    **/
    public static String naturalsegment(DependencyBasedToken verbtoken, boolean withPlus)
    {
        if (verbtoken.MorphoSyntacticFeats.TenseMoodAspect == TenseFormationType.TenseFormationType_NONE && verbtoken.ChasbidegiType == Chasbidegi.PREV)
            return "+" + verbtoken.WordForm;
        if (verbtoken.MorphoSyntacticFeats.TenseMoodAspect == TenseFormationType.TenseFormationType_NONE)
            return verbtoken.WordForm;
        String kardan = "کرد#کن";
        MorphoSyntacticFeatures feat = verbtoken.MorphoSyntacticFeats;
        String lemma = verbtoken.Lemma;
        PersonType person = feat.Person;
        TenseFormationType tma = feat.TenseMoodAspect;
        TensePositivity posit = feat.Positivity;
        TensePassivity voice = feat.Voice;
        String prefix = "";
        String wordForm = verbtoken.WordForm.replace("می‌", "می");
        String[] wordformsplit = wordForm.split(" ");

        String[] splitedlemma = lemma.split("#");
        if (splitedlemma.length == 3)
            prefix = splitedlemma[0];

        String present = "";
        String past = "";

        if (splitedlemma.length >= 2)
        {
            present = splitedlemma[splitedlemma.length - 1];
            past = splitedlemma[splitedlemma.length - 2];
        }
        else
        {
            if (lemma.indexOf("#") == 0)
                present = lemma.replace("#", "");
            else
                past = lemma.replace("#", "");
        }
        String corelemma = past + "#" + present;
        String output = "";
        if(tma== TenseFormationType.AAYANDEH)
            {
                if (voice == TensePassivity.PASSIVE)
                {
                    if (! corelemma .equals( kardan))
                    {
                        output = "";
                        int prefixindex = 0;
                        if (!prefix.equals(""))
                        {
                            prefixindex = prefix.length();
                            output = wordformsplit[0].substring(0, prefixindex) + "+ ";
                        }

                        output += wordformsplit[0].substring(prefixindex, wordformsplit[0].length() - prefixindex - 1) + " +" + "ه ";

                        int index = wordformsplit[1].indexOf("خواه");
                        if (index > 0)
                            output += wordformsplit[1].substring(0, index) + "+ ";
                        output += "خواه";
                        output += " +" + wordformsplit[1].substring(index + 4);
                        output += " " + wordformsplit[2];
                    }
                    else
                    {
                        output = "";
                        int index = wordformsplit[0].indexOf("خواه");
                        if (index > 0)
                            output += wordformsplit[0].substring(0, index) + "+ ";
                        output += "خواه";
                        output += " +" + wordformsplit[0].substring(index + 4);
                        output += " " + wordformsplit[1];
                    }
                }
                else
                {
                    output = "";
                    int prefixindex = 0;
                    if (!prefix.equals(""))
                    {
                        prefixindex = prefix.length();
                        output = wordformsplit[0].substring(0, prefixindex) + "+ ";
                    }
                    int index = wordformsplit[0].indexOf("خواه");
                    if (prefixindex < index)
                        output += wordformsplit[0].substring(prefixindex, index - prefixindex) + "+ ";
                    output += wordformsplit[0].substring(index, 4);
                    output += " +" + wordformsplit[0].substring(index + 4);
                    output += " " + wordformsplit[1];

                }
            }
       else if(tma==TenseFormationType.AMR)
            {
                output = "";
                int prefixindex = 0;
                if (!prefix .equals( ""))
                {
                    prefixindex = prefix.length();
                    output += verbtoken.WordForm.substring(0, prefixindex) + "+ ";
                }
                int lemmaindex = verbtoken.WordForm.indexOf(present.substring(1, present.length() - 1));
                if (prefixindex < lemmaindex - 1)
                {
                    output += verbtoken.WordForm.substring(prefixindex, lemmaindex - prefixindex - 1) + "+ ";
                }
                output += present;
                if (lemmaindex + present.length() - 1 < verbtoken.WordForm.length() - 1)
                    output += verbtoken.WordForm.substring(lemmaindex + present.length() - 1);
            }
        else if(tma== TenseFormationType.GOZASHTEH_ABAD)
            {
                if (voice == TensePassivity.ACTIVE)
                {
                    output = "";
                    if (!prefix.equals(""))
                        output += prefix + "+ ";
                    if (posit == TensePositivity.NEGATIVE)
                        output += "ن+ ";
                    output += past;
                    output += " +ه";
                    output += " ";
                    output += "بود";
                    output += " +ه";
                    int pastindex = wordForm.indexOf("بود");
                    if (!wordForm.endsWith("است"))
                        output += " +" + wordForm.substring(pastindex + 4).replace("‌", "");
                    else
                        output += " است";
                }
                else
                {
                    if (! corelemma .equals( kardan))
                    {
                        output = "";
                        if (!prefix.equals(""))
                            output += prefix + "+ ";

                        output += past;
                        output += " +ه";
                        output += " ";
                        if (posit == TensePositivity.NEGATIVE)
                            output += "ن+ ";
                        output += "شد";
                        output += " +ه";
                        output += " ";
                        output += "بود";
                        output += " +ه";
                        int pastindex = wordForm.indexOf("بود");
                        if (!wordForm.endsWith("است"))
                            output += " +" + wordForm.substring(pastindex + 4).replace("‌", "");
                        else
                            output += " است";
                    }
                    else
                    {
                        output = "";
                        if (!prefix.equals(""))
                            output += prefix + "+ ";
                        if (posit == TensePositivity.NEGATIVE)
                            output += "ن+ ";
                        output += "شد";
                        output += " +ه";
                        output += " ";
                        output += "بود";
                        output += " +ه";
                        int pastindex = wordForm.indexOf("بود");
                        if (!wordForm.endsWith("است"))
                            output += " +" + wordForm.substring(pastindex + 4).replace("‌", "");
                        else
                            output += " است";
                    }
                }
            }
        else if(tma== TenseFormationType.GOZASHTEH_BAEED)
            {
                if (voice == TensePassivity.ACTIVE)
                {
                    output = "";
                    if (!prefix.equals(""))
                        output += prefix + "+ ";
                    if (posit == TensePositivity.NEGATIVE)
                        output += "ن+ ";
                    output += past;
                    output += " +ه";
                    output += " ";
                    output += "بود";
                    int pastindex = wordForm.indexOf("بود");
                    if (pastindex + 3 < wordForm.length())
                        output += " +" + wordForm.substring(pastindex + 3);
                }
                else
                {
                    if (! corelemma .equals( kardan))
                    {
                        output = "";
                        if (!prefix.equals(""))
                            output += prefix + "+ ";

                        output += past;
                        output += " +ه";
                        output += " ";
                        if (posit == TensePositivity.NEGATIVE)
                            output += "ن+ ";
                        output += "شد";
                        output += " +ه";
                        output += " ";
                        output += "بود";
                        int pastindex = wordForm.indexOf("بود");
                        if (pastindex + 3 < wordForm.length())
                            output += " +" + wordForm.substring(pastindex + 3);
                    }
                    else
                    {
                        output = "";
                        if (!prefix.equals(""))
                            output += prefix + "+ ";
                        if (posit == TensePositivity.NEGATIVE)
                            output += "ن+ ";
                        output += "شد";
                        output += " +ه";
                        output += " ";
                        output += "بود";
                        int pastindex = wordForm.indexOf("بود");
                        if (pastindex + 3 < wordForm.length())
                            output += " +" + wordForm.substring(pastindex + 3);
                    }
                }
            }
        else if(tma== TenseFormationType.GOZASHTEH_ELTEZAMI)
            {
                if (voice == TensePassivity.ACTIVE)
                {
                    output = "";
                    if (!prefix.equals(""))
                        output += prefix + "+ ";
                    if (posit == TensePositivity.NEGATIVE)
                        output += "ن+ ";
                    output += past + " ";
                    output += "+ه ";
                    output += "باش";
                    int bashindex = wordformsplit[1].indexOf("باش");
                    if (bashindex + 3 < wordformsplit[1].length())
                        output += " +" + wordformsplit[1].substring(bashindex + 3);
                }

                else
                {
                    if (! corelemma .equals( kardan))
                    {
                        output = "";
                        if (!prefix.equals(""))
                            output += prefix + "+ ";

                        output += past + " ";
                        output += "+ه ";
                        if (posit == TensePositivity.NEGATIVE)
                            output += "ن+ ";
                        output += "شد" + " ";
                        output += "+ه ";
                        output += "باش";
                        int bashindex = wordformsplit[2].indexOf("باش");
                        if (bashindex + 3 < wordformsplit[2].length())
                            output += " +" + wordformsplit[2].substring(bashindex + 3);
                    }
                    else
                    {
                        output = "";
                        if (!prefix.equals(""))
                            output += prefix + "+ ";

                        if (posit == TensePositivity.NEGATIVE)
                            output += "ن+ ";
                        output += "شد" + " ";
                        output += "+ه ";
                        output += "باش";
                        int bashindex = wordformsplit[1].indexOf("باش");
                        if (bashindex + 3 < wordformsplit[1].length())
                            output += " +" + wordformsplit[1].substring(bashindex + 3);
                    }
                }
            }
        else if(tma== TenseFormationType.GOZASHTEH_ESTEMRAARI)
            {
                if (voice == TensePassivity.ACTIVE)
                {
                    int miIndex = wordForm.indexOf("می");
                    if (miIndex >= 0)
                    {
                        int prefixindex = 0;
                        if (!prefix.equals(""))
                        {
                            output += prefix + "+ ";
                        }

                        if (posit == TensePositivity.NEGATIVE)
                            output += "ن+ ";
                        output += "می+ ";
                        int pastindex = wordForm.indexOf(past);
                        output += past;
                        if (pastindex + past.length() < wordForm.length())
                            output += " +" + wordForm.substring(pastindex + past.length());
                    }
                }
                else
                {
                    if (! corelemma .equals( kardan))
                    {
                        output = "";
                        int prefixindex = 0;
                        if (!prefix.equals(""))
                        {
                            prefixindex = prefix.length();
                            output = wordformsplit[0].substring(0, prefixindex) + "+ ";
                        }

                        output += wordformsplit[0].substring(prefixindex, wordformsplit[0].length() - prefixindex - 1) + " +" + "ه ";



                        if (posit == TensePositivity.NEGATIVE)
                            output += "ن+ ";
                        output += "می+ ";
                        output += "شد";
                        int pastindex = wordformsplit[1].indexOf("شد");
                        if (pastindex + 2 < wordformsplit[1].length())
                            output += " +" + wordformsplit[1].substring(pastindex + 2);
                    }
                    else
                    {
                        int miIndex = wordForm.indexOf("می");
                        if (miIndex >= 0)
                        {
                            int prefixindex = 0;
                            if (!prefix.equals(""))
                            {
                                prefixindex = prefix.length();
                                output += prefix + "+ ";
                            }


                            if (posit == TensePositivity.NEGATIVE)
                                output += "ن+ ";
                            output += "می+ ";
                            int pastindex = wordForm.indexOf("شد");

                            output += "شد";
                            if (pastindex + 2 < wordForm.length())
                                output += " +" + wordForm.substring(pastindex + 2);
                        }
                    }
                }
            }
        else if(tma== TenseFormationType.GOZASHTEH_NAGHLI_ESTEMRAARI)
            {
                if (voice == TensePassivity.ACTIVE)
                {
                    output = "";
                    if (!prefix.equals(""))
                        output += prefix + "+ ";
                    if (posit == TensePositivity.NEGATIVE)
                        output += "ن+ ";
                    output += "می+ ";
                    output += past;
                    output += " +ه";
                    int pastindex = wordForm.indexOf(past);
                    if (!wordForm.endsWith("است"))
                        output += " +" + wordForm.substring(pastindex + 1 + past.length()).replace("‌", "");
                    else
                        output += " است";
                }
                else
                {
                    if (! corelemma .equals( kardan))
                    {
                        output = "";
                        if (!prefix.equals(""))
                            output += prefix + "+ ";

                        output += past;
                        output += " +ه";
                        output += " ";
                        if (posit == TensePositivity.NEGATIVE)
                            output += "ن+ ";
                        output += "می+ ";
                        output += "شد";
                        output += " +ه";
                        int pastindex = wordForm.indexOf("شده");
                        if (pastindex + 3 < wordForm.length() - 1)
                            if (!wordForm.endsWith("است"))
                                output += " +" + wordForm.substring(pastindex + 4).replace("‌", "");
                            else
                                output += " است";
                    }
                    else
                    {
                        output = "";
                        if (!prefix.equals(""))
                            output += prefix + "+ ";

                        if (posit == TensePositivity.NEGATIVE)
                            output += "ن+ ";
                        output += "می+ ";
                        output += "شد";
                        output += " +ه";
                        int pastindex = wordForm.indexOf("شده");
                        if (pastindex + 3 < wordForm.length() - 1 && !wordForm.endsWith("است"))
                            output += " +" + wordForm.substring(pastindex + 4).replace("‌", "");
                        else if (pastindex + 3 < wordForm.length() - 1)
                            output += " است";
                    }
                }
            }
        else if(tma== TenseFormationType.GOZASHTEH_NAGHLI_SADEH)
            {
                if (voice == TensePassivity.ACTIVE)
                {
                    output = "";
                    if (!prefix.equals(""))
                        output += prefix + "+ ";
                    if (posit == TensePositivity.NEGATIVE)
                        output += "ن+ ";
                    output += past;
                    output += " +ه";
                    int pastindex = wordForm.indexOf(past);
                    if (pastindex + past.length() + 1 < wordForm.length())
                        if (!wordForm.endsWith("است"))
                            output += " +" + wordForm.substring(pastindex + 1 + past.length()).replace("‌", "");
                        else
                            output += " است";
                }
                else
                {
                    if (! corelemma .equals( kardan))
                    {
                        output = "";
                        if (!prefix.equals(""))
                            output += prefix + "+ ";

                        output += past;
                        output += " +ه";
                        output += " ";
                        if (posit == TensePositivity.NEGATIVE)
                            output += "ن+ ";
                        output += "شد";
                        output += " +ه";
                        int pastindex = wordForm.indexOf("شده");
                        if (pastindex + 3 < wordForm.length())
                            if (pastindex + 3 < wordForm.length() && !wordForm.endsWith("است"))
                                output += " +" + wordForm.substring(pastindex + 4).replace("‌", "");
                            else
                                output += " است";
                    }
                    else
                    {
                        output = "";
                        if (!prefix.equals(""))
                            output += prefix + "+ ";

                        if (posit == TensePositivity.NEGATIVE)
                            output += "ن+ ";
                        output += "شد";
                        output += " +ه";
                        int pastindex = wordForm.indexOf("شده");
                        if (pastindex + 3 < wordForm.length())
                            if (pastindex + 3 < wordForm.length() && !wordForm.endsWith("است"))
                                output += " +" + wordForm.substring(pastindex + 4).replace("‌", "");
                            else
                                output += " است";
                    }
                }
            }
        else if(tma== TenseFormationType.GOZASHTEH_SADEH)
            {
                if (voice == TensePassivity.ACTIVE)
                {
                    int prefixindex = 0;
                    if (!prefix.equals(""))
                    {
                        prefixindex = prefix.length();
                        output += verbtoken.WordForm.substring(0, prefixindex) + "+ ";
                    }
                    int pastindex = verbtoken.WordForm.indexOf(past);
                    if (prefixindex < pastindex)
                        output += verbtoken.WordForm.substring(prefixindex, pastindex - prefixindex) + "+ ";
                    output += past;
                    if (pastindex + past.length() < wordForm.length())
                        output += " +" + verbtoken.WordForm.substring(pastindex + past.length());
                }
                else
                {
                    if (! corelemma .equals( kardan))
                    {
                        output = "";
                        int prefixindex = 0;
                        if (!prefix.equals(""))
                        {
                            prefixindex = prefix.length();
                            output = wordformsplit[0].substring(0, prefixindex) + "+ ";
                        }

                        output += wordformsplit[0].substring(prefixindex, wordformsplit[0].length() - prefixindex - 1) + " +" + "ه ";



                        if (posit == TensePositivity.NEGATIVE)
                            output += "ن+ ";
                        output += "شد";
                        int pastindex = wordformsplit[1].indexOf("شد");
                        if (pastindex + 2 < wordformsplit[1].length())
                            output += " +" + wordformsplit[1].substring(pastindex + 2);
                    }
                    else
                    {
                        int prefixindex = 0;
                        if (!prefix.equals(""))
                        {
                            prefixindex = prefix.length();
                            output += wordForm.substring(0, prefixindex) + "+ ";
                        }

                        if (posit == TensePositivity.NEGATIVE)
                            output += "ن+";
                        int pastindex = wordForm.indexOf("شد");
                        output += "شد";
                        if (pastindex + 2 < wordForm.length())
                            output += " +" + wordForm.substring(pastindex + 2);

                    }
                }
            }
        else if(tma== TenseFormationType.HAAL_ELTEZAMI)
            {
                if (voice == TensePassivity.ACTIVE)
                {
                    int prefixindex = 0;
                    if (!prefix.equals(""))
                    {
                        prefixindex = prefix.length();
                        output += verbtoken.WordForm.substring(0, prefixindex) + "+ ";
                    }
                    if (posit == TensePositivity.NEGATIVE)
                        output += "ن+ ";
                    if (posit == TensePositivity.POSITIVE && !present.startsWith("ب") && wordForm.startsWith("ب"))
                        output += "ب+ ";
                    int presentindex = verbtoken.WordForm.indexOf(present.substring(1, present.length() - 1));

                    output += present;
                    output += " +" + verbtoken.WordForm.substring(presentindex + present.length() - 1);
                }
                else
                {
                    if (! corelemma .equals( kardan))
                    {
                        output = "";
                        int prefixindex = 0;
                        if (!prefix.equals(""))
                        {
                            prefixindex = prefix.length();
                            output = wordformsplit[0].substring(0, prefixindex) + "+ ";
                        }

                        output += wordformsplit[0].substring(prefixindex, wordformsplit[0].length() - prefixindex - 1) + " +" + "ه ";



                        if (posit == TensePositivity.NEGATIVE)
                            output += "ن+ ";
                        output += "شو ";
                        int presentindex = wordformsplit[1].indexOf("شو");
                        if (presentindex + 2 < wordformsplit[1].length())
                            output += " +" + wordformsplit[1].substring(presentindex + 2);
                    }
                    else
                    {
                        int prefixindex = 0;
                        if (!prefix.equals(""))
                        {
                            prefixindex = prefix.length();
                            output += wordForm.substring(0, prefixindex) + "+ ";
                        }

                        if (posit == TensePositivity.NEGATIVE)
                            output += "ن+";
                        int presentindex = wordForm.indexOf("شو");
                        output += "شو";
                        output += " +" + wordForm.substring(presentindex + 2);

                    }
                }
            }
        else if(tma== TenseFormationType.HAAL_SAADEH)
            {
                if (voice == TensePassivity.ACTIVE)
                {
                    int prefixindex = 0;
                    if (!prefix.equals(""))
                    {
                        prefixindex = prefix.length();
                        output += verbtoken.WordForm.substring(0, prefixindex) + "+ ";
                    }
                    int presentindex = verbtoken.WordForm.indexOf(present);
                    if (prefixindex < presentindex)
                        output += verbtoken.WordForm.substring(prefixindex, presentindex - prefixindex) + "+ ";
                    output += present;
                    if (presentindex + present.length() < wordForm.length() - 1)
                        output += " +" + verbtoken.WordForm.substring(presentindex + present.length());
                }
                else
                {
                    if (! corelemma .equals( kardan))
                    {
                        output = "";
                        int prefixindex = 0;
                        if (!prefix.equals(""))
                        {
                            prefixindex = prefix.length();
                            output = wordformsplit[0].substring(0, prefixindex) + "+ ";
                        }

                        output += wordformsplit[0].substring(prefixindex, wordformsplit[0].length() - prefixindex - 1) + " +" + "ه ";



                        if (posit == TensePositivity.NEGATIVE)
                            output += "ن+ ";
                        output += "شو";
                        int presentindex = wordformsplit[1].indexOf("شو");
                        if (presentindex + 2 < wordformsplit[1].length())
                            output += " +" + wordformsplit[1].substring(presentindex + 2);
                    }
                    else
                    {
                        int prefixindex = 0;
                        if (!prefix.equals(""))
                        {
                            prefixindex = prefix.length();
                            output += wordForm.substring(0, prefixindex) + "+ ";
                        }

                        if (posit == TensePositivity.NEGATIVE)
                            output += "ن+";
                        int presentindex = wordForm.indexOf("شو");
                        output += "شو";
                        output += " +" + wordForm.substring(presentindex + 2);

                    }
                }
            }
        else if(tma== TenseFormationType.HAAL_SAADEH_EKHBARI)
            {
                if (voice == TensePassivity.ACTIVE)
                {
                    int miIndex = wordForm.indexOf("می");
                    if (miIndex >= 0)
                    {

                        int prefixindex = 0;
                        if (!prefix.equals(""))
                        {
                            output += prefix + "+ ";
                        }

                        if (posit == TensePositivity.NEGATIVE)
                            output += "ن+ ";
                        output += "می+ ";
                        int presentindex = wordForm.indexOf(present);
                        output += present;
                        output += " +" + wordForm.substring(presentindex + present.length());
                    }
                    else
                    {
                        if (wordForm .equals("است") )
                            return wordForm;
                        else if (wordForm.startsWith("نیست"))
                        {
                            output = "نی+ ";
                            output += "ست";
                            if (wordForm.length() > 4)
                                output += " +" + wordForm.substring(4);
                        }
                        else if (wordForm.startsWith("هست"))
                        {
                            output += "هست";
                            if (wordForm.length() > 3)
                                output += " +" + wordForm.substring(3);
                        }

                    }
                }
                else
                {
                    if (! corelemma .equals( kardan))
                    {
                        output = "";
                        int prefixindex = 0;
                        if (!prefix.equals(""))
                        {
                            prefixindex = prefix.length();
                            output = wordformsplit[0].substring(0, prefixindex) + "+ ";
                        }

                        output += wordformsplit[0].substring(prefixindex, wordformsplit[0].length() - prefixindex - 1) + " +" + "ه ";



                        if (posit == TensePositivity.NEGATIVE)
                            output += "ن+ ";
                        output += "می+ ";
                        output += "شو ";
                        int presentindex = wordformsplit[1].indexOf("شو");
                        if (presentindex + 2 < wordformsplit[1].length())
                            output += " +" + wordformsplit[1].substring(presentindex + 2);
                    }
                    else
                    {
                        int miIndex = wordForm.indexOf("می");
                        if (miIndex >= 0)
                        {
                            int prefixindex = 0;
                            if (!prefix.equals(""))
                            {
                                prefixindex = prefix.length();
                                output += prefix + "+ ";
                            }


                            if (posit == TensePositivity.NEGATIVE)
                                output += "ن+ ";
                            output += "می+ ";
                            int presentindex = wordForm.indexOf("شو");

                            output += "شو";
                            output += " +" + wordForm.substring(presentindex + 2);
                        }
                    }
                }
            }
        else if(tma== TenseFormationType.PAYEH_MAFOOLI)
            {
                output = "";
                int prefixindex = 0;
                if (!prefix.equals(""))
                {
                    prefixindex = prefix.length();
                    output += verbtoken.WordForm.substring(0, prefixindex) + "+ ";
                }
                int pastindex = verbtoken.WordForm.indexOf(past);
                if (prefixindex < pastindex)
                    output += verbtoken.WordForm.substring(prefixindex, pastindex - prefixindex) + "+ ";
                output += past;
                output += " +" + verbtoken.WordForm.substring(pastindex + past.length());
            }
        else if(tma==  TenseFormationType.TenseFormationType_NONE)
            {
                output = verbtoken.WordForm;
            }
        
        if (!withPlus)
            output = output.replace("+", "");
        return output;
    }

   /**
     makes a partial dependency tree : where only verb parts are tagged
    **/
    private static ArrayList<DependencyBasedToken> MakePartialDependencyTree(String[] sentence) throws IOException {
        ArrayList<DependencyBasedToken> tree = new ArrayList<DependencyBasedToken>();

        HashMap<Integer, KeyValuePair<String, KeyValuePair<Integer, Object>>> dic = VerbPartTagger.MakePartialTree(sentence, verbDicPath);
        int indexOfOriginalWords = 0;
        boolean addZamir = false;
        String zamirString = "";
        NumberType ZamirNumberType = NumberType.INVALID;
        PersonType ZamirShakhsType = PersonType.PERSON_NONE;
        String zamirLemma = "";
        int offset = 0;
        int realPosition = 0;

        for (Integer key : dic.keySet())
        {
            addZamir = false;
            zamirString = "";
            ZamirShakhsType = PersonType.PERSON_NONE;
            ZamirNumberType = NumberType.INVALID;
            zamirLemma = "";
            realPosition = key + 1;
            KeyValuePair<String, KeyValuePair<Integer, Object>> keyValuePair= dic.get(key);
            int position = key + 1 + offset;

            String wordForm = keyValuePair.getKey();
            int head = keyValuePair.getValue().getKey();
            String deprel = "_";
            Object obj = keyValuePair.getValue().getValue();
            String lemma = "_";
            int wordCount = wordForm.split(" ").length;
            PersonType person = PersonType.PERSON_NONE;
            NumberType number = NumberType.INVALID;
            TensePositivity posit = TensePositivity.POSITIVE;
            TensePassivity voice = TensePassivity.ACTIVE;
            TenseFormationType tma = TenseFormationType.TenseFormationType_NONE;


            indexOfOriginalWords += wordCount;



            String CPOSTag = "_";
            if (obj instanceof VerbInflection)
            {
                VerbInflection newObj = (VerbInflection)obj;
                tma = newObj.TenseForm;
                PersonType personType = newObj.Person;
                person = personType;
                number = NumberType.SINGULAR;
                posit = newObj.Positivity;
                voice = newObj.Passivity;
                if (personType == PersonType.FIRST_PERSON_PLURAL || personType == PersonType.SECOND_PERSON_PLURAL || personType == PersonType.THIRD_PERSON_PLURAL)
                {
                    number = NumberType.PLURAL;
                }
                lemma = newObj.VerbRoot.SimpleToString();
                CPOSTag = "V";

                if (newObj.ZamirPeyvasteh != AttachedPronounType.AttachedPronoun_NONE)
                {
                    addZamir = true;
                    zamirString = newObj.AttachedPronounString;
                    offset++;
                    if(newObj.ZamirPeyvasteh== AttachedPronounType.FIRST_PERSON_PLURAL){
                            ZamirNumberType = NumberType.PLURAL;
                            ZamirShakhsType = PersonType.FIRST_PERSON_PLURAL;
                            zamirLemma = "مان";
                    }
                       else  if(newObj.ZamirPeyvasteh==  AttachedPronounType.FIRST_PERSON_SINGULAR){
                            ZamirNumberType = NumberType.SINGULAR;
                            ZamirShakhsType = PersonType.FIRST_PERSON_SINGULAR;
                            zamirLemma = "م";
                    }
                    else  if(newObj.ZamirPeyvasteh==  AttachedPronounType.SECOND_PERSON_PLURAL){
                            ZamirNumberType = NumberType.PLURAL;
                            ZamirShakhsType = PersonType.SECOND_PERSON_PLURAL;
                            zamirLemma = "تان";
                    }
                    else  if(newObj.ZamirPeyvasteh==  AttachedPronounType.SECOND_PERSON_SINGULAR){
                            ZamirNumberType = NumberType.SINGULAR;
                            ZamirShakhsType = PersonType.SECOND_PERSON_SINGULAR;
                            zamirLemma = "ت";
                    }
                    else  if(newObj.ZamirPeyvasteh==  AttachedPronounType.THIRD_PERSON_PLURAL){
                            ZamirNumberType = NumberType.PLURAL;
                            ZamirShakhsType = PersonType.THIRD_PERSON_PLURAL;
                            zamirLemma = "شان";
                    }
                    else  if(newObj.ZamirPeyvasteh==  AttachedPronounType.THIRD_PERSON_SINGULAR){
                            ZamirNumberType = NumberType.SINGULAR;
                            ZamirShakhsType = PersonType.THIRD_PERSON_SINGULAR;
                            zamirLemma = "ش";
                    
                    }
                }

            }
            if (obj instanceof MostamarSaz)
            {
                MostamarSaz newObj = (MostamarSaz)obj;
                deprel = "PROG";
                lemma = "داشت#دار";

                VerbInflection headObj = (VerbInflection)((KeyValuePair<String, KeyValuePair<Integer, Object>>)dic.values().toArray()[head]).getValue().getValue();  //todo it is changed from elementAt
                person = headObj.Person;
                number = NumberType.SINGULAR;
                PersonType personType = headObj.Person;
                if (personType == PersonType.FIRST_PERSON_PLURAL || personType == PersonType.SECOND_PERSON_PLURAL || personType == PersonType.THIRD_PERSON_PLURAL)
                {
                    number = NumberType.PLURAL;
                }
                tma = TenseFormationType.HAAL_SAADEH;
                if (newObj.Type .equals("MOSTAMAR_SAAZ_GOZASHTEH"))
                    tma = TenseFormationType.GOZASHTEH_SADEH;
                CPOSTag = "V";
            }
            if (obj instanceof String)
            {
                String newObj = (String)obj;
                if (newObj .equals( "POSDEP"))
                {
                    deprel = newObj;
                }
                else if (newObj .equals( "VERBAL-PREPOSIOTION") )
                {
                    deprel = "VPRT";
                }
                else if (newObj .equals( "NON-VERBAL-ELEMENT")       )
                {
                    deprel = "NVE";
                }
                else if (newObj .equals( "MOSTAMAR_SAAZ_HAAL") || newObj .equals( "MOSTAMAR_SAAZ_GOZASHTEH"))
                {
                    deprel = "PROG";
                    lemma = "داشت#دار";

                    VerbInflection headObj = (VerbInflection)((KeyValuePair<String, KeyValuePair<Integer, Object>>)dic.values().toArray()[head]).getValue().getValue();  //todo it is changed from elementAt
                    person = headObj.Person;
                    number = NumberType.SINGULAR;
                    PersonType personType = headObj.Person;
                    if (personType == PersonType.FIRST_PERSON_PLURAL || personType == PersonType.SECOND_PERSON_PLURAL || personType == PersonType.THIRD_PERSON_PLURAL)
                    {
                        number = NumberType.PLURAL;
                    }
                    tma = TenseFormationType.HAAL_SAADEH;
                    if (newObj .equals("MOSTAMAR_SAAZ_GOZASHTEH"))
                        tma = TenseFormationType.GOZASHTEH_SADEH;
                    CPOSTag = "V";
                }
            }
            if (!addZamir)
            {

                MorphoSyntacticFeatures mfeat = new MorphoSyntacticFeatures(number, person, tma, posit, voice);
                DependencyBasedToken dependencyToken = new DependencyBasedToken(position, wordForm, lemma, CPOSTag, "_", head, deprel, wordCount,
                        mfeat, Chasbidegi.TANHA);
                tree.add(dependencyToken);
            }
            else
            {
                MorphoSyntacticFeatures mfeat1 = new MorphoSyntacticFeatures(number, person, tma, posit, voice);
                MorphoSyntacticFeatures mfeat2 = new MorphoSyntacticFeatures(ZamirNumberType, ZamirShakhsType, TenseFormationType.TenseFormationType_NONE, TensePositivity.POSITIVE, TensePassivity.ACTIVE);
                DependencyBasedToken dependencyToken1 = new DependencyBasedToken(position, wordForm.substring(0, wordForm.length() - zamirString.length()), lemma, CPOSTag, "_",
                        head, deprel, wordCount,
                        mfeat1, Chasbidegi.NEXT);
                DependencyBasedToken dependencyToken2 = new DependencyBasedToken(position + 1, zamirString, zamirLemma, "CPR", "CPR",
                        position, "OBJ", 1,
                        mfeat2, Chasbidegi.PREV);
                tree.add(dependencyToken1);
                tree.add(dependencyToken2);
            }
        }
        return tree;
    }

    /***
     makes a partial dependency tree : where only verb parts are tagged
      You need to have a POS tagger and a lemmatizer
     The tagset is similar to Bijankhan corpus
    **/
    private static ArrayList<DependencyBasedToken> MakePartialDependencyTree(String[] sentence, String[] posSentence, String[] lemmas, MorphoSyntacticFeatures[] morphoSyntacticFeatureses) throws IOException {
        ArrayList<DependencyBasedToken> tree = new ArrayList<DependencyBasedToken>();
        String[] outpos=new String[10];
        HashMap<Integer, KeyValuePair<String, KeyValuePair<Integer, Object>>> dic = VerbPartTagger.MakePartialTree(sentence, posSentence, outpos, lemmas, verbDicPath);
        int indexOfOriginalWords = 0;
        boolean addZamir = false;
        String zamirString = "";
        NumberType ZamirNumberType = NumberType.INVALID;
        PersonType ZamirShakhsType = PersonType.PERSON_NONE;
        String zamirLemma = "";
        int offset = 0;
        int realPosition = 0;
        for (int key : dic.keySet())
        {
            KeyValuePair<Integer, KeyValuePair<String, KeyValuePair<Integer, Object>>> keyValuePair=new KeyValuePair<Integer, KeyValuePair<String, KeyValuePair<Integer, Object>>>(key,dic.get(key));
            addZamir = false;
            zamirString = "";
            ZamirShakhsType = PersonType.PERSON_NONE;
            ZamirNumberType = NumberType.INVALID;
            zamirLemma = "";
            realPosition = keyValuePair.getKey() + 1;
            int position = keyValuePair.getKey() + 1 + offset;
            String wordForm = keyValuePair.getValue().getKey();
            int head = keyValuePair.getValue().getValue().getKey();
            String deprel = "_";
            Object obj = keyValuePair.getValue().getValue().getValue();
            String lemma = "_";
            String FPOS = "_";
            int wordCount = wordForm.split(" ").length;
            PersonType person = PersonType.PERSON_NONE;
            NumberType number = NumberType.INVALID;
            TenseFormationType tma = TenseFormationType.TenseFormationType_NONE;
            TensePositivity posit = TensePositivity.POSITIVE;
            TensePassivity voice = TensePassivity.ACTIVE;
            if (wordCount == 1)
            {
                lemma = lemmas[indexOfOriginalWords];
                person = morphoSyntacticFeatureses[indexOfOriginalWords].Person;
                number = morphoSyntacticFeatureses[indexOfOriginalWords].Number;
                tma = morphoSyntacticFeatureses[indexOfOriginalWords].TenseMoodAspect;
                posit = morphoSyntacticFeatureses[indexOfOriginalWords].Positivity;
            }
            indexOfOriginalWords += wordCount;

            if (obj instanceof VerbInflection)
            {
                VerbInflection newObj = (VerbInflection)obj;
                tma = newObj.TenseForm;
                person = newObj.Person;
                posit = newObj.Positivity;
                voice = newObj.Passivity;
                if (newObj.Passivity == TensePassivity.ACTIVE)
                {
                    FPOS = "ACT";
                }
                else
                {
                    FPOS = "PASS";
                }

                if (newObj.ZamirPeyvasteh != AttachedPronounType.AttachedPronoun_NONE)
                {
                    addZamir = true;
                    zamirString = newObj.AttachedPronounString;
                    offset++;
                if(newObj.ZamirPeyvasteh== AttachedPronounType.FIRST_PERSON_PLURAL){
                            ZamirNumberType = NumberType.PLURAL;
                            ZamirShakhsType = PersonType.FIRST_PERSON_PLURAL;
                            zamirLemma = "مان";
                }
                        else  if(newObj.ZamirPeyvasteh==  AttachedPronounType.FIRST_PERSON_SINGULAR){
                            ZamirNumberType = NumberType.SINGULAR;
                            ZamirShakhsType = PersonType.FIRST_PERSON_SINGULAR;
                            zamirLemma = "م";
                }
                else  if(newObj.ZamirPeyvasteh==  AttachedPronounType.SECOND_PERSON_PLURAL){
                            ZamirNumberType = NumberType.PLURAL;
                            ZamirShakhsType = PersonType.SECOND_PERSON_PLURAL;
                            zamirLemma = "تان";
                }
                else  if(newObj.ZamirPeyvasteh== AttachedPronounType.SECOND_PERSON_SINGULAR){
                            ZamirNumberType = NumberType.SINGULAR;
                            ZamirShakhsType = PersonType.SECOND_PERSON_SINGULAR;
                            zamirLemma = "ت";
                }
                else  if(newObj.ZamirPeyvasteh==  AttachedPronounType.THIRD_PERSON_PLURAL){
                            ZamirNumberType = NumberType.PLURAL;
                            ZamirShakhsType = PersonType.THIRD_PERSON_PLURAL;
                            zamirLemma = "شان";
                }
                else  if(newObj.ZamirPeyvasteh==  AttachedPronounType.THIRD_PERSON_SINGULAR){
                            ZamirNumberType = NumberType.SINGULAR;
                            ZamirShakhsType = PersonType.THIRD_PERSON_SINGULAR;
                            zamirLemma = "ش";
                    }
                }
                number = NumberType.SINGULAR;
                if (person == PersonType.PERSON_NONE)
                {
                    number = NumberType.INVALID;
                }
                if (person == PersonType.FIRST_PERSON_PLURAL || person == PersonType.SECOND_PERSON_PLURAL || person == PersonType.THIRD_PERSON_PLURAL)
                {
                    number = NumberType.PLURAL;
                }
                lemma = newObj.VerbRoot.SimpleToString();
            }
            if (obj instanceof String)
            {
                String newObj = (String)obj;
                if (newObj .equals( "POSDEP"))
                {
                    deprel = newObj;
                }
                else if (newObj .equals( "VERBAL-PREPOSIOTION") )
                {
                    deprel = "VPRT";
                }
                else if (newObj .equals( "NON-VERBAL-ELEMENT")    )
                {
                    deprel = "NVE";
                }
                else if (newObj .equals( "MOSTAMAR_SAAZ_HAAL" )|| newObj .equals( "MOSTAMAR_SAAZ_GOZASHTEH") )
                {
                    deprel = "PROG";
                    lemma = "داشت#دار";

                    VerbInflection headObj = (VerbInflection) ((KeyValuePair<String, KeyValuePair<Integer, Object>>)dic.values().toArray()[head]).getValue().getValue();  //todo it is changed from elementAt
                    person = headObj.Person;
                    number = NumberType.SINGULAR;

                    PersonType personType = headObj.Person;
                    if (personType == PersonType.FIRST_PERSON_PLURAL || personType == PersonType.SECOND_PERSON_PLURAL || personType == PersonType.THIRD_PERSON_PLURAL)
                    {
                        number = NumberType.PLURAL;
                    }
                    tma = TenseFormationType.HAAL_SAADEH;
                    if (newObj .equals( "MOSTAMAR_SAAZ_GOZASHTEH")  )
                        tma = TenseFormationType.GOZASHTEH_SADEH;
                }
            }
            if (!addZamir)
            {
                MorphoSyntacticFeatures mfeat = new MorphoSyntacticFeatures(number, person, tma, posit, voice);
                DependencyBasedToken dependencyToken = new DependencyBasedToken(position, wordForm, lemma, outpos[realPosition - 1], FPOS,
                        head, deprel, wordCount,
                        mfeat, Chasbidegi.TANHA);
                tree.add(dependencyToken);
            }
            else
            {
                MorphoSyntacticFeatures mfeat1 = new MorphoSyntacticFeatures(number, person, tma, posit, voice);
                MorphoSyntacticFeatures mfeat2 = new MorphoSyntacticFeatures(ZamirNumberType, ZamirShakhsType, TenseFormationType.TenseFormationType_NONE, TensePositivity.POSITIVE, TensePassivity.ACTIVE);
                DependencyBasedToken dependencyToken1 = new DependencyBasedToken(position, wordForm.substring(0, wordForm.length() - zamirString.length()), lemma, outpos[realPosition - 1], FPOS,
                        head, deprel, wordCount,
                        mfeat1, Chasbidegi.NEXT);
                DependencyBasedToken dependencyToken2 = new DependencyBasedToken(position + 1, zamirString, zamirLemma, "CPR", "CPR",
                        position, "OBJ", 1,
                        mfeat2, Chasbidegi.PREV);
                tree.add(dependencyToken1);
                tree.add(dependencyToken2);
            }

        }
        return tree;
    }

    /***
     returns a verbbasedsentence
     You need to have a POS tagger and a lemmatizer
     The tagset is similar to Bijankhan corpus
     **/
    public static VerbBasedSentence MakeVerbBasedSentence(String[] sentence, String[] posSentence, String[] lemmas, MorphoSyntacticFeatures[] morphoSyntacticFeatureses) throws IOException {
        return new VerbBasedSentence(MakePartialDependencyTree(sentence, posSentence, lemmas, morphoSyntacticFeatureses));
    }

    public static VerbBasedSentence MakeVerbBasedSentence(String[] sentence) throws IOException {
        return new VerbBasedSentence(MakePartialDependencyTree(sentence));
    }

    public static VerbBasedSentence MakeVerbBasedSentence(String sentence) throws IOException {
        //todo
       // sentence = StringUtil.RefineAndFilterPersianWord(sentence);
        //todo make tokenize function
        String[] tokenized = sentence.replace("  ", " ").replace("  ", " ").split(" ");
        // var tokenized = PersianWordTokenizer.Tokenize(sentence,false);
        return MakeVerbBasedSentence(tokenized);
    }
}
