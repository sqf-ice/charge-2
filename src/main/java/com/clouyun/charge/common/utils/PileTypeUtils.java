package com.clouyun.charge.common.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 桩类型工具类
 */
public class PileTypeUtils {


    private static Map<Integer,Map<Integer,Map<String,Integer>>> pileTypeMap = new LinkedHashMap<>();
    static{
//        Map<String,Integer> jlTwoMap = new HashMap();
//        jlTwoMap.put("A",1);
//        jlTwoMap.put("B",0);
//        Map<String,Integer> jlOneMap = new HashMap();
//        jlOneMap.put("A",1);
//
//        Map<String,Integer> zlMap = new HashMap<>();
//        zlMap.put("A",3);
//        zlMap.put("B",2);
//        zlMap.put("Z",1);
//
//        Map<String,Integer> zlOneMap = new HashMap<>();
//        zlOneMap.put("A",3);
//        zlOneMap.put("Z",1);
//
//        Map<String,Integer> jzlMap = new HashMap<>();
//        jzlMap.put("A",3);
//        jzlMap.put("B",1);
//
//        Map<String,Integer> moreMap = new HashMap<>();
//        moreMap.put("A",1);
//        moreMap.put("B",2);
//        moreMap.put("C",3);
//        moreMap.put("D",4);
//        moreMap.put("E",5);
//        moreMap.put("F",6);
//        moreMap.put("G",7);
//        moreMap.put("H",8);
//        moreMap.put("Z",99);



        Map<String,Integer> jlTwoMap = new LinkedHashMap<>();
        jlTwoMap.put("01",1);
        jlTwoMap.put("02",0);
        Map<String,Integer> jlOneMap = new LinkedHashMap<>();
        jlOneMap.put("01",1);

        Map<String,Integer> zlMap = new LinkedHashMap<>();
        zlMap.put("01",3);
        zlMap.put("02",2);
        zlMap.put("Z",1);

        Map<String,Integer> zlOneMap = new LinkedHashMap<>();
        zlOneMap.put("01",3);
        zlOneMap.put("Z",1);

        Map<String,Integer> jzlMap = new LinkedHashMap<>();
        jzlMap.put("01",3);
        jzlMap.put("02",1);

        Map<String,Integer> moreMap = new LinkedHashMap<>();
        moreMap.put("01",1);
        moreMap.put("02",2);
        moreMap.put("03",3);
        moreMap.put("04",4);
        moreMap.put("05",5);
        moreMap.put("06",6);
        moreMap.put("07",7);
        moreMap.put("08",8);
        moreMap.put("09",9);
        moreMap.put("10",10);
        moreMap.put("Z",99);

        Map jlOrtMap = new LinkedHashMap();
        jlOrtMap.put(1,jlOneMap);
        jlOrtMap.put(2,jlTwoMap);
        jlOrtMap.put(4,moreMap);
        jlOrtMap.put(6,moreMap);
        jlOrtMap.put(8,moreMap);
        jlOrtMap.put(10,moreMap);

        Map zlOrtMap = new LinkedHashMap();
        zlOrtMap.put(1,zlOneMap);
        zlOrtMap.put(2,zlMap);
        zlOrtMap.put(4,moreMap);
        zlOrtMap.put(6,moreMap);
        zlOrtMap.put(8,moreMap);
        zlOrtMap.put(10,moreMap);

        Map jzlOrtMap = new LinkedHashMap();
        jzlOrtMap.put(1,jzlMap);
        jzlOrtMap.put(2,jzlMap);
        jzlOrtMap.put(4,moreMap);
        jzlOrtMap.put(6,moreMap);
        jzlOrtMap.put(8,moreMap);
        jzlOrtMap.put(10,moreMap);

        pileTypeMap.put(1,jlOrtMap);
        pileTypeMap.put(2,zlOrtMap);
        pileTypeMap.put(3,jzlOrtMap);
    }

    /**
     * @param numberGun    枪数
     * @param ortMode      功率模式
     * @param gunNo       枪号(比如A枪传"01",比如B枪传"02",总表传"Z")
     * @return  innerId
     */
    public static Integer getInnerId(Integer numberGun,Integer ortMode,String gunNo){
        Map<String, Integer> gunNoMap = getGunNoMap(numberGun, ortMode);
        Integer innerId = gunNoMap.get(gunNo);
        return innerId;
    }

    /**
     * @param numberGun   枪数
     * @param ortMode     功率模式
     * @return   key是枪号,value是innerId
     */
    public static Map<String,Integer> getGunInnerId(Integer numberGun,Integer ortMode){
        return getGunNoMap(numberGun, ortMode);
    }

    private static Map<String, Integer> getGunNoMap(Integer numberGun, Integer ortMode) {
        Map<Integer, Map<String, Integer>> numberGunMap = pileTypeMap.get(ortMode);
        return numberGunMap.get(numberGun);
    }

    /**
     *
     * @param numberGun  枪数
     * @param ortMode    交直模式
     * @return    返回的枪的innerId
     */
    public static Map<String,Integer> getNumberGun(Integer numberGun,Integer ortMode){
        Map<String, Integer> gunNoMap = getGunNoMap(numberGun, ortMode);
        Map<String, Integer> resultMap = new LinkedHashMap<>();
        if(gunNoMap != null && gunNoMap.size() > 0){
            for (String letter: gunNoMap.keySet()) {
                if(!"Z".equals(letter)){
                    int number = Integer.parseInt(letter);
                    if(number > numberGun){
                        continue;
                    }
                }
                resultMap.put(letter,gunNoMap.get(letter));
            }
        }
        return resultMap;
    }
}
