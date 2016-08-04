package com.example.seeker.moneyexchanger;

import android.content.Context;
import android.view.LayoutInflater;
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Holder holder;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.currency_grid, null);
            holder = new Holder();
            holder.tvGridName = (TextView) convertView.findViewById(R.id.tvGridName);
            holder.tvGridValue = (TextView) convertView.findViewById(R.id.tvGridValue);
            convertView.setTag(holder);
        } else {
            holder = (Holder)convertView.getTag();
        }
        holder.tvGridName.setText(currencies.get(position).getValuteName());
        holder.tvGridValue.setText(String.format(Locale.getDefault(), "%s", currencies.get(position).getCurrencyValue()));
        return convertView;
    }
    

    public static class Holder{
        public TextView tvGridName, tvGridValue;
    }
}


