package cn.shilight.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import cn.shilight.myapplication.message.MessagObtian;

public class MessageActivityTest extends AppCompatActivity {



    Socket ss;
    Button connect,send,regist;
    EditText ip,port,sendc,from,forr;
    TextView state,get;

    ObjectOutputStream out;
    ObjectInputStream in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        connect = findViewById(R.id.connnect);
        send = findViewById(R.id.send);
        regist = findViewById(R.id.Regist);

        ip = findViewById(R.id.IP);
        port = findViewById(R.id.PORT);
        get = findViewById(R.id.GET);
        sendc = findViewById(R.id.sendCon);

        from = findViewById(R.id.FROM);

        forr = findViewById(R.id.TO);

        state = findViewById(R.id.state);


        connect.setOnClickListener(v -> {


            try {
                ss =  new Socket(ip.getText().toString(), Integer.parseInt(port.getText().toString()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if(ss.isConnected()){


                state.setText("已连接");

                try {

                     out = new ObjectOutputStream(ss.getOutputStream());
                     in = new ObjectInputStream(ss.getInputStream());

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }


        });



        send.setOnClickListener(v -> {

            try {
                out.writeObject(new MessagObtian(forr.getText().toString(),from.getText().toString(),1).setContent(sendc.getText().toString()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });


        getSupportActionBar().hide();
    }
}