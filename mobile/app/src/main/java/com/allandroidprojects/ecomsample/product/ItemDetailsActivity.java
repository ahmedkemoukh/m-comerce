package com.allandroidprojects.ecomsample.product;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.allandroidprojects.ecomsample.Mcommerce.Article;
import com.allandroidprojects.ecomsample.Mcommerce.Command;
import com.allandroidprojects.ecomsample.Mcommerce.LingeCommand;
import com.allandroidprojects.ecomsample.R;
import com.allandroidprojects.ecomsample.agent.ChatClientInterface;
import com.allandroidprojects.ecomsample.fragments.ImageListFragment;
import com.allandroidprojects.ecomsample.fragments.ViewPagerActivity;
import com.allandroidprojects.ecomsample.gui.MainActivity1;
import com.allandroidprojects.ecomsample.notification.NotificationCountSetClass;
import com.allandroidprojects.ecomsample.options.CartListActivity;
import com.allandroidprojects.ecomsample.startup.MainActivity;
import com.allandroidprojects.ecomsample.utility.ImageUrlUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import jade.core.MicroRuntime;
import jade.wrapper.ControllerException;

public class ItemDetailsActivity extends AppCompatActivity {
    int imagePosition;
    String stringImageUri;
    Article article;
    private ChatClientInterface chatClientInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        try {
            chatClientInterface = MicroRuntime.getAgent(MainActivity1.nickname)
                    .getO2AInterface(ChatClientInterface.class);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        SimpleDraweeView mImageView = (SimpleDraweeView)findViewById(R.id.image1);
        TextView textViewAddToCart = (TextView)findViewById(R.id.text_action_bottom1);
        TextView textViewBuyNow = (TextView)findViewById(R.id.text_action_bottom2);
        TextView nom_P=(TextView) findViewById(R.id.nom_P);
        TextView marq_P=(TextView) findViewById(R.id.marq_P);
        TextView prix_P=(TextView) findViewById(R.id.prix_P);
        TextView desc_P=(TextView) findViewById(R.id.desc_P);
        TextView quantTot=(TextView) findViewById(R.id.quanTot);
        TextView catg=(TextView) findViewById(R.id.catg_P);
        TextView markp=(TextView) findViewById(R.id.markpalce);
        final EditText quant=(EditText) findViewById(R.id.quant);

        if(MainActivity.curentype==1)
        {
            textViewAddToCart.setText("modifier");
            textViewBuyNow.setText("suprimer");
        }

        //Getting image uri from previous screen
        if (getIntent() != null) {

            Intent intent=getIntent();
            Log.d("ssssssssss",intent.getAction());
            if(intent.getAction().equals("command"))
            {
                LingeCommand comd= (LingeCommand) intent.getSerializableExtra("command");
                quant.setText(comd.getQuant()+"");
               article=comd.getArticle();
            }
            else
            {
                article=(Article) intent.getSerializableExtra("Article");
            }
            stringImageUri = intent.getStringExtra(ImageListFragment.STRING_IMAGE_URI);
            imagePosition = intent.getIntExtra(ImageListFragment.STRING_IMAGE_POSITION,0);

        }
        nom_P.setText("Nom : "+article.getNom_p());
        marq_P.setText("Marque : "+article.getMarque_p()+" , "+article.getLibCat_p());
        prix_P.setText(article.getPrix_P()+"");
        desc_P.setText(article.getDescription_p());
        Uri uri = Uri.parse(stringImageUri);
        quantTot.setText(article.getQuanSt_p()+"");
      //  catg.setText(article.getLibCat_p());
        markp.setText("Magasine : "+article.getMarkeplace().getName());
        mImageView.setImageURI(uri);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(ItemDetailsActivity.this, ViewPagerActivity.class);
                    intent.putExtra("position", imagePosition);
                    intent.putExtra("Article",article);
                    startActivity(intent);

            }
        });

        textViewAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.curentype == 1) {
                      Intent intent1=new Intent(ItemDetailsActivity.this,AddProduit.class);
                    intent1.putExtra("article",article);
                    startActivity(intent1);
                }else
                {
                    ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
                    LingeCommand ling=new LingeCommand();
                  ling.setArticle(article);
                    Log.d("dddddddddd",quant.getText().toString());
                  ling.setQuant(Integer.parseInt(quant.getText().toString()));
                    imageUrlUtils.addCartListImageUri(ling);

                    Toast.makeText(ItemDetailsActivity.this, "Item added to cart.", Toast.LENGTH_SHORT).show();
                    MainActivity.notificationCountCart++;
                    NotificationCountSetClass.setNotifyCount(MainActivity.notificationCountCart);
                }
            }
        });

        textViewBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.curentype == 1) {
                       chatClientInterface.suparticle(article);
                       finish();
                } else {
                    ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
                    LingeCommand ling=new LingeCommand();
                    ling.setArticle(article);
                    Log.d("dddddddddd",quant.getText().toString());
                    ling.setQuant(Integer.parseInt(quant.getText().toString()));
                    imageUrlUtils.addCartListImageUri(ling);
                    MainActivity.notificationCountCart++;
                    NotificationCountSetClass.setNotifyCount(MainActivity.notificationCountCart);
                    startActivity(new Intent(ItemDetailsActivity.this, CartListActivity.class));
                 finish();
                }
            }
        });

    }
}
