package com.example.seeker.moneyexchanger;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "myLog";
    private String currentDate = getCurrentDate();

    private String URL = "http://www.bnm.md/ru/official_exchange_rates?get_xml=1&date=" + currentDate;
    private String fromValuteName;
    private String toValuteName;

    static final String NODE_NAME = "Name";
    static final String NODE_VALUE = "Value";
    static final String NODE_VALUE_NAME = "CharCode";

    TextView tvShowResult;
    EditText etEnterValue;
    Button btnConvert, btnShowValute;
    List<Currency> currencys;
    Spinner leftSpinner, rightSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                                            getResources().getStringArray(R.array.valutes));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        leftSpinner = (Spinner) findViewById(R.id.leftSpinner);
        rightSpinner = (Spinner) findViewById(R.id.rightSpinner);
        tvShowResult = (TextView) findViewById(R.id.tvShowResult);
        etEnterValue = (EditText) findViewById(R.id.etEnterValue);
        btnConvert = (Button) findViewById(R.id.btnConvert);
        btnShowValute = (Button) findViewById(R.id.btnShowValute);

        leftSpinner.setAdapter(adapter);
        leftSpinner.setSelection(1);
        rightSpinner.setAdapter(adapter);
        rightSpinner.setSelection(0);

        leftSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,getResources().getStringArray(R.array.valutes)[position] , Toast.LENGTH_SHORT).show();
                fromValuteName = getResources().getStringArray(R.array.valutes)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toValuteName = getResources().getStringArray(R.array.valutes)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void onConvertClick(View v){
        ConvertXMLValueTask cXMLTask = new ConvertXMLValueTask();
        cXMLTask.execute(URL);

    }

    public void onShowClick(View v){
        GetXMLTask getXMLTask = new GetXMLTask();
        getXMLTask.execute(URL);
    }



    private class GetXMLTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            String xml = null;
            for (String url : urls) {
                xml = getXmlFromUrl(url);
            }
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {
            XMLDOMParser parser = new XMLDOMParser();
            InputStream stream = new ByteArrayInputStream(xml.getBytes());
            Document doc = parser.getDocument(stream);

            NodeList nodeList = doc.getElementsByTagName("Valute");

            currencys = new ArrayList<>();
            Currency currency;

            tvShowResult.setText("Курс валют на " + currentDate + ":\n");
            for (int i = 0; i < nodeList.getLength(); i++){
                currency = new Currency();
                Element e = (Element) nodeList.item(i);

//                tvShowResult.append(parser.getValue(e, NODE_NAME) + ":\n");
//                tvShowResult.append(parser.getValue(e, NODE_VALUE) + " лей\n");
                //double convertResult = convertMoney(currentValue, Double.parseDouble(parser.getValue(e , NODE_VALUE)));


                //Log.d(LOG_TAG, currentValue + " лей = " + parser.getValue(e, NODE_VALUE_NAME) + convertResult + " лей ");

                currency.setName(parser.getValue(e, NODE_NAME));
                currency.setCurrencyValue(Double.parseDouble(parser.getValue(e, NODE_VALUE)));
                currencys.add(currency);

            }

        }



    }

    private class ConvertXMLValueTask extends AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... urls) {
            String xml = null;
            for (String url : urls) {
                xml = getXmlFromUrl(url);
            }
            return xml;
        }


        @Override
        protected void onPostExecute(String xml) {
            XMLDOMParser parser = new XMLDOMParser();
            InputStream stream = new ByteArrayInputStream(xml.getBytes());
            Document doc = parser.getDocument(stream);

            NodeList nodeList = doc.getElementsByTagName("Valute");
            double moneyCount;

            currencys = new ArrayList<>();
            Currency currency = null;

            if (etEnterValue.getText().length() > 0){
                moneyCount = Double.parseDouble(etEnterValue.getText().toString());
            } else moneyCount = 1;

            for (int i = 0; i < nodeList.getLength(); i++){
                currency = new Currency();
                Element e = (Element) nodeList.item(i);

                currency.setName(parser.getValue(e, NODE_NAME));
                currency.setValuteName(parser.getValue(e, NODE_VALUE_NAME));
                currency.setCurrencyValue(Double.parseDouble(parser.getValue(e, NODE_VALUE)));
                currencys.add(currency);
            }
            double fromValute = currency.findValuteValue(currencys, fromValuteName);
            double toValute = currency.findValuteValue(currencys, toValuteName);

            double convertResult = convertMoney(moneyCount, fromValute, toValute);
            tvShowResult.setText(String.format(Locale.getDefault(), "%.2f", convertResult));

        }
    }

    private String getXmlFromUrl(String urlString) {
        StringBuilder output = new StringBuilder("");

        InputStream stream;
        URL url;
        try {
            url = new URL(urlString);
            URLConnection connection = url.openConnection();

            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(
                        new InputStreamReader(stream));
                String s;
                while ((s = buffer.readLine()) != null)
                    output.append(s);
            }
        } catch (MalformedURLException e) {
            Log.e("Error", "Unable to parse URL", e);
        } catch (IOException e) {
            Log.e("Error", "IO Exception", e);
        }

        return output.toString();
    }

    private String getCurrentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    private double convertMoney(double moneyCount, double fromValute, double toValute){
        if (moneyCount != 0){
            return moneyCount * (fromValute / toValute);
        }
        else return 0;
    }

}
