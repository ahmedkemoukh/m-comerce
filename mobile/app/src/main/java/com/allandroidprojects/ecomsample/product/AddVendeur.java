package com.allandroidprojects.ecomsample.product;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allandroidprojects.ecomsample.Login.CustomToast;
import com.allandroidprojects.ecomsample.Login.Utils;
import com.allandroidprojects.ecomsample.Mcommerce.Markeplace;
import com.allandroidprojects.ecomsample.Mcommerce.Utilisateur;
import com.allandroidprojects.ecomsample.R;
import com.allandroidprojects.ecomsample.agent.ChatClientAgent;
import com.allandroidprojects.ecomsample.agent.ChatClientInterface;
import com.allandroidprojects.ecomsample.gui.MainActivity1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jade.core.MicroRuntime;
import jade.wrapper.ControllerException;

public class AddVendeur extends AppCompatActivity {

    private static EditText name,position;
    private static TextView textView3;
    private static Button ajouter;
    private static LinearLayout linear;
    private ChatClientInterface  chatClientInterface;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_addvendeur);
        name = (EditText) findViewById(R.id.name);
        position = (EditText) findViewById(R.id.position);
        ajouter = (Button) findViewById(R.id.ajouter);
        textView3=(TextView)findViewById(R.id.textView3);
        linear=(LinearLayout)findViewById(R.id.layoutD);
        if(ChatClientAgent.monAchteur.getMarkplace()!=null)
        {
            if(ChatClientAgent.monAchteur.getMarkplace().getEtatMarkplace()==0)
            {
                textView3.setVisibility(View.VISIBLE);
                linear.setVisibility(View.GONE);
            }
            if(ChatClientAgent.monAchteur.getMarkplace().getEtatMarkplace()==-1)
            {
                textView3.setVisibility(View.VISIBLE);
                textView3.setText("le dernier demande est refusee");

            }

        }
        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });
        try {
            chatClientInterface = MicroRuntime.getAgent(MainActivity1.nickname)
                    .getO2AInterface(ChatClientInterface.class);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }

    private void checkValidation() {

        // Get all edittext texts
        String getName = name.getText().toString();
        String getPosition=position.getText().toString();

        // Pattern match for email id


        // Check if all strings are null or not
        if (getName.equals("") || getName.length() == 0
                ||getPosition.equals("") || getPosition.length() == 0
                )
            new CustomToast().Show_Toast(AddVendeur.this, null,
                    "All fields are required.");
        else {
            Utilisateur us=  ChatClientAgent.monAchteur;
            us.setType(1);
            Markeplace getMarkplace=new Markeplace();
            getMarkplace.setUser(us);

           getMarkplace.setName(getName);
            getMarkplace.setPosition(getPosition);
          ChatClientAgent.monAchteur.setMarkplace(getMarkplace);
            ChatClientAgent.monAchteur.getMarkplace().setEtatMarkplace(0);
             chatClientInterface.addMarkPlace(getMarkplace);
            textView3.setVisibility(View.VISIBLE);
            linear.setVisibility(View.GONE);

            Toast.makeText(AddVendeur.this, "Do SignUp.", Toast.LENGTH_SHORT)
                    .show();
        }

    }
}
