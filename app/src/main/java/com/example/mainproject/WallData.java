package com.example.mainproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WallData {

    List<Map<String,?>> wallList;

    public List<Map<String, ?>> getWallList() {
        return wallList;
    }

    public int getSize() {return wallList.size();}

    public HashMap getItem(int i) {
        if (i >=0 && i<wallList.size()) {
            return (HashMap) wallList.get(i);
        } else return null;
    }

    public WallData() {
        String username;
        int profile_pic;
        int image;
        String content;
        wallList = new ArrayList<Map<String,?>>();
        //first example
        username = "BrewJohn";
        profile_pic = R.drawable.default_profile_pic;
        image = R.drawable.wall_example_1;
        content = "Me and the guys, brewing up a storm!  Cannot wait to taste this one!";
        wallList.add(createWallPost(username, profile_pic,image,content));

        username = "HopMaster";
        profile_pic = R.drawable.default_profile_pic;
        image = R.drawable.wall_example_2;
        content = "I think I may have a problem.  Is this too many brews to have going on at one time?";
        wallList.add(createWallPost(username,profile_pic,image,content));

    }

    private HashMap createWallPost(String username, int profile_pic, int image, String content) {
        HashMap wallPost = new HashMap();
        wallPost.put("username",username);
        wallPost.put("profile_pic", profile_pic);
        wallPost.put("image", image);
        wallPost.put("content", content);
        return wallPost;
    }
}
