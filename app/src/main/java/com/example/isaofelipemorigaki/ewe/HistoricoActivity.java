package com.example.isaofelipemorigaki.ewe;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class HistoricoActivity extends ListActivity{
    String[] items={"EDI 1 1", "EDI 2", "EDI 3","EDI 4", " EDI 5", " ITEM 6"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);
        setListAdapter(new IconicAdapter(this));
    }
    class IconicAdapter extends ArrayAdapter {
        Activity context;
        IconicAdapter(Activity context) {
            super(context, R.layout.acitivity_historico_row, items);
            this.context = context;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View row = inflater.inflate(R.layout.acitivity_historico_row, null);
            TextView beacon = (TextView) row.findViewById(R.id.id_beacon_historico);
            TextView mensagemFixa = (TextView) row.findViewById(R.id.mensagem_fixa_historico);
            TextView mensagemTemp = (TextView) row.findViewById(R.id.mensagem_temp_historico);

            beacon.setText(items[position]);
            mensagemFixa.setText(items[position]);
            mensagemTemp.setText(items[position]);
            return (row);
        }
    }
}
