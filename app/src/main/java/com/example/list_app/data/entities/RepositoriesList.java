package com.example.list_app.data.entities;

import java.io.Serializable;
import java.util.ArrayList;

public class RepositoriesList implements Serializable {
    public int total_count;
    public boolean incomplete_results;
    public ArrayList<Item> items;
}
