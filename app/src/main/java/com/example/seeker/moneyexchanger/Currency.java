package com.example.seeker.moneyexchanger;

import java.util.List;

/**
 * Created by SeeKeR on 010 10.07.16.
 */
public class Currency {

    private double currencyValue;
    private String name;
    private String valuteName;

    public String getName(){
        return name;
    }

    public double getCurrencyValue() {
        return currencyValue;
    }

    public void setCurrencyValue(double currencyValue) {
        this.currencyValue = currencyValue;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double findValuteValue(List<Currency> currencies, String findValuteName){
        double valuteValue = 0;
        for (Currency c : currencies){
            if (c.getValuteName().equals(findValuteName)){
                valuteValue = c.getCurrencyValue();
                break;
            } else valuteValue = 1;
        }
        return valuteValue;
    }

    public String getValuteName() {
        return valuteName;
    }

    public void setValuteName(String valuteName) {
        this.valuteName = valuteName;
    }
}
