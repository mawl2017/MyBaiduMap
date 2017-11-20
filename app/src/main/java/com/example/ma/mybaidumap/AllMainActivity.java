package com.example.ma.mybaidumap;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class AllMainActivity extends Activity implements View.OnClickListener{
private Button btn1,btn2,btn3,btn4,btn5,btn6;

private List<Button> btnes=new ArrayList<Button>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_main);

        btn1= (Button) findViewById(R.id.button3_all);
        btn2= (Button) findViewById(R.id.button4_all);
        btn3= (Button) findViewById(R.id.button5_all);
        btn4= (Button) findViewById(R.id.button6_all);
        btn5= (Button) findViewById(R.id.button7_all);
        btn6= (Button) findViewById(R.id.button8_all);

        btnes.add(btn1);
        btnes.add(btn2);
        btnes.add(btn3);
        btnes.add(btn4);
        btnes.add(btn5);
        btnes.add(btn6);

        for (int i=0;i<btnes.size();i++){
            btnes.get(i).setOnClickListener(this);
        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.button3_all:
                startActivity(new Intent(AllMainActivity.this,MainActivity.class));
                break;
            case R.id.button4_all:
                startActivity(new Intent(AllMainActivity.this,Main2Activity.class));
                break;
            case R.id.button5_all:
                startActivity(new Intent(AllMainActivity.this,Main3Activity.class));
                break;
            case R.id.button6_all:
                startActivity(new Intent(AllMainActivity.this,Main4Activity.class));
                break;
            case R.id.button7_all:
                startActivity(new Intent(AllMainActivity.this,Main5Activity.class));
                break;
            case R.id.button8_all:
                startActivity(new Intent(AllMainActivity.this,Main6Activity.class));
                break;
        }


    }
}
