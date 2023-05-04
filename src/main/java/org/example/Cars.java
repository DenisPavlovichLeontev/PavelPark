package org.example;

import java.util.HashMap;

public class Cars {
    HashMap<String, String> map = new HashMap<>();
    {
        map.put("402047998", "89137767475"); //Камри 570
        map.put("204799", "89134517628"); //Что-то еще
        map.put("113318444", "89232237091"); // Теана
    }

    public HashMap<String, String> getMap() {
        return map;
    }

    HashMap<String, String> map1 = new HashMap<>();
    {
        map1.put("402047998", "Denis Leontev 570 Camry"); //Камри 570
        map1.put("204799", "Denis Leontev 571230 Camry"); //Что-то еще
        map1.put("113318444", "Denis Leontev 570 Camry123"); // Теана
    }
    public HashMap<String, String> getMap1() {
        return map1;
    }
}
