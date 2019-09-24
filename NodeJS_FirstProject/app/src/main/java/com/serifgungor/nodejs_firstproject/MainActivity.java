package com.serifgungor.nodejs_firstproject;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public class Kisi{
        private String ad;
        private String soyad;
        private String sehir;

        public Kisi() {
        }

        public Kisi(String ad, String soyad, String sehir) {
            this.ad = ad;
            this.soyad = soyad;
            this.sehir = sehir;
        }

        public String getAd() {
            return ad;
        }

        public void setAd(String ad) {
            this.ad = ad;
        }

        public String getSoyad() {
            return soyad;
        }

        public void setSoyad(String soyad) {
            this.soyad = soyad;
        }

        public String getSehir() {
            return sehir;
        }

        public void setSehir(String sehir) {
            this.sehir = sehir;
        }
    }


    public class ListeAdapter extends BaseAdapter{

        private ArrayList<Kisi> kisiler;
        private Context context;
        private LayoutInflater layoutInflater;

        public ListeAdapter(){

        }

        public ListeAdapter(ArrayList<Kisi> kisiler,Context context){
            this.kisiler = kisiler;
            this.context = context;
            this.layoutInflater = (LayoutInflater)
                    context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return kisiler.size();
        }

        @Override
        public Object getItem(int position) {
            return kisiler.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
          View v = layoutInflater.inflate(R.layout.custom_listview_row,null);

            TextView tvAd = v.findViewById(R.id.tvAd);
            TextView tvSoyad = v.findViewById(R.id.tvSoyad);
            TextView tvSehir = v.findViewById(R.id.tvSehir);

            tvAd.setText(kisiler.get(position).getAd());
            tvSehir.setText(kisiler.get(position).getSehir());
            tvSoyad.setText(kisiler.get(position).getSoyad());

          return v;
        }
    }



    EditText etAd,etSoyad,etSehir;
    Button btnBaglan,btnGonder;
    Socket socket;
    ArrayList<Kisi> kisiler;
    ListeAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etAd = findViewById(R.id.etAd);
        etSoyad = findViewById(R.id.etSoyad);
        etSehir = findViewById(R.id.etSehir);
        btnBaglan = findViewById(R.id.btnBaglan);
        btnGonder = findViewById(R.id.btnGonder);

        listView = findViewById(R.id.listView);
        kisiler = new ArrayList<>();





        try{
            socket = IO.socket("http://10.1.9.14:3001");
        }catch (Exception e){

        }

        socket.on("mesajAl", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                String str = args[0].toString();
                String[] dizi = str.split(",");

                String ad = dizi[0];
                String soyad = dizi[1];
                String sehir = dizi[2];
                kisiler.add(new Kisi(ad,soyad,sehir));


                Log.d("RESPONSE",args[0].toString());
            }
        });

        btnBaglan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket.connect();
            }
        });

        btnGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String veri = etAd.getText().toString();
                veri = veri+","+ etSoyad.getText().toString();
                veri = veri+","+ etSehir.getText().toString();
                socket.emit("mesaj",veri);
                adapter = new ListeAdapter(kisiler,getApplicationContext());
                listView.setAdapter(adapter);
                // serif,gungor,istanbul
            }
        });
    }
}
