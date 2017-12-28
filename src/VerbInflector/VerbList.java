package VerbInflector;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Vector;
import java.io.*;

/**
 * Created by Mohammad Sadegh Rasooli.
 * User: Mohammad Sadegh Rasooli
 * Date: 5/29/14
 * Time: 1:28 AM
 * To report any bugs or problems contact rasooli@cs.columbia.edu
 */

public class VerbList {
    /***
        Saves all possible inflections of a special verb representation
    ***/
    public static HashMap<String, Vector<VerbInflection>> VerbShapes;

    public static  HashMap<String, Vector< String>> VerbPishvandiDic;

    /***
        A dictionay of possible complex predicates in Persian verbs
    ***/
    public static  HashMap<Verb,  HashMap< String,  HashMap< String, Boolean>>> CompoundVerbDic;

    /***
        Constructs all dictionaries used in the inflection program
    ***/
    public VerbList( String verbDicPath) throws IOException {
        VerbPishvandiDic = new  HashMap< String, Vector< String>>();
        VerbShapes = new  HashMap< String, Vector<VerbInflection>>();
        CompoundVerbDic = new  HashMap<Verb,  HashMap< String,  HashMap< String, Boolean>>>();
        Vector<Verb> verbs = new Vector<Verb>();

        BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(verbDicPath), "utf-8"));
        String record;
        int index = 0;
        int i = 0;
        while ((record = fileReader.readLine()) != null) {
             String[] fields = record.split("\t");
            int vtype = Integer.parseInt(fields[0]);

            if (vtype == 1 || vtype == 2)
            {
                VerbType verbType = VerbType.SADEH;
                if (vtype == 2)
                    verbType = VerbType.PISHVANDI;
                int trans = Integer.parseInt(fields[1]);
                VerbTransitivity transitivity = VerbTransitivity.Transitive;
                if (trans == 0)
                    transitivity = VerbTransitivity.InTransitive;
                else if (trans == 2)
                    transitivity = VerbTransitivity.BiTransitive;
                 String pishvand = "";
                if (!fields[5].equals( "-"))
                {
                    pishvand = fields[5];
                }
                Verb verb;
                boolean amrShodani = true;
                if (fields[7].equals("*"))
                    amrShodani = false;
                 String vowelEnd = fields[8];
                 String maziVowel = fields[9];
                 String mozarehVowel = fields[10];
                if (fields[3].equals("-"))
                    verb = new Verb("", fields[2], "", pishvand, "", transitivity, verbType, amrShodani,
                            vowelEnd, maziVowel, mozarehVowel);
                else if (fields[2] .equals("-"))
                    verb = new Verb("", "", fields[3], pishvand, "", transitivity, verbType, amrShodani,
                            vowelEnd, maziVowel, mozarehVowel);
                else
                    verb = new Verb("", fields[2], fields[3], pishvand, "", transitivity, verbType, amrShodani,
                            vowelEnd, maziVowel, mozarehVowel);

                verbs.add(verb);
                if (verb.Type == VerbType.PISHVANDI)
                {
                    verbs.add(new Verb("", "", "خواه", pishvand, "", VerbTransitivity.InTransitive,
                            VerbType.AYANDEH_PISHVANDI, false, "?", "@", "!"));
                    if (VerbPishvandiDic.containsKey(pishvand))
                    {
                        VerbPishvandiDic.get(pishvand).add(verb.PastTenseRoot + "|" + verb.PresentTenseRoot);
                    }
                    else
                    {
                        Vector lst = new Vector< String>();
                        lst.add(verb.PastTenseRoot + "|" + verb.PresentTenseRoot);
                        VerbPishvandiDic.put(pishvand, lst);
                    }
                }
            }
            else if (vtype == 3)
            {
                VerbType verbType = VerbType.SADEH;
                int trans = Integer.parseInt(fields[1]);
                VerbTransitivity transitivity = VerbTransitivity.Transitive;
                if (trans == 0)
                    transitivity = VerbTransitivity.InTransitive;
                else if (trans == 2)
                    transitivity = VerbTransitivity.BiTransitive;
                Verb verb;
                boolean amrShodani = true;
                 String vowelEnd = fields[8];
                 String maziVowel = fields[9];
                 String mozarehVowel = fields[10];
                 String nonVerbalElemant = fields[4];
                if (fields[3].equals("-") )
                    verb = new Verb("", fields[2], "", "", "", transitivity, verbType, amrShodani,
                            vowelEnd, maziVowel, mozarehVowel);
                else if (fields[2] .equals("-"))
                    verb = new Verb("", "", fields[3], "", "", transitivity, verbType, amrShodani,
                            vowelEnd, maziVowel, mozarehVowel);
                else
                    verb = new Verb("", fields[2], fields[3], "", "", transitivity, verbType, amrShodani,
                            vowelEnd, maziVowel, mozarehVowel);
                if (fields[7] .equals("*"))
                    amrShodani = false;
                boolean istran=true;
                if(transitivity==VerbTransitivity.InTransitive)
                    istran=false;

                if (!CompoundVerbDic.containsKey(verb))
                    CompoundVerbDic.put(verb, new HashMap<String, HashMap<String, Boolean>>());
                if (!CompoundVerbDic.get(verb).containsKey(nonVerbalElemant))
                {
                    CompoundVerbDic.get(verb).put(nonVerbalElemant, new HashMap<String, Boolean>());
                }
                if (!CompoundVerbDic.get(verb).get(nonVerbalElemant).containsKey(""))
                    CompoundVerbDic.get(verb).get(nonVerbalElemant).put("", istran);

            }
            else if (vtype == 4)
            {
                VerbType verbType = VerbType.PISHVANDI;
                int trans = Integer.parseInt(fields[1]);
                VerbTransitivity transitivity = VerbTransitivity.Transitive;
                if (trans == 0)
                    transitivity = VerbTransitivity.InTransitive;
                else if (trans == 2)
                    transitivity = VerbTransitivity.BiTransitive;
                Verb verb;
                 String pishvand = "";
                if (!fields[5] .equals( "-"))
                {
                    pishvand = fields[5];
                }
                boolean amrShodani = true;
                 String vowelEnd = fields[8];
                 String maziVowel = fields[9];
                 String mozarehVowel = fields[10];
                 String nonVerbalElemant = fields[4];
                if (fields[3] .equals("-")  )
                    verb = new Verb("", fields[2], "", pishvand, "", transitivity, verbType, amrShodani,
                            vowelEnd, maziVowel, mozarehVowel);
                else if (fields[2] .equals("-"))
                    verb = new Verb("", "", fields[3], pishvand, "", transitivity, verbType, amrShodani,
                            vowelEnd, maziVowel, mozarehVowel);
                else
                    verb = new Verb("", fields[2], fields[3], pishvand, "", transitivity, verbType, amrShodani,
                            vowelEnd, maziVowel, mozarehVowel);
                if (fields[7] .equals("*") )
                    amrShodani = false;
                boolean istrans=true;
                if(transitivity==VerbTransitivity.InTransitive)
                    istrans=false;
                if (!CompoundVerbDic.containsKey(verb))
                    CompoundVerbDic.put(verb, new HashMap<String, HashMap<String, Boolean>>());
                if (!CompoundVerbDic.get(verb).containsKey(nonVerbalElemant))
                {
                    CompoundVerbDic.get(verb).put(nonVerbalElemant, new HashMap<String, Boolean>());
                }
                if (!CompoundVerbDic.get(verb).get(nonVerbalElemant).containsKey(""))
                    CompoundVerbDic.get(verb).get(nonVerbalElemant).put("", istrans);
            }
            else if (vtype == 5 || vtype == 7)
            {
                VerbType verbType = VerbType.SADEH;
                int trans = Integer.parseInt(fields[1]);
                VerbTransitivity transitivity = VerbTransitivity.Transitive;
                if (trans == 0)
                    transitivity = VerbTransitivity.InTransitive;
                else if (trans == 2)
                    transitivity = VerbTransitivity.BiTransitive;
                Verb verb;
                 String pishvand = "";
                if (!fields[5].equals("-"))
                {
                    pishvand = fields[5];
                }
                if (!pishvand.equals(""))
                {
                    verbType = VerbType.PISHVANDI;
                }
                boolean amrShodani = true;
                 String vowelEnd = fields[8];
                 String maziVowel = fields[9];
                 String mozarehVowel = fields[10];
                 String nonVerbalElemant = fields[4];
                 String harfeEazafeh = fields[6];
                if (fields[3] .equals("-") )
                    verb = new Verb("", fields[2], "", pishvand, "", transitivity, verbType, amrShodani,
                            vowelEnd, maziVowel, mozarehVowel);
                else if (fields[2] .equals("-") )
                    verb = new Verb("", "", fields[3], pishvand, "", transitivity, verbType, amrShodani,
                            vowelEnd, maziVowel, mozarehVowel);
                else
                    verb = new Verb("", fields[2], fields[3], pishvand, "", transitivity, verbType, amrShodani,
                            vowelEnd, maziVowel, mozarehVowel);
                if (fields[7] .equals("*"))
                    amrShodani = false;
                boolean istrans=true;
                if(transitivity==VerbTransitivity.InTransitive)
                    istrans=false;
                if (!CompoundVerbDic.containsKey(verb))
                    CompoundVerbDic.put(verb, new HashMap<String, HashMap<String, Boolean>>());
                if (!CompoundVerbDic.get(verb).containsKey(nonVerbalElemant))
                {
                    CompoundVerbDic.get(verb).put(nonVerbalElemant, new HashMap<String, Boolean>());
                }
                if (!CompoundVerbDic.get(verb).get(nonVerbalElemant).containsKey(harfeEazafeh))

                    CompoundVerbDic.get(verb).get(nonVerbalElemant).put(harfeEazafeh, istrans);
            }
        }
        StringBuilder verbtext = new  StringBuilder();
        VerbInflection mitavanInflection = new VerbInflection(new Verb("", "", "توان", "", "", VerbTransitivity.InTransitive, VerbType.SADEH, false, "?", "@", "!"), AttachedPronounType.AttachedPronoun_NONE, "",
                PersonType.PERSON_NONE,
                TenseFormationType.HAAL_SAADEH, TensePositivity.POSITIVE);
        VerbShapes.put("می‌توان", new Vector<VerbInflection>());
        VerbShapes.get("می‌توان").add(mitavanInflection);
        VerbInflection nemitavanInflection = new VerbInflection(new Verb("", "", "توان", "", "", VerbTransitivity.InTransitive, VerbType.SADEH, false, "?", "@", "!"), AttachedPronounType.AttachedPronoun_NONE, "",
                PersonType.PERSON_NONE,
                TenseFormationType.HAAL_SAADEH, TensePositivity.POSITIVE);
        VerbShapes.put("نمی‌توان", new Vector<VerbInflection>());
        VerbShapes.get("نمی‌توان").add(nemitavanInflection);

        VerbShapes.put("میتوان", new Vector<VerbInflection>());
        VerbShapes.get("میتوان").add(mitavanInflection);
        VerbShapes.put("نمیتوان", new Vector<VerbInflection>());
        VerbShapes.get("نمیتوان").add(nemitavanInflection);

        VerbInflection betavanInflection = new VerbInflection(new Verb("", "", "توان", "", "", VerbTransitivity.InTransitive, VerbType.SADEH, false, "?", "@", "!"), AttachedPronounType.AttachedPronoun_NONE, "",
                PersonType.PERSON_NONE,
                TenseFormationType.HAAL_ELTEZAMI, TensePositivity.POSITIVE);
        VerbShapes.put("بتوان", new Vector<VerbInflection>());
        VerbShapes.get("بتوان").add(betavanInflection);

        VerbInflection naitavanInflection = new VerbInflection(new Verb("", "", "توان", "", "", VerbTransitivity.InTransitive, VerbType.SADEH, false, "?", "@", "!"), AttachedPronounType.AttachedPronoun_NONE, "",
                PersonType.PERSON_NONE,
                TenseFormationType.HAAL_ELTEZAMI, TensePositivity.POSITIVE);
        VerbShapes.put("نتوان", new Vector<VerbInflection>());
        VerbShapes.get("نتوان").add(naitavanInflection);


        for (Verb verb : verbs)
        {
            if (verb.Type == VerbType.SADEH || verb.Type == VerbType.PISHVANDI || verb.Type == VerbType.AYANDEH_PISHVANDI)
            {
                for (TensePositivity positivity :TensePositivity.values())
                {
                    for (PersonType shakhsType :PersonType.values())
                    {
                        for (TenseFormationType tenseFormationType :TenseFormationType.values())
                        {
                            for ( AttachedPronounType zamirPeyvastehType: AttachedPronounType.values())
                            {

                                VerbInflection inflection = new VerbInflection(verb, zamirPeyvastehType, "",
                                        shakhsType,
                                        tenseFormationType, positivity);
                                if (inflection.IsValid())
                                {
                                    Vector<String> output = InflectorAnalyzeSentencer.GetInflections(inflection);
                                    if (inflection.VerbRoot.PastTenseRoot .equals("بایست"))
                                    {
                                        if (output.elementAt(0).contains("بایست"))
                                        {
                                            inflection.Person = PersonType.PERSON_NONE;
                                            inflection.TenseForm = TenseFormationType.GOZASHTEH_SADEH;
                                        }
                                        else
                                        {
                                            inflection.Person = PersonType.PERSON_NONE;
                                            inflection.TenseForm = TenseFormationType.HAAL_SAADEH;
                                        }
                                    }
                                    Vector< String> output2=new Vector< String>();
                                    for ( String list : output)
                                    {
                                        output2.add(list);
                                        //Console.WriteLine(list);
                                        if (list.contains("می‌")){
                                            String newshape=list.replace("می‌", "می");
                                            output2.add(newshape);
                                            //Console.WriteLine(newshape);
                                        }

                                    }
                                    for ( String list : output2)
                                    {
                                        if (!(VerbShapes.containsKey(list)))
                                        {
                                            Vector<VerbInflection>  verbInflections = new Vector<VerbInflection>();
                                            verbInflections.add( inflection );
                                            VerbShapes.put(list, verbInflections);
                                        }
                                        else
                                        {
                                            boolean contains = false;
                                            for (VerbInflection inf : VerbShapes.get(list))
                                            {
                                                if (inflection.equals(inf))
                                                {
                                                    contains = true;
                                                    break;
                                                }
                                            }
                                            if (!contains)
                                            {
                                                //This for zamir_peyvaste rule based disambiguation in which the inflections
                                                //without zamir_peyvaste are rather to remain
                                                for (int j = 0; j < VerbShapes.get(list).size(); j++)     //todo changed i to j (scope coflict)
                                                {
                                                    VerbInflection verbInflection = VerbShapes.get(list).get(j);
                                                    if (verbInflection.ZamirPeyvasteh !=
                                                            AttachedPronounType.AttachedPronoun_NONE)
                                                    {
                                                        VerbShapes.get(list).remove(verbInflection);
                                                    }
                                                }
                                                VerbShapes.get(list).add(inflection);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
