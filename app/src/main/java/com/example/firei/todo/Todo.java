package com.example.firei.todo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Todo extends AppCompatActivity {


    public static ArrayList<String> TODO = new ArrayList<String>();
    public static ArrayList<String> HISTORY = new ArrayList<String>();

    final static int HISTORY_ACTION = 1;
    final static int ITEM_ACTION = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        load();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        final LinearLayout list = (LinearLayout) findViewById(R.id.list);
        if (savedInstanceState != null) {
            TODO = (ArrayList) savedInstanceState.getSerializable("todo");
            HISTORY = (ArrayList) savedInstanceState.getSerializable("history");
        }
        setList();


        Button addNew = (Button) findViewById(R.id.add);
        addNew.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Todo.this, NewItem.class);
                startActivityForResult(intent, ITEM_ACTION);
            }
        });

        Button history = (Button) findViewById(R.id.history);
        history.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Todo.this, History.class);
                intent.putExtra("todo", TODO);
                intent.putExtra("history", HISTORY);
                startActivityForResult(intent, 1);
            }
        });

        Button complete = (Button) findViewById(R.id.complete);
        complete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for(int i = 0; i <list.getChildCount(); i++){
                    try{
                        CheckBox cb = (CheckBox) list.getChildAt(i);
                        if(cb.isChecked()){
                            String text = cb.getText().toString();
                            TODO.remove(text);
                            HISTORY.add(text);
                        }
                    }catch(Exception e){
                        System.out.println(":^(");
                        System.out.println(e.getMessage());
                    }

                }
                setList();
            }
        });

        TextView t = (TextView) findViewById(R.id.textView);
        t.setBackgroundColor(0xfdafa8);
        list.addView(t);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == HISTORY_ACTION) {
            if (resultCode == RESULT_OK) {
                TODO = (ArrayList) data.getSerializableExtra("todo");
                HISTORY = (ArrayList) data.getSerializableExtra("history");
                setList();
            }
        }
        if (requestCode == ITEM_ACTION) {
            if (resultCode == RESULT_OK) {
                String text = data.getStringExtra("new item");
                final LinearLayout list = (LinearLayout) findViewById(R.id.list);
                TODO.add(text);
                System.out.println("\t" + text);
                createCheckbox(list, text);
            }
        }
    }

    private void createCheckbox(final LinearLayout list, String text){
        CheckBox ch = new CheckBox(Todo.this);
        ch.setText(text);
        list.addView(ch);
//        ch.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if(HISTORY.size() == 10){
//                    HISTORY.remove(0);
//                }
//                HISTORY.add(((CheckBox) v).getText().toString());
//                TODO.remove(((CheckBox) v).getText().toString());
//                list.removeView(v);
//            }
//        });
    }

    private void setList(){
        final LinearLayout list = (LinearLayout) findViewById(R.id.list);
        list.removeAllViews();
        for(int i = 0; i < TODO.size(); i++){
            String text = TODO.get(i);
            createCheckbox(list, text);
        }
    }

    @Override
    public void onPause() {
        //https://stackoverflow.com/questions/10523801/java-save-binary-code-to-a-file

        try {
            FileOutputStream outputStream = openFileOutput("TODO.dat", Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(outputStream);
            out.writeObject((Serializable) TODO);
            out.writeObject((Serializable) HISTORY);
            out.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onPause();

    }

    private void load(){
        try{
            // Reading the object from a file
            FileInputStream file = openFileInput("TODO.dat");
            ObjectInputStream in = new ObjectInputStream(file);
            // Method for deserialization of object
            TODO = (ArrayList)in.readObject();
            HISTORY = (ArrayList)in.readObject();
            in.close();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("todo", TODO);
        outState.putSerializable("history", HISTORY);
        super.onSaveInstanceState(outState);
    }
}
