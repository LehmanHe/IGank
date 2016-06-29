package cn.edu.ustc.igank.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lehman on 2016-06-25.
 */
public class GirlArrayBean {
    private static List<GirlBean> girlList=new ArrayList<>();

    public static List<GirlBean> getGirlList() {
        return girlList;
    }

    public static void setGirlList(List<GirlBean> girlList) {
        GirlArrayBean.girlList = girlList;
    }

    public static void addGirl(GirlBean girlBean){
        girlList.add(girlBean);
    }
}
