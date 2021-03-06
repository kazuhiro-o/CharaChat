package moon.charachat;
import moon.util.Util;
import java.util.Properties;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class User{

    private final static String fileName = "\\user.ini";
    private final static String FirstName = "firstName";
    private final static String LastName = "lastName";
    private final static String NickName = "nickName";
    private final static Properties userData = new Properties();

    private static void init(){
        String str = Util.inputBox("苗字を入れて下さい。");
        setLastName(str);
        str = Util.inputBox("名前を入れて下さい。");
        setFirstName(str);
        str = Util.inputBox("あだ名を入れて下さい。");
        setNickName(str);
        store();
    }

    public static String getFirstName(){
        return userData.getProperty(User.FirstName, "権兵衛");
    }

    public static String getLastName(){
        return userData.getProperty(User.LastName, "名無しの");
    }

    public static String getNickName(){
        return userData.getProperty(User.NickName, "あなた");
    }

    public static int getLike(String gohstname){
        try{
            return Integer.parseInt(userData.getProperty(gohstname, "0"));
        }
        catch(Exception e){
            return 0;
        }
    }

    public static void setFirstName(String str){
        userData.setProperty(User.FirstName, str);
        store();
    }

    public static void setLastName(String str){
        userData.setProperty(User.LastName, str);
        store();
    }

    public static void setNickName(String str){
        userData.setProperty(User.NickName, str);
        store();
    }

    public static void setLike(String gohstname, int like){
        userData.setProperty(gohstname, Integer.valueOf(like).toString());
        store();
    }

    public static void load(){
        String path = System.getProperty("user.dir");
        File file = new File(path + User.fileName);
        try{
            FileReader fr = new FileReader(file);
            userData.load(fr);
        }
        catch(Exception e){ 
            init();
            try{
                FileReader fr = new FileReader(file);
                userData.load(fr);
            }
            catch(Exception ex){
                ex.printStackTrace();
                System.exit(0);
            }
        }
    }

    private static void store(){
        String path = System.getProperty("user.dir");
        File file = new File(path + User.fileName);
        try{
            FileWriter fw = new FileWriter(file);
            userData.store(fw, null);
        }
        catch(Exception e){
            e.printStackTrace();
            System.exit(0);
        }
    }

}