package com.allandroidprojects.ecomsample.product;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.allandroidprojects.ecomsample.Mcommerce.Command;
import com.allandroidprojects.ecomsample.Mcommerce.Compte;
import com.allandroidprojects.ecomsample.Mcommerce.LingeCommand;
import com.allandroidprojects.ecomsample.Mcommerce.Resulta;
import com.allandroidprojects.ecomsample.R;
import com.allandroidprojects.ecomsample.agent.ChatClientAgent;
import com.allandroidprojects.ecomsample.agent.ChatClientInterface;
import com.allandroidprojects.ecomsample.gui.MainActivity1;
import com.allandroidprojects.ecomsample.miscellaneous.EmptyActivity;
import com.allandroidprojects.ecomsample.notification.NotificationCountSetClass;
import com.allandroidprojects.ecomsample.startup.MainActivity;
import com.allandroidprojects.ecomsample.utility.ImageUrlUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;

import jade.core.MicroRuntime;
import jade.util.Logger;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

public class Payment extends AppCompatActivity {
private Button button;
    private MyReceiver myReceiver;
private EditText mobileNumber,location,infoU,creditcard,cartid;
    private ChatClientInterface chatClientInterface;
    private Logger logger = Logger.getJADELogger(this.getClass().getName());
  private TextView textview;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            chatClientInterface = MicroRuntime.getAgent(MainActivity1.nickname)
                    .getO2AInterface(ChatClientInterface.class);
        } catch (StaleProxyException e) {
            logger.log(Level.INFO, "Received intent************************************ " + 1);
        } catch (ControllerException e) {
            logger.log(Level.INFO, "Received intent************************************ " + 2);
        }

        myReceiver = new MyReceiver();

        IntentFilter killFilter = new IntentFilter();
        killFilter.addAction("resultaPaymrnt");
        setContentView(R.layout.layout_recylerview_list);
        registerReceiver(myReceiver, killFilter);
       setContentView(R.layout.layout_payment);
       textview=(TextView)findViewById(R.id.textView);
        mobileNumber=(EditText)findViewById(R.id.mobileNumber);
        location=(EditText)findViewById(R.id.location);

        creditcard=(EditText)findViewById(R.id.cart);
        cartid=(EditText)findViewById(R.id.cartid);;
        button=(Button) findViewById(R.id.button);
        button.setOnClickListener(paymentListener);
    }


    private OnClickListener paymentListener=new OnClickListener() {
        @Override
        public void onClick(View v) {
            ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
            String getmobileNumber=mobileNumber.getText().toString();
            String getlocation=location.getText().toString();
            String getcreditcard=creditcard.getText().toString();
            String getcartid=cartid.getText().toString();

            if(getmobileNumber.isEmpty() || getlocation.isEmpty() || getcreditcard.isEmpty() || getcartid.isEmpty())
            {
                Toast.makeText(Payment.this,"remplire tout les chanes",Toast.LENGTH_LONG).show();
            }
            else {

                Compte compte=new Compte();
                compte.setCod_post(Integer.parseInt(getcreditcard));
                compte.setPassword(getcartid);
                imageUrlUtils.getCartListImageUri().setLocation(getlocation);
                imageUrlUtils.getCartListImageUri().setNumberPhon(getmobileNumber);
                ChatClientAgent.monAchteur.setCompte(compte);
                imageUrlUtils.getCartListImageUri().setAchteur(ChatClientAgent.monAchteur);

                chatClientInterface.AchatArt(imageUrlUtils.getCartListImageUri());
            }
          /*  if (!cod.isEmpty()) {
                list.setCodPost(cod);
                list.setList(imageUrlUtils.getCartListImageUri());



                Toast.makeText(v.getContext(), cod, Toast.LENGTH_SHORT).show();
                finish();
            }*/
        }
    };


    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            logger.log(Level.INFO, "Received intent " + action);

            if (action.equalsIgnoreCase("resultaPaymrnt")) {
               String etat=(String)intent.getSerializableExtra("resultaPaymrnt");
              switch (etat) {
                  case "0":
                      logger.log(Level.INFO, "Recdddddddddetetetetetetdddddeived intent " +etat);
                      textview.setText("L'argent ne suffit pas");

                      break;
                  case "1":
                      logger.log(Level.INFO, "Recdddddddddetetetetetetdddddeived intent " +etat);
                      textview.setText("Mauvaise carte");


                      break;
                  case "2":
                      Intent intent1=new Intent(Payment.this, EmptyActivity.class);

                      logger.log(Level.INFO, "Recdddddddddetetetetetetdddddeived intent " +etat);
                      ImageUrlUtils imageUrlUtils = new ImageUrlUtils();

                   imageUrlUtils.getCartListImageUri().getLignsCommand().clear();
                      MainActivity.notificationCountCart=0;
                      NotificationCountSetClass.setNotifyCount(MainActivity.notificationCountCart);
                      startActivity(intent1);
                      finish();
                      break;
              }
            }
        }
    }
}
