package com.taifua.material;

import android.util.Log;

import java.util.ArrayList;

public class MatchString {
    public ArrayList<String> bf(String sub){
        ArrayList<String> arr = new ArrayList<>();
        for(String s:Images.imageThumbUrls){
            if(fuck(s,sub) == true){
                arr.add(s);
                Log.d("fuck", "s: "+ s);
            }
        }
        return arr;
    }
    private boolean fuck(String s,String sub){
        int len1 = s.length();
        int len2 = sub.length();
        boolean flag ;
        for(int i=0;i<len1-len2;i++){
            flag = false;
            for(int j=0;j<len2;j++){
                if(s.charAt(i+j)!=sub.charAt(j)){
                    flag = true;
                    break;
                }
            }
            if (!flag){
                return true;
            }
        }
        return false;
    }
}