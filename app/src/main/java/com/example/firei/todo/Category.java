package com.example.firei.todo;

import java.util.ArrayList;

/**
 * Created by FireI on 11/3/2018.
 */

public class Category {
    String name;
    ArrayList<String> tasks;

    public Category(String n){
        name = n;
        tasks = new ArrayList<String>();
    }

    public void add(String task){
        tasks.add(task);
    }

    public void remove(String task){
        tasks.remove(task);
    }
}
