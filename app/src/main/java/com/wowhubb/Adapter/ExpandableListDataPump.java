package com.wowhubb.Adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> technology = new ArrayList<String>();
        technology.add("Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.");


        List<String> entertainment = new ArrayList<String>();
        entertainment.add("It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged.");


        List<String> science = new ArrayList<String>();
        science.add("It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. ");

        expandableListDetail.put("What is Event Name?", science);
        expandableListDetail.put("Event coverpage photo/image size", technology);
        expandableListDetail.put("Event timezone selection", entertainment);

        return expandableListDetail;
    }
}
