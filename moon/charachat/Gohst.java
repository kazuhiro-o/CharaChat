package moon.charachat;
import java.util.Set;
import java.util.Iterator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class Gohst{
    private static final String Prof = "[gohst]";
    private static final String FirstName = "firstName";
    private static final String LastName = "lastName";
    private static final String File = "[file]";
    private static final String Level0 = "[level0]";
    private static final String Level1 = "[level1]";
    private static final String Level2 = "[level2]";
    private static final String Level3 = "[level3]";
    private static final int limit[] = {100,200,300};
    private static CharaChat charaChat = null;
    private static File dic = null;
    private static String firstName = "";
    private static String lastName = "";
    private static int like = 0;
    private static int emote = 0;
    private static int level = 0;
    private static HashMap<String,String> fileMap = new HashMap<String,String>();
    private static HashMap<HashSet,Iterator> iteMap = new HashMap<HashSet,Iterator>();
    private static HashMap<HashSet,ArrayList> dic0 = new HashMap<HashSet,ArrayList>();
    private static HashMap<HashSet,ArrayList> dic1 = new HashMap<HashSet,ArrayList>();
    private static HashMap<HashSet,ArrayList> dic2 = new HashMap<HashSet,ArrayList>();
    private static HashMap<HashSet,ArrayList> dic3 = new HashMap<HashSet,ArrayList>();
    private static Iterator ans = null;
    
    public synchronized static void storeUser(){
        User.setLike(lastName + firstName, like);        
    }

    public static String getFirstName(){
        return Gohst.firstName;
    }
    public static String sendMessage(String str){
        if(level == 0){
            return getMessage(dic0, str);
        }
        else if(level == 1){
            return getMessage(dic1, str);
        }
        else if(level == 2){
            return getMessage(dic2, str);
        }
        else if(level == 3){
            return getMessage(dic3, str);
        }
        return str;
    }

    private static String getMessage(HashMap<HashSet,ArrayList> dic, String str){
        Iterator i1 = dic.keySet().iterator();
        HashSet<String> h = new HashSet<String>();
        HashMap<HashSet,Integer> keywordMap = new HashMap<HashSet,Integer>();
        while(i1.hasNext()){
            h = (HashSet<String>)i1.next();
            Iterator i2 = h.iterator();
            boolean flag = true;
            int i = 0;
            while(i2.hasNext()){
                if(str.indexOf((String)i2.next()) == -1){
                    flag = false;
                    break;
                }                
                i++;
            }
            if(flag){
                keywordMap.put(new HashSet<String>(h), Integer.valueOf(i));
            }
        }
        Iterator i3 = keywordMap.keySet().iterator();
        HashSet<String> key = null;
        int max = 0;
        while(i3.hasNext()){
            h = (HashSet<String>)i3.next();
            int i = ((Integer)keywordMap.get(h)).intValue();
            if(i > max){
                max = i;
                key = new HashSet<String>(h);
            }
        }
        String answer = "";
        if(key != null){
            Iterator ite = null;
            if(iteMap.containsKey(key)){
                ite = (Iterator)iteMap.get(key);
            }
            else{
                ite = (Iterator)dic.get(key).iterator();
                iteMap.put(key, ite);
            }
            if(ite.hasNext()){
                answer = (String)ite.next();
            }
            else{
                ite = (Iterator)dic.get(key).iterator();
                iteMap.put(key, ite);
                answer = (String)ite.next();             
            }
        }
        else{
            answer = "わかりません";
        }
        StringTokenizer st = new StringTokenizer(answer, ",");
        ArrayList<String> a = new ArrayList<String>();
        while(st.hasMoreTokens()){
            a.add(new String(st.nextToken()));
        }
        int n = a.size();
        String message = "";
        String face = "";
        String emo = "";
        String like = "";
        String emote = "";
        if(n >= 5){
            emote = a.get(4);
        }
        if(n >= 4){
            like = a.get(3);
        }
        if(n >= 3){
            emo = a.get(2);
        }
        if(n >= 2){
            face = a.get(1);
        }
        if(n >= 1){
            message = a.get(0);
        }
        if(like.equals("LUP1")){
            Gohst.like = Gohst.like + 2;
            charaChat.printLog("親密度＋２");
        }
        if(like.equals("LUP2")){
            Gohst.like = Gohst.like + 4;
            charaChat.printLog("親密度＋４");
        }
        if(like.equals("LUP3")){
            Gohst.like = Gohst.like + 6;
            charaChat.printLog("親密度＋６");
        }
        if(Gohst.like > 200){
            Gohst.like = 201;
        }
        if(emote.equals("EUP1")){
            Gohst.emote = Gohst.emote + (10 * (Gohst.like + 100) / 100);
            charaChat.printLog("感情値＋１０＋[親密度補正]");
        }
        if(emote.equals("EUP2")){
            Gohst.emote = Gohst.emote + (20 * (Gohst.like + 100) / 100);
            charaChat.printLog("感情値＋２０＋[親密度補正]");
        }
        if(emote.equals("EUP3")){
            Gohst.emote = Gohst.emote + (30 * (Gohst.like + 100) / 100);
            charaChat.printLog("感情値＋３０＋[親密度補正]");
        }
        if(Gohst.emote > Gohst.limit[0]){
            Gohst.level = 1;
        }
        if(Gohst.emote > Gohst.limit[1]){
            Gohst.level = 2;
        }
        if(Gohst.emote > Gohst.limit[2]){
            Gohst.emote = Gohst.limit[2] + 1;
            Gohst.level = 3;
        }
        String faceFile = "";
        if(face != ""){
            faceFile = (String)fileMap.get(face);
            charaChat.setFace(Gohst.dic.getParent() + "\\face\\" + faceFile + ".png");
        }
        String emoFile = "";
        if(emo != ""){
            emoFile = (String)fileMap.get(emo);
            charaChat.setEmote(Gohst.dic.getParent() + "\\emote\\" + emoFile + ".png");
        }
        charaChat.printLog("親密度：" + Gohst.like);
        charaChat.printLog("感情値：" + Gohst.emote);
        charaChat.printLog("感情レベル：" + Gohst.level);
        String s = null;
        if(Gohst.like > 100){
            s = message.replace("%u", User.getNickName());
        }
        else if(Gohst.like > 50){
            s = message.replace("%u", User.getFirstName() + "さん");
        }
        else{
            s = message.replace("%u", User.getLastName() + "さん");
        }
        return s; 

    }
 
    public static void load(CharaChat cc, File dic){
        Gohst.charaChat = cc;
        Gohst.dic = dic;
        User.load();
        try{
            BufferedReader f = new BufferedReader(new InputStreamReader(new FileInputStream(dic), "UTF-16"));
            while(true){
                String s = new String();
                do{
                    s = f.readLine();
                    if(s != null){
                        s = s.trim();
                    }
                    if(s == null || s.equals("")){
                        break;
                    }
                }while(s.charAt(0) == '#');
                if(s == null){
                    break;
                }
                if(s.equals(Prof)){
                    loadProf(f);
                }
                else if(s.equals(File)){
                    loadFiles(f);
                }
                else if(s.equals(Level0)){
                    loadDic(f, 0);
                }
                else if(s.equals(Level1)){
                    loadDic(f, 1);
                }
                else if(s.equals(Level2)){
                    loadDic(f, 2);
                }
                else if(s.equals(Level3)){
                    loadDic(f, 3);
                }
            }
        }
        catch(Exception e){
                System.out.println("load");
                System.out.println(e.toString());
                System.exit(0);
        }
        Gohst.like = User.getLike(Gohst.lastName + Gohst.firstName);
        Gohst.emote = Gohst.like;
        if(Gohst.emote > Gohst.limit[0]){
            Gohst.level = 1;
        }
        if(Gohst.emote > Gohst.limit[1]){
            Gohst.level = 2;
        }
        if(Gohst.emote > Gohst.limit[2]){
            Gohst.emote = Gohst.limit[2] + 1;
            Gohst.level = 3;
        }
    }

    private static void loadProf(BufferedReader f){
        while(true){
            try{
                String s = new String();
                do{
                    s = f.readLine();
                    if(s != null){
                        s = s.trim();
                    }
                    if(s == null || s.equals("")){
                        break;
                    }
                }while(s.charAt(0) == '#');
                if(s == null || s.equals("")){
                    break;
                }
                if(s.equals(Prof)){
                    loadProf(f);
                }
                else if(s.equals(File)){
                    loadFiles(f);
                }
                else if(s.equals(Level0)){
                    loadDic(f, 0);
                }
                else if(s.equals(Level1)){
                    loadDic(f, 1);
                }
                else if(s.equals(Level2)){
                    loadDic(f, 2);
                }
                else if(s.equals(Level3)){
                    loadDic(f, 3);
                }
                StringTokenizer st = new StringTokenizer(s, "=");
                String s1 = "";
                String s2 = "";
                if(st.hasMoreTokens()){
                    s1 = st.nextToken();
                }
                if(st.hasMoreTokens()){
                    s2 = st.nextToken();
                }
                if(s1.equals(FirstName)){
                    firstName = new String(s2);
                }
                else if(s1.equals(LastName)){
                    lastName = new String(s2);
                }
            }
            catch(Exception e){
                System.out.println("loadProf");
                System.out.println(e.toString());
                System.exit(0);
            }
        }
    }

    private static void loadFiles(BufferedReader f){
        while(true){
            try{
                String s = new String();
                do{
                    s = f.readLine();
                    if(s != null){
                        s = s.trim();
                    }
                    if(s == null || s.equals("")){
                        break;
                    }
                }while(s.charAt(0) == '#');
                if(s == null || s.equals("")){
                    break;
                }
                if(s.equals(Prof)){
                    loadProf(f);
                }
                else if(s.equals(File)){
                    loadFiles(f);
                }
                else if(s.equals(Level0)){
                    loadDic(f, 0);
                }
                else if(s.equals(Level1)){
                    loadDic(f, 1);
                }
                else if(s.equals(Level2)){
                    loadDic(f, 2);
                }
                else if(s.equals(Level3)){
                    loadDic(f, 3);
                }
                StringTokenizer st = new StringTokenizer(s, "=");
                String s1 = "";
                String s2 = "";
                if(st.hasMoreTokens()){
                    s1 = st.nextToken();
                }
                if(st.hasMoreTokens()){
                    s2 = st.nextToken();
                }
                fileMap.put(s1, s2);
            }
            catch(Exception e){
                System.out.println("loadProf");
                System.out.println(e.toString());
                System.exit(0);
            }
        }
    }

    private static void loadDic(BufferedReader f, int i){
        while(true){
            try{
                String s = new String();
                do{
                    s = f.readLine();
                    if(s != null){
                        s = s.trim();
                    }
                    if(s == null || s.equals("")){
                        break;
                    }
                }while(s.charAt(0) == '#');
                if(s == null || s.equals("")){
                    break;
                }
                if(s.equals(Prof)){
                    loadProf(f);
                }
                else if(s.equals(File)){
                    loadFiles(f);
                }
                else if(s.equals(Level0)){
                    loadDic(f, 0);
                }
                else if(s.equals(Level1)){
                    loadDic(f, 1);
                }
                else if(s.equals(Level2)){
                    loadDic(f, 2);
                }
                else if(s.equals(Level3)){
                    loadDic(f, 3);
                }
                StringTokenizer st1 = new StringTokenizer(s, "=");
                String s1 = "";
                String s2 = "";
                if(st1.hasMoreTokens()){
                    s1 = st1.nextToken();
                }
                if(st1.hasMoreTokens()){
                    s2 = st1.nextToken();
                }

                HashSet<String> h = new HashSet<String>();
                StringTokenizer st2 = new StringTokenizer(s1, ":");
                while(st2.hasMoreTokens()){
                    h.add(new String(st2.nextToken()));
                }
                ArrayList<String> a = new ArrayList<String>();
                if(i == 0){
                    putDic(dic0, h, a, s2);
                }
                else if(i == 1){
                    putDic(dic1, h, a, s2);
                }
                else if(i == 2){
                    putDic(dic2, h, a, s2);
                }
                else if(i == 3){
                    putDic(dic3, h, a, s2);
                }
            }
            catch(Exception e){
                System.out.println("loadDic");
                e.printStackTrace();
                System.out.println(e.toString());
                System.exit(0);
            }
        }
    }

    private static void putDic(HashMap<HashSet,ArrayList> dic, HashSet<String> h, ArrayList<String> a, String s){
        if(dic.containsKey(h)){
            a = (ArrayList)dic.get(h);
        }
        a.add(new String(s));
        dic.put(h, a);
    }  
}