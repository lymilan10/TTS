package com.example.ttsdemo;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SharedData {
    //语速
    public static float speed = 1.0f;
    //语调
    public static float tone = 1.0f;
    //音量 0-1f
    public static float volume= 0.1f;

    public static Map<String, Locale> languageList = new HashMap<String, Locale>();

}
