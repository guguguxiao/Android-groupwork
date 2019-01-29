package com.homework.grop.group_homework;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class ChatroomActivity extends AppCompatActivity implements View.OnClickListener {
   private EditText editText;
   private TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        name=findViewById(R.id.tv_with_name);
        Bundle bundle=this.getIntent().getExtras();
        name.setText(bundle.getString("message"));
        editText=findViewById(R.id.ed_say);
        editText.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(!(editText.getText()==null||editText.getText().length()==0)) {
            TextView content = findViewById(R.id.tv_content_info);
            content.append("我：" + editText.getText() + "\n");
            content.append(name.getText()+": "+editText.getText()+"\n");
            editText.setText("");

            //Toast.makeText(this, findViewById(R.id.ed_say).getContext().toString(), Toast.LENGTH_SHORT).show();

        }
    }
}
