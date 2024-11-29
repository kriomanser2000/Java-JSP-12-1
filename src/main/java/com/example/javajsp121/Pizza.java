package com.example.javajsp121;

import java.util.List;

public class Pizza
{
    private String name;
    private List<String> toppings;
    public Pizza(String name, List<String> toppings)
    {
        this.name = name;
        this.toppings = toppings;
    }
    public String getName()
    {
        return name;
    }
    public List<String> getToppings()
    {
        return toppings;
    }
}
