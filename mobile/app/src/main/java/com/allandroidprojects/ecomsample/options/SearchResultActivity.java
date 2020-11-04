package com.allandroidprojects.ecomsample.options;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.allandroidprojects.ecomsample.Login.CustomToast;
import com.allandroidprojects.ecomsample.Mcommerce.ArticlRech;
import com.allandroidprojects.ecomsample.Mcommerce.Article;
import com.allandroidprojects.ecomsample.Mcommerce.Markeplace;
import com.allandroidprojects.ecomsample.R;
import com.allandroidprojects.ecomsample.agent.ChatClientInterface;
import com.allandroidprojects.ecomsample.fragments.ImageListFragment;
import com.allandroidprojects.ecomsample.gui.MainActivity1;
import com.allandroidprojects.ecomsample.product.ItemDetailsActivity;
import com.allandroidprojects.ecomsample.startup.MainActivity;
import com.allandroidprojects.ecomsample.utility.ImageUrlUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import jade.core.MicroRuntime;
import jade.util.Logger;
import jade.wrapper.ControllerException;

import static com.allandroidprojects.ecomsample.fragments.ImageListFragment.STRING_IMAGE_POSITION;
import static com.allandroidprojects.ecomsample.fragments.ImageListFragment.STRING_IMAGE_URI;

public class SearchResultActivity extends AppCompatActivity {

    private  EditText marqText, prixText,catg ,nomproduit;

    private Button rechButton;



    private static Context mContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = SearchResultActivity.this;
setContentView(R.layout.rech_article);
        marqText=(EditText) findViewById(R.id.marqText);
        prixText=(EditText) findViewById(R.id.prixtext);
        catg=(EditText) findViewById(R.id.catg);
        nomproduit=(EditText) findViewById(R.id.nomproduit);
        rechButton=(Button)findViewById(R.id.rechButton);
        rechButton.setOnClickListener(buttonRechListener);

    }
    private OnClickListener buttonRechListener=new OnClickListener() {
        @Override
        public void onClick(View v) {
          String marqT=marqText.getText().toString();
          String prix=prixText.getText().toString();
           String getcat=catg.getText().toString();
            String getnom=nomproduit.getText().toString();



                    ArticlRech articlRech = new ArticlRech();
                    articlRech.setMarque(marqT);
                    articlRech.setGategori(getcat);
                  articlRech.setNom(getnom);
                if(prix.isEmpty())
                {
                    articlRech.setPrix(0);
                }
                else
                {
                    articlRech.setPrix(Integer.parseInt(prix));
                }

                    Intent intent = new Intent(mContext, Resulta_recharch.class);
                    intent.putExtra("recharche", articlRech);
                    startActivity(intent);

            }


    };






}



