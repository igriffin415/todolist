package com.example.firei.todo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.io.Serializable;
import java.util.ArrayList;

public class History extends AppCompatActivity {

    public static ArrayList<String> TODO = new ArrayList<String>();
    public static ArrayList<String> HISTORY = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("** START **");
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        TODO = (ArrayList) intent.getSerializableExtra("todo");
        HISTORY = (ArrayList) intent.getSerializableExtra("history");
        setContentView(R.layout.activity_history);

        final LinearLayout list = (LinearLayout) findViewById(R.id.historyList);
        for(int i = 0; i < HISTORY.size(); i++){
            String text = HISTORY.get(i);
            createCheckbox(list, text);
        }

        Button ret = (Button) findViewById(R.id.ret);
        ret.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("todo", TODO);
                intent.putExtra("history", HISTORY);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        Button restore = (Button) findViewById(R.id.restore);
        restore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for(int i = 0; i <list.getChildCount(); i++){
                    try{
                        CheckBox cb = (CheckBox) list.getChildAt(i);
                        if(cb.isChecked()){
                            String text = cb.getText().toString();
                            HISTORY.remove(text);
                            TODO.add(text);
                        }
                    }catch(Exception e){
                        System.out.println(":^(");
                        System.out.println(e.getMessage());
                    }

                }
                setLayout();
            }
        });

        Button clear = (Button) findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for(int i = 0; i <list.getChildCount(); i++){
                    try{
                        CheckBox cb = (CheckBox) list.getChildAt(i);
                        if(cb.isChecked()){
                            String text = cb.getText().toString();
                            HISTORY.remove(text);
                        }
                    }catch(Exception e){
                        System.out.println(":^(");
                        System.out.println(e.getMessage());
                    }

                }
                setLayout();
            }
        });

    }

    private void setLayout(){
        LinearLayout list = (LinearLayout) findViewById(R.id.historyList);
        list.removeAllViews();
        for(int i = 0; i < HISTORY.size(); i++){
            String text = HISTORY.get(i);
            createCheckbox(list, text);
        }

    }

    private LinearLayout createCheckbox(final LinearLayout list, String text){
        CheckBox ch = new CheckBox(History.this);
        ch.setText(text);
        list.addView(ch);
//        ch.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                HISTORY.remove(((CheckBox) v).getText().toString());
////                TODO.add(((CheckBox) v).getText().toString());
////                list.removeView(v);
//            }
//        });
        return list;
    }
}
