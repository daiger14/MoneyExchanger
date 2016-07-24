package com.example.seeker.moneyexchanger;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

/**
 * Created by Андрей on 24.07.2016.
 */
public class CurrencyAdapter extends BaseAdapter {
    private Context context;
    private List<Currency> currencies;

    public CurrencyAdapter(Context context, List<Currency> currencies){
        this.context = context;
        this.currencies = currencies;
    }

    @Override
    public int getCount() {
        return currencies.size();
    }

    @Override
    public Object getItem(int position) {
        return currencies.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        TextView tvName, tvValue;
        if (viewGroup == null){
            tvName = new TextView(context);
            tvValue = new TextView(context);
        } else {
            tvName = (TextView) view;
            tvValue = (TextView) view;
        }

        tvName.setText(currencies.get(position).getName());
        tvValue.setText(String.format(Locale.getDefault(), "%.2f", currencies.get(position).getCurrencyValue()));
        return null;
    }
}
