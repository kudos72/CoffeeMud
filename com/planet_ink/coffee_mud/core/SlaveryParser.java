package com.planet_ink.coffee_mud.core;
import com.planet_ink.coffee_mud.core.SlaveryParser.geasStep;
import com.planet_ink.coffee_mud.core.SlaveryParser.geasSteps;
import com.planet_ink.coffee_mud.core.interfaces.*;


import java.io.IOException;
import java.util.*;

/* 
   Copyright 2000-2005 Bo Zimmerman

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

public class SlaveryParser extends EnglishParser implements Tickable
{
    private SlaveryParser(){super();};

    public String ID(){return "EnglishParser";}
    public String name(){return "CoffeeMuds English Parser";}
    public long getTickStatus(){return Tickable.STATUS_NOT;}
    public CMObject newInstance(){return this;}
    public CMObject copyOf(){return this;}
    public int compareTo(Object o){ return CMClass.classID(this).compareToIgnoreCase(CMClass.classID(o));}
    
    public final static int STEP_EVAL=0;
    public final static int STEP_INT1=1;
    public final static int STEP_INT2=2;
    public final static int STEP_INT3=3;
    public final static int STEP_INT4=4;
    public final static int STEP_INT5=5;
    public final static int STEP_ALLDONE=-999;
    
    public boolean tick(Tickable ticking, int tickID)
    {
        return true;
    }

    // these should be checked after pmap prelim check.
    protected static final String[] universalStarters={
        "go ",
        "go and ",
        "i want you to ",
        "i command you to ",
        "i order you to ",
        "you are commanded to ",
        "please ",
        "to ",
        "you are ordered to "};

    protected static final String[] responseStarters={
        "try at the",
        "its at the",
        "it`s at the",
        "over at the",
        "at the",
        "go to",
        "go to the",
        "find the",
        "go ",
        "try to the",
        "over to the",
        "you`ll have to go",
        "you`ll have to find the",
        "you`ll have to find",
        "look at the",
        "look at",
        "look to",
        "look to the",
        "look for",
        "look for the",
        "search at the",
        "search at",
        "search to the",
        "look",
        "i saw one at",
        "he`s",
        "it`s",
        "she`s",
        "hes",
        "shes",
        "its",
    };
    protected static String[] universalRejections={
        "dunno",
        "nope",
        "not",
        "never",
        "nowhere",
        "noone",
        "can`t",
        "cant",
        "don`t",
        "dont",
        "no"
    };

    //codes:
    //%m mob name (anyone)
    //%i item name (anything)
    //%g misc parms
    //%c casters name
    //%s social name
    //%k skill command word
    //%r room name
    // * match anything
    protected static final String[][] pmap={
        // below is killing
        {"kill %m","mobfind %m;kill %m"},
        {"find and kill %m","mobfind %m;kill %m"},
        {"murder %m","mobfind %m;kill %m"},
        {"find and murder %m","mobfind %m;kill %m"},
        {"find and destroy %m","mobfind %m;kill %m"},
        {"search and destroy %m","mobfind %m;kill %m"},
        {"destroy %i","itemfind %i;recall"},
        {"find and destroy %i","mobfind %i;recall"},
        {"search and destroy %i","mobfind %i;recall"},
        {"destroy %m","mobfind %m;kill %m"},
        {"assassinate %m","mobfind %m; kill %m"},
        {"find and assassinate %m","mobfind %m; kill %m"},
        // below is socials
        {"find and %s %m","mobfind %m;%s %m"},
        {"%s %m","mobfind %m;%s %m"},
        // below is item fetching
//DROWN, DROWN YOURSELF, DROWN IN A LAKE, SWIM, SWIM AN OCEAN, CLIMB A MOUNTAIN, CLIMB A TREE, CLIMB <X>, SWIM <x>, HANG YOURSELF, CRAWL <x>
//BLOW YOUR NOSE, VOMIT, PUKE, THROW UP, KISS MY ASS, KISS <CHAR> <Body part>
        {"bring %i","itemfind %i;mobfind %c;give %i %c"},
        {"bring %m %i","itemfind %i;mobfind %m;give %i %m"},
        {"bring %i to %m","itemfind %i;mobfind %m;give %i %m"},
        {"bring me %i","itemfind %i;mobfind %c;give %i %c"},
        {"bring my %i","itemfind %i;mobfind %c;give %i %c"},
        {"make %i","itemfind %i;mobfind %c;give %i %c"},
        {"make %m %i","itemfind %i;mobfind %m;give %i %m"},
        {"make %i for %m","itemfind %i;mobfind %m;give %i %m"},
        {"make me %i","itemfind %i;mobfind %c;give %i %c"},
        {"give %i","itemfind %i;mobfind %c;give %i %c"},
        {"give %m %i","itemfind %i;mobfind %m;give %i %m"},
        {"give %i to %m","itemfind %i;mobfind %m;give %i %m"},
        {"give me %i","itemfind %i;mobfind %c;give %i %c"},
        {"give your %i","itemfind %i;mobfind %c;give %i %c"},
        {"give %m your %i","itemfind %i;mobfind %m;give %i %m"},
        {"give your %i to %m","itemfind %i;mobfind %m;give %i %m"},
        {"give me your %i","itemfind %i;mobfind %c;give %i %c"},
        {"buy %i","itemfind %i;mobfind %c;give %i %c"},
        {"buy %m %i","itemfind %i;mobfind %m;give %i %m"},
        {"buy %i to %m","itemfind %i;mobfind %m;give %i %m"},
        {"buy me %i","itemfind %i;mobfind %c;give %i %c"},
        {"find me %i","itemfind %i;mobfind %c;give %i %c"},
        {"find my %i","itemfind %i;mobfind %c;give %i %c"},
        {"find %i for %m","itemfind %i;mobfind %m;give %i %m"},
        {"find %m %i","itemfind %i;mobfind %m;give %i %m"},
        {"fetch me %i","itemfind %i;mobfind %c;give %i %c"},
        {"fetch my %i","itemfind %i;mobfind %c;give %i %c"},
        {"fetch %i for %m","itemfind %i;mobfind %m;give %i %m"},
        {"fetch %m %i","itemfind %i;mobfind %m;give %i %m"},
        {"get me %i","itemfind %i;mobfind %c;give %i %c"},
        {"get my %i","itemfind %i;mobfind %c;give %i %c"},
        {"get %i for %m","itemfind %i;mobfind %m;give %i %m"},
        {"get %m %i","itemfind %i;mobfind %m;give %i %m"},
        {"get %i","itemfind %i"},
        {"deliver %i to %m","itemfind %i;mobfind %m;give %i %m"},
        {"deliver my %i to %m","itemfind %i;mobfind %m;give %i %m"},
        // below are eats, drinks
        {"eat %i","itemfind %i;eat %i"},
        {"consume %i","itemfind %i;eat %i"},
        {"stuff yourself with %i","itemfind %i;eat %i"},
        {"drink %i","itemfind %i;drink %i"},
        // below are gos, and find someone (and report back where), take me to, show me
        {"go to %r","find %r;sit"},
        {"report to %r","find %r;sit"},
        {"walk to %r","find %r;sit"},
        {"find %r","find %r;"},
        {"find %r","find %r;"},
        {"show me the way to %r","say follow me;find %r;"},
        {"show me how to get to %r","say follow me;find %r;"},
        {"show me how to get %r","say follow me;find %r;"},
        {"take me to %r","say follow me;find %r;"},
        // follow someone around (but not FOLLOW)
        // simple commands: hold, lock, unlock, read, channel
        {"hold %i","itemfind %i;hold %i"},
        {"lock %i","itemfind %i;lock %i"},
        {"unlock %i","itemfind %i;unlock %i"},
        {"read %i","itemfind %i;read %i"},
        {"gossip %g","gossip %g"},
        // more simpletons: say sit sleep stand wear x, wield x
        {"sleep","sleep"},
        {"sit","sit"},
        {"stand","stand"},
        {"sit down","sit"},
        {"stand up","stand"},
        {"wear %i","itemfind %i;wear %i"},
        {"wield %i","itemfind %i;wield %i"},
        // below are sit x sleep x mount x enter x
        {"sit %i","itemfind %i;sit %i"},
        {"sleep %i","itemfind %i;sleep %i"},
        {"mount %i","itemfind %i;mount %i"},
        {"mount %m","mobfind %m;mount %m"},
        // below are learns, practices, teaches, etc..
        // below are tells, say tos, report tos,
        {"tell %m %g","mobfind %m;sayto %m %g"},
        {"say %g to %m","mobfind %m;sayto %m %g"},
        {"tell %g to %m","mobfind %m;sayto %m %g"},
        // below are skill usages
        {"%k %i","itemfind %i;%k %i"},
        {"%k %m","mobfind %m;%k %m"},
        {"%k %g %i","itemfind %i;%k %g %i"},
        {"%k %g %m","mobfind %m;%k %g %m"},
        // below are silly questions
        {"where %*","say You want me to answer where? I don't know where!"},
        {"who %*","say You want me to answer who? I don't know who!"},
        {"when %*","say You want me to answer when? I don't know when!"},
        {"what %*","say You want me to answer what? I don't know what!"},
        {"why %*","say You want me to answer why? I don't know why!"}
    };

    private static Object[] fpmap=null;
    
    private static Vector findMatch(MOB mob, Vector prereq)
    {
        Vector possibilities=new Vector();
        Hashtable map=new Hashtable();
        if(fpmap==null)
        {
            fpmap=new Object[pmap.length];
            for(int p=0;p<pmap.length;p++)
                fpmap[p]=Util.toStringArray(Util.parse(pmap[p][0]));
        }
        String[] chk=null;
        String[] req=Util.toStringArray(prereq);
        boolean reject=false;
        int ci=0,ri=0;
        Object[] commands=new Object[req.length];
        Social[] socials=new Social[req.length];
        for(int i=0;i<req.length;i++)
        {
            socials[i]=Socials.FetchSocial(req[i],true);
            commands[i]=findCommand(mob,Util.makeVector(req[i].toUpperCase()));
        }
        for(int p=0;p<fpmap.length;p++)
        {
            chk=(String[])fpmap[p];
            ci=0;ri=0;
            reject=false;
            while((!reject)&&(ci<chk.length)&&(ri<req.length))
            {
                if(chk[ci].equals(req[ri]))
                { 
                    ci++; ri++; 
                    reject=false;
                }
                else
                if(chk[ci].charAt(0)=='%')
                {
                    switch(chk[ci].charAt(1))
                    {
                    case 's':
                        if(socials[ri]==null)
                            reject=true;
                        else
                        {
                            map.put("%s",req[ri]);
                            reject=false;
                            ci++;
                            ri++;
                        }
                        break;
                    case 'm':
                    case 'g':
                    case '*':
                    case 'r':
                    case 'i':
                        String code=chk[ci];
                        int remain=chk.length-ci;
                        String str=req[ri];
                        ri++;
                        ci++;
                        reject=false;
                        while(ri<=(req.length-remain))
                        {
                            String nxt="";
                            if(ci<chk.length)
                            {
                                nxt=chk[ci];
                                if(nxt.startsWith("%"))
                                    nxt="";
                            }
                            if((nxt.length()>0)
                            &&(ri<req.length)
                            &&(req[ri].equals(nxt)))
                               break;
                            if(ri<req.length)
                                str=str+" "+req[ri];
                            ri++;
                        }
                        map.put(code,str);
                        break;
                    case 'k':
                        if(commands[ri]==null)
                           reject=true;
                        else
                        {
                            map.put("%k",req[ri]);
                            reject=false;
                            ci++;
                            ri++;
                        }
                        break;
                    default:
                        break;
                    }
                }
                else
                    reject=true;
            }
            if((reject)||(ci!=chk.length)||(ri!=req.length))
            {
                map.clear();
                continue;
            }
            if(CMSecurity.isDebugging("GEAS"))
                Log.debugOut("GEAS","POSS-"+pmap[p][1]);
            map.put("INSTR",pmap[p][1]);
            possibilities.addElement(map);
            map=new Hashtable();
        }
        return possibilities;
    }

    private static String cleanWord(String s)
    {
        String chars=".,;!?'";
        for(int x=0;x<chars.length();x++)
            for(int i=0;i<chars.length();i++)
            {
                while(s.startsWith(""+chars.charAt(i)))
                    s=s.substring(1).trim();
                while(s.endsWith(""+chars.charAt(i)))
                    s=s.substring(0,s.length()-1).trim();
            }
        return s;
    }

    public static geasSteps processRequest(MOB you, MOB me, String req)
    {
        Vector REQ=Util.parse(req.toLowerCase().trim());
        for(int v=0;v<REQ.size();v++)
            REQ.setElementAt(cleanWord((String)REQ.elementAt(v)),v);
        Vector poss=findMatch(me,REQ);
        if(poss.size()==0)
        {
            req=Util.combine(REQ,0);
            boolean doneSomething=true;
            boolean didAnything=false;
            while(doneSomething)
            {
                doneSomething=false;
                for(int i=0;i<universalStarters.length;i++)
                    if(req.startsWith(universalStarters[i]))
                    {
                        doneSomething=true;
                        didAnything=true;
                        req=req.substring(universalStarters.length).trim();
                    }
            }
            if(didAnything)
            {
                REQ=Util.parse(req);
                poss=findMatch(me,REQ);
            }
        }
        if(CMSecurity.isDebugging("GEAS"))
            Log.debugOut("GEAS","POSSTOTAL-"+poss.size());
        geasSteps geasSteps=new geasSteps(you,me);
        if(poss.size()==0)
        {
            geasStep g=new geasStep(geasSteps);
            g.que.addElement("wanderquery "+req);
            geasSteps.addElement(g);
        }
        else
        {
            for(int i=0;i<poss.size();i++)
            {
                geasStep g=new geasStep(geasSteps);
                Hashtable map=(Hashtable)poss.elementAt(i);
                Vector all=Util.parseSemicolons((String)map.get("INSTR"),true);
                if(CMSecurity.isDebugging("GEAS"))
                    Log.debugOut("GEAS",Util.toStringList(all));
                g.que=new Vector();
                for(int a=0;a<all.size();a++)
                    g.que.addElement(Util.parse((String)all.elementAt(a)));
                if(you!=null)   map.put("%c",you.name());
                map.put("%n",me.name());
                for(int q=0;q<g.que.size();q++)
                {
                    Vector V=(Vector)g.que.elementAt(q);
                    for(int v=0;v<V.size();v++)
                    {
                        String s=(String)V.elementAt(v);
                        if(s.startsWith("%"))
                            V.setElementAt(cleanArticles((String)map.get(s.trim())),v);
                    }
                }
                geasSteps.addElement(g);
            }
        }
        return geasSteps;
    }

    public static class geasSteps extends Vector
    {
        public static final long serialVersionUID=Long.MAX_VALUE;
        public Vector bothered=new Vector();
        public boolean done=false;
        public MOB you=null;
        public MOB me=null;
        
        public geasSteps(MOB you1, MOB me1)
        {
            you=you1;
            me=me1;
        }
        
        public void step()
        {
            String say=null;
            boolean moveFlag=false;
            boolean holdFlag=false;
            String ss=null;
            geasStep sg=null;
            
            if(!done)
            for(int s=0;s<size();s++)
            {
                geasStep G=(geasStep)elementAt(s);
                ss=G.step();
                if(ss.equalsIgnoreCase("DONE"))
                {
                    done=true;
                    break;
                }
                if(ss.equalsIgnoreCase("HOLD"))
                {
                    removeElementAt(s);
                    insertElementAt(G,0);
                    holdFlag=true;
                    break;
                }
                else
                if(ss.equalsIgnoreCase("MOVE"))
                    moveFlag=true;
                else
                if(ss.startsWith("1"))
                {
                    say=ss;
                    sg=G;
                }
                else
                if(ss.startsWith("0"))
                {
                    if(say==null)
                    {
                        say=ss;
                        sg=G;
                    }
                }
            
            }
            if(!holdFlag)
            {
                if((say!=null)&&(sg!=null))
                {
                    if(!sg.botherIfAble(ss.substring(1)))
                    {
                        sg.step=STEP_EVAL;
                        move(Util.s_int(""+ss.charAt(0)));
                    }
                    else
                        sg.step=STEP_INT1;
                }
                else
                if(moveFlag)
                    move(0);
            }
        }
        public void move(int moveCode)
        {
            if(!bothered.contains(me.location()))
                bothered.addElement(me.location());
            if(CMSecurity.isDebugging("GEAS"))
                Log.debugOut("GEAS","BEINGMOBILE: "+moveCode);
            if(moveCode==0)
            {
                if(!MUDTracker.beMobile(me,true,true,false,true,null,bothered))
                    MUDTracker.beMobile(me,true,true,false,false,null,null);
            }
            else
            {
                if(!MUDTracker.beMobile(me,true,true,true,false,null,bothered))
                    MUDTracker.beMobile(me,true,true,false,false,null,null);
            }
        }

        public boolean sayResponse(MOB speaker, MOB target, String response)
        {
            for(int s=0;s<size();s++)
            {
                geasStep G=(geasStep)elementAt(s);
                if(G.bothering!=null)
                    return G.sayResponse(speaker,target,response);
            }
            return false;
        }
    }
    
    public static class geasStep
    {
        public Vector que=new Vector();
        public int step=STEP_EVAL;
        public MOB bothering=null;
        public geasSteps mySteps=null;
        public MOB you=null;
        
        public geasStep(geasSteps gs)
        {
            mySteps=gs;
        }

        public boolean botherIfAble(String msgOrQ)
        {
            MOB me=mySteps.me;
            bothering=null;
            if((me==null)||(me.location()==null)) 
                return false;
            if((msgOrQ!=null)&&(!Sense.isAnimalIntelligence(me)))
                for(int m=0;m<me.location().numInhabitants();m++)
                {
                    MOB M=me.location().fetchInhabitant(m);
                    if((M!=null)
                    &&(M!=me)
                    &&(!Sense.isAnimalIntelligence(M))
                    &&(!mySteps.bothered.contains(M)))
                    {
                        CommonMsgs.say(me,M,msgOrQ,false,false);
                        bothering=M;
                        mySteps.bothered.addElement(M);
                        if(CMSecurity.isDebugging("GEAS"))
                            Log.debugOut("GEAS","BOTHERING: "+bothering.name());
                        return true;
                    }
                }
            return false;
        }
        
        public boolean sayResponse(MOB speaker, MOB target, String response)
        {
            MOB me=mySteps.me;
            if((speaker!=null)
            &&(speaker!=me)
            &&(bothering!=null)
            &&(speaker==bothering)
            &&(step!=STEP_EVAL)
            &&((target==null)||(target==me)))
            {
                for(int s=0;s<universalRejections.length;s++)
                {
                    if(containsString(response,universalRejections[s]))
                    {
                        CommonMsgs.say(me,speaker,"Ok, thanks anyway.",false,false);
                        return true;
                    }
                }
                boolean starterFound=false;
                response=response.toLowerCase().trim();
                for(int i=0;i<responseStarters.length;i++);
                    for(int s=0;s<responseStarters.length;s++)
                    {
                        if(response.startsWith(responseStarters[s]))
                        {
                            starterFound=true;
                            response=response.substring(responseStarters[s].length()).trim();
                        }
                    }
                if((!starterFound)&&(speaker.isMonster())&&(Dice.rollPercentage()<10))
                    return false;
                if(response.trim().length()==0)
                    return false;
                bothering=null;
                que.insertElementAt(Util.parse("find150 \""+response+"\""),0);
                step=STEP_EVAL;
                return true;
            }
            return false;
        }

        public String step()
        {
            MOB me=mySteps.me;
            if(que.size()==0)
            {
                step=STEP_ALLDONE;
                return "DONE";
            }
            Vector cur=(Vector)que.firstElement();
            if(cur.size()==0)
            {
                step=STEP_EVAL;
                que.removeElementAt(0);
                return "HOLD";
            }
            String s=(String)cur.firstElement();
            if(CMSecurity.isDebugging("GEAS"))
                Log.debugOut("GEAS","STEP-"+s);
            if(s.equalsIgnoreCase("itemfind"))
            {
                String item=Util.combine(cur,1);
                if(CMSecurity.isDebugging("GEAS"))
                    Log.debugOut("GEAS","ITEMFIND: "+item);
                if((Util.isNumber(item)&&(Util.s_int(item)>0)))
                {
                    if(BeanCounter.getTotalAbsoluteNativeValue(me)>=new Integer(Util.s_int(item)).doubleValue())
                    {
                        step=STEP_EVAL;
                        que.removeElementAt(0);
                        CommonMsgs.say(me,null,"I got the money!",false,false);
                        return "HOLD";
                    }
                    item="coins";
                }

                // do I already have it?
                Item I=me.fetchInventory(item);
                if((I!=null)&&(Sense.canBeSeenBy(I,me)))
                {
                    step=STEP_EVAL;
                    if(!I.amWearingAt(Item.INVENTORY))
                    {
                        CommonMsgs.remove(me,I,false);
                        return "HOLD";
                    }
                    if(I.container()!=null)
                    {
                        CommonMsgs.get(me,I.container(),I,false);
                        return "HOLD";
                    }
                    que.removeElementAt(0);
                    CommonMsgs.say(me,null,"I got "+I.name()+"!",false,false);
                    return "HOLD";
                }
                // is it just sitting around?
                I=me.location().fetchItem(null,item);
                if((I!=null)&&(Sense.canBeSeenBy(I,me)))
                {
                    step=STEP_EVAL;
                    CommonMsgs.get(me,null,I,false);
                    return "HOLD";
                }
                // is it in a container?
                I=me.location().fetchAnyItem(item);
                if((I!=null)&&(I.container()!=null)
                   &&(I.container() instanceof Container)
                   &&(((Container)I.container()).isOpen()))
                {
                    step=STEP_EVAL;
                    CommonMsgs.get(me,I.container(),I,false);
                    return "HOLD";
                }
                // is it up for sale?
                for(int m=0;m<me.location().numInhabitants();m++)
                {
                    MOB M=me.location().fetchInhabitant(m);
                    if((M!=null)&&(M!=me)&&(Sense.canBeSeenBy(M,me)))
                    {
                        I=M.fetchInventory(null,item);
                        if((I!=null)&&(!I.amWearingAt(Item.INVENTORY)))
                        {
                            if(step==STEP_EVAL)
                            {
                                CommonMsgs.say(me,M,"I must have '"+I.name()+".  Give it to me now.",false,false);
                                step=STEP_INT1;
                                return "HOLD";
                            }
                            else
                            if(step==STEP_INT1)
                            {
                                step=STEP_INT2;
                                return "HOLD";
                            }
                            else
                            if(step==STEP_INT2)
                            {
                                CommonMsgs.say(me,M,"I MUST HAVE '"+I.name().toUpperCase()+".  GIVE IT TO ME NOW!!!!",false,false);
                                step=STEP_INT3;
                                return "HOLD";
                            }
                            else
                            if(step==STEP_INT3)
                            {
                                step=STEP_INT4;
                                return "HOLD";
                            }
                            else
                            if(step==STEP_INT4)
                            {
                                MUDFight.postAttack(me,M,me.fetchWieldedItem());
                                step=STEP_EVAL;
                                return "HOLD";
                            }
                        }
                        ShopKeeper sk=CoffeeShops.getShopKeeper(M);
                        if((!item.equals("coins"))&&(sk!=null)&&(sk.getStock(item,me)!=null))
                        {
                            Environmental E=sk.getStock(item,me);
                            if((E!=null)&&(E instanceof Item))
                            {
                                double price=CoffeeShops.sellingPrice(M,me,E,sk,true).absoluteGoldPrice;
                                if(price<=BeanCounter.getTotalAbsoluteShopKeepersValue(me,M))
                                {
                                    me.enqueCommand(Util.parse("BUY \""+E.name()+"\""),0);
                                    step=STEP_EVAL;
                                    return "HOLD";
                                }
                                price=price-BeanCounter.getTotalAbsoluteShopKeepersValue(me,M);
                                que.insertElementAt(Util.parse("itemfind "+BeanCounter.nameCurrencyShort(M,price)),0);
                                CommonMsgs.say(me,null,"Damn, I need "+BeanCounter.nameCurrencyShort(M,price)+".",false,false);
                                step=STEP_EVAL;
                                return "HOLD";
                            }
                        }
                    }
                }
                // if asked someone something, give them time to respond.
                if((bothering!=null)&&(step>STEP_EVAL)&&(step<=STEP_INT4)&&(!bothering.isMonster()))
                {   step++; return "HOLD";}
                step=STEP_EVAL;
                return "0Can you tell me where to find "+Util.combine(cur,1)+"?";
            }
            else
            if(s.equalsIgnoreCase("mobfind"))
            {
                String name=Util.combine(cur,1);
                if(CMSecurity.isDebugging("GEAS"))
                    Log.debugOut("GEAS","MOBFIND: "+name);
                if(name.equalsIgnoreCase("you")) name=me.name();
                if(name.equalsIgnoreCase("yourself")) name=me.name();
                if(you!=null)
                {
                    if(name.equals("me")) name=you.name();
                    if(name.equals("myself")) name=you.name();
                    if(name.equals("my")) name=you.name();
                }

                MOB M=me.location().fetchInhabitant(name);
                if(M==me) M=me.location().fetchInhabitant(name+".2");
                if((M!=null)&&(M!=me)&&(Sense.canBeSeenBy(M,me)))
                {
                    if(CMSecurity.isDebugging("GEAS"))
                        Log.debugOut("GEAS","MOBFIND-FOUND: "+name);
                    step=STEP_EVAL;
                    que.removeElementAt(0);
                    return "HOLD";
                }

                // if asked someone something, give them time to respond.
                if((bothering!=null)&&(step>STEP_EVAL)&&(step<=STEP_INT4)&&(!bothering.isMonster()))
                {   
                    if(CMSecurity.isDebugging("GEAS"))
                        Log.debugOut("GEAS","MOBFIND-RESPONSEWAIT: "+bothering.name());
                    step++; 
                    return "HOLD";
                }
                step=STEP_EVAL;
                int code=0;
                if((you!=null)&&(you.name().equalsIgnoreCase(name)))
                    code=1;
                return code+"Can you tell me where to find "+name+"?";
            }
            else
            if(s.toLowerCase().startsWith("find"))
            {
                String name=Util.combine(cur,1);
                if(CMSecurity.isDebugging("GEAS"))
                    Log.debugOut("GEAS","FIND: "+name);
                if(name.equalsIgnoreCase("you")) name=me.name();
                if(name.equalsIgnoreCase("yourself")) name=me.name();
                if(you!=null)
                {
                    if(name.equals("me")) name=you.name();
                    if(name.equals("myself")) name=you.name();
                    if(name.equals("my")) name=you.name();
                }
                int dirCode=Directions.getGoodDirectionCode((String)Util.parse(name).firstElement());
                if((dirCode>=0)&&(me.location()!=null)&&(me.location().getRoomInDir(dirCode)!=null))
                {
                    if(Util.parse(name).size()>1)
                        cur.setElementAt(Util.combine(Util.parse(name),1),1);
                    step=STEP_EVAL;
                    MUDTracker.move(me,dirCode,false,false);
                    return "HOLD";
                }

                if(containsString(me.location().name(),name)
                   ||containsString(me.location().displayText(),name)
                   ||containsString(me.location().description(),name))
                {
                    step=STEP_EVAL;
                    que.removeElementAt(0);
                    return "HOLD";
                }
                MOB M=me.location().fetchInhabitant(name);
                if((M!=null)&&(M!=me)&&(Sense.canBeSeenBy(M,me)))
                {
                    step=STEP_EVAL;
                    que.removeElementAt(0);
                    return "HOLD";
                }
                // is it just sitting around?
                Item I=me.location().fetchItem(null,name);
                if((I!=null)&&(Sense.canBeSeenBy(I,me)))
                {
                    step=STEP_EVAL;
                    CommonMsgs.get(me,null,I,false);
                    return "HOLD";
                }
                if((s.length()>4)&&(Util.isNumber(s.substring(4))))
                {
                    int x=Util.s_int(s.substring(4));
                    if((--x)<0)
                    {
                        que.removeElementAt(0);
                        step=STEP_EVAL;
                        return "HOLD";
                    }
                    cur.setElementAt("find"+x,0);
                }

                // if asked someone something, give them time to respond.
                if((bothering!=null)&&(step>STEP_EVAL)&&(step<=STEP_INT4)&&(!bothering.isMonster()))
                {   step++; return "HOLD";}
                step=STEP_EVAL;
                if(s.length()>4)
                    return "0Can you tell me where to find "+name+"?";
                return "MOVE";
            }
            else
            if(s.equalsIgnoreCase("wanderquery"))
            {
                if(CMSecurity.isDebugging("GEAS"))
                    Log.debugOut("GEAS","WANDERQUERY: "+Util.combine(cur,1));
                // if asked someone something, give them time to respond.
                if((bothering!=null)&&(step>STEP_EVAL)&&(step<=STEP_INT4)&&(!bothering.isMonster()))
                {   step++; return "HOLD";}
                step=STEP_EVAL;
                return "Can you help me "+Util.combine(cur,1)+"?";
            }
            else
            {
                step=STEP_EVAL;
                que.removeElementAt(0);
                me.enqueCommand(cur,0);
                return "HOLD";
            }
        }
    }
}
