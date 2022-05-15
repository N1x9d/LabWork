package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.*;

import static java.util.Arrays.stream;

public class NewThread extends   Thread {
    Thread  t;
    String szDir="";
    public static Map map = new HashMap<String,Map<String,Integer>>();
    public static List<NewThread> threads = new ArrayList<>();
    boolean isFinish = false;
    public NewThread(String SzDir) {
        szDir=SzDir;
        t = new Thread (this);
        t.start();

    }

    @Override
    public  void  run ()  {
        try {
            Search(szDir);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(t.getName());
        for(Object key : map.keySet()) {
            Main.map.put(key,map.get(key));


        }
        Main.map.forEach((key, value) -> System.out.println(key + ":" + value));

    }
    public static void Search(String szDir) throws InterruptedException, FileNotFoundException, SQLException, ClassNotFoundException {
        File f = new File(szDir);

        String[] sDirList = f.list();
        int i;
        for(i = 0; i < sDirList.length; i++)
        {
            File f1 = new File(szDir +
                    File.separator + sDirList[i]);

            if(f1.isFile() && f1.getName().contains(".txt")) {
                String FilePath = szDir + File.separator + sDirList[i];
                // System.out.println(FilePath);
                DB.addFile(f1.getName(),szDir + File.separator + sDirList[i]);
                var a = CalcWords(FilePath);
                for (var entry : a.entrySet()) {
                    DB.addWord(entry.getKey(),szDir + File.separator + sDirList[i], entry.getValue());

                }
                map.put(FilePath,a);
            }
            else if(!f1.isFile())
            {
                DB.addDir(sDirList[i],szDir + File.separator + sDirList[i]);
                Search(f1.getPath());
            }
            //else break;
        }


    }

    public static Map<String,Integer> CalcWords(String path) throws FileNotFoundException {
        var res = new HashMap<String, Integer>();
        Scanner s = new Scanner(new File(path));
        ArrayList<String> Words = new ArrayList<String>();
        LinkedHashMap sortedMap = new LinkedHashMap<>();
        ArrayList list = new ArrayList<>();
        while (s.hasNext()){
            Words.add(removePunct(s.next()).toUpperCase());
        }
        s.close();
        for (String word: Words) {
            if(word==""||word==" ")
            {}
            else{
                if(res.containsKey(word)){
                    var cout= res.get(word);
                    res.put(word,cout+1);
                }
                else{
                    res.put(word,1);
                }
            }


        }
        for (Map.Entry entry : res.entrySet()) {
            list.add(entry.getValue());
        }
        Collections.sort(list, Collections.reverseOrder());
        for (Object num : list) {
            for (Map.Entry entry : res.entrySet()) {
                if (entry.getValue().equals(num)) {
                    sortedMap.put(entry.getKey(), num);
                }
            }
        }
        // System.out.println(sortedMap);


        return sortedMap;
    }

    public static String removePunct(String str) {
        StringBuilder result = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isAlphabetic(c) || Character.isDigit(c) || Character.isSpaceChar(c)) {
                result.append(c);
            }
        }
        return result.toString();
    }
}
