package com.serifgungor.socketio_chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Button btnBaglan,btnGonder;
    EditText etMesaj;
    Socket socket;

    ArrayAdapter<String> adapter;
    ArrayList<String> liste;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        btnBaglan = findViewById(R.id.btnBaglan);
        btnGonder = findViewById(R.id.btnGonder);
        etMesaj = findViewById(R.id.etMesaj);

        liste = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,liste);
        listView.setAdapter(adapter);

        try{
            socket = IO.socket("http://10.1.9.14:4001");
        }catch (Exception e){}


        //İlgili port dinlenir ne zaman mesajAl tetiklenir ise
        //metot o esnada çalışır
        socket.on("mesajAl", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                //Log.d("RESPONSE",args[0].toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        liste.add(args[0].toString());
                        adapter.notifyDataSetChanged();
                        listView.setAdapter(adapter);
                    }
                });
                //Console'a düşen son değeri yakaladık
            }
        });

        btnGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket.emit("mesaj",etMesaj.getText().toString());
            }
        });

        btnBaglan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket.connect();
            }
        });
    }
}
