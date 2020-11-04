package com.allandroidprojects.ecomsample.options;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allandroidprojects.ecomsample.Mcommerce.ArticlRech;
import com.allandroidprojects.ecomsample.Mcommerce.Article;
import com.allandroidprojects.ecomsample.Mcommerce.LingeCommand;
import com.allandroidprojects.ecomsample.Mcommerce.Markeplace;
import com.allandroidprojects.ecomsample.R;
import com.allandroidprojects.ecomsample.agent.ChatClientInterface;
import com.allandroidprojects.ecomsample.gui.MainActivity1;
import com.allandroidprojects.ecomsample.product.ItemDetailsActivity;
import com.allandroidprojects.ecomsample.product.ToutProduit;
import com.allandroidprojects.ecomsample.startup.MainActivity;
import com.allandroidprojects.ecomsample.utility.ImageUrlUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.logging.Level;

import jade.core.MicroRuntime;
import jade.util.Logger;
import jade.wrapper.ControllerException;

import static com.allandroidprojects.ecomsample.fragments.ImageListFragment.STRING_IMAGE_POSITION;
import static com.allandroidprojects.ecomsample.fragments.ImageListFragment.STRING_IMAGE_URI;

public class Resulta_recharch extends AppCompatActivity {

    private EditText marqText;
    private   EditText prixText;
    private Button rechButton;
    private ChatClientInterface chatClientInterface;
    private String nickname;
    private MyReceiver myReceiver;
    private  TextView markting,showmor;
    private static Context mContext;
    private ArrayList<Markeplace> listArt;
    private SimpleStringRecyclerViewAdapter adabter;
    private Logger logger = Logger.getJADELogger(this.getClass().getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        logger.log(Level.INFO, "nikname************************************ " + MainActivity1.nickname);


        try {
            chatClientInterface = MicroRuntime.getAgent(MainActivity1.nickname)
                    .getO2AInterface(ChatClientInterface.class);
        } catch (ControllerException e) {
            e.printStackTrace();
        }


        myReceiver = new MyReceiver();

        IntentFilter killFilter = new IntentFilter();
        killFilter.addAction("repondRecherch");
        setContentView(R.layout.layout_recylerview_list);
        registerReceiver(myReceiver, killFilter);




        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager recylerViewLayoutManager = new LinearLayoutManager(mContext);

        recyclerView.setLayoutManager(recylerViewLayoutManager);
        listArt=new ArrayList<Markeplace>();
        adabter=new SimpleStringRecyclerViewAdapter(recyclerView,listArt );
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adabter);
        if(getIntent()!=null) {
            Intent intent=getIntent();
           ArticlRech articlRech=(ArticlRech)intent.getSerializableExtra("recharche");
            chatClientInterface.recherchArticle(articlRech);
        }

    }
    private View.OnClickListener buttonRechListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String marqT=marqText.getText().toString();
            String prix=prixText.getText().toString();

            if(!marqT.isEmpty() && !prix.isEmpty() )
            {
                ArticlRech articlRech=new ArticlRech();
                articlRech.setMarque(marqT);
                articlRech.setPrix(Integer.parseInt(prix));
                listArt.clear();
                chatClientInterface.recherchArticle(articlRech);

            }

        }
    };


    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            logger.log(Level.INFO, "Received intent " + action);

            if (action.equalsIgnoreCase("repondRecherch")) {
                logger.log(Level.INFO, "repond recharch **************************");
                Markeplace A = (Markeplace) intent.getSerializableExtra("repondRecherch");
                //  logger.log(Level.INFO, "brod recharch **************************"+A.get(0).);


                listArt.add(A);
                logger.log(Level.INFO, "repond recharch **************************"+" "+listArt.size());
                adabter.notifyDataSetChanged();

            }
        }
    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private ArrayList<Markeplace> mValues;
        private RecyclerView mRecyclerView;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final RecyclerView recyclerView1;

            private final  TextView markting,showmor;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                recyclerView1 = (RecyclerView)mView.findViewById(R.id.recyclerview1);

                markting=(TextView)mView.findViewById(R.id.markting);
                showmor=(TextView)mView.findViewById(R.id.showmor);

            }
        }

        public SimpleStringRecyclerViewAdapter(RecyclerView recyclerView, ArrayList<Markeplace> items) {
            mValues = items;
            mRecyclerView = recyclerView;
        }

        @Override
        public SimpleStringRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_listitem, parent, false);
            return new SimpleStringRecyclerViewAdapter.ViewHolder(view);
        }




        @Override
        public void onViewRecycled(SimpleStringRecyclerViewAdapter.ViewHolder holder) {

        }

        @Override
        public void onBindViewHolder(final SimpleStringRecyclerViewAdapter.ViewHolder holder, final int position) {
           /* FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) holder.mImageView.getLayoutParams();
            if (mRecyclerView.getLayoutManager() instanceof GridLayoutManager) {
                layoutParams.height = 200;
            } else if (mRecyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                layoutParams.height = 600;
            } else {
                layoutParams.height = 800;

            }*/
            holder.markting.setText(mValues.get(position).getName());
            holder.showmor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ToutProduit.class);
                    intent.putExtra("Articles",mValues.get(position));
                    mContext.startActivity(intent);
                }
            });
            SimpleStringRecyclerViewAdapt a=new SimpleStringRecyclerViewAdapt(holder.recyclerView1,(ArrayList<Article>)mValues.get(position).getListArticle());
            holder.recyclerView1.setAdapter(a);
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
            holder.recyclerView1.setLayoutManager(layoutManager);
            Toast.makeText(holder.mView.getContext(),"fffffffffffff",Toast.LENGTH_SHORT).show();
        }
        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }

















    public static class SimpleStringRecyclerViewAdapt
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapt.ViewHolder> {

        private ArrayList<Article> mCartlistImageUri;
        private RecyclerView mRecyclerView;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final SimpleDraweeView mImageView;
            public final LinearLayout mLayoutItem, mLayoutRemove , mLayoutEdit;
            public final TextView nom_P,catg_P,prix_P,markpalce,quant;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (SimpleDraweeView) view.findViewById(R.id.image_cartlist);
                mLayoutItem = (LinearLayout) view.findViewById(R.id.layout_item_desc);
                mLayoutRemove = (LinearLayout) view.findViewById(R.id.layout_action1);
                mLayoutEdit = (LinearLayout) view.findViewById(R.id.layout_action2);
                nom_P=(TextView) view.findViewById(R.id.nom_P);
                catg_P=(TextView) view.findViewById(R.id.catg_P);
                prix_P=(TextView) view.findViewById(R.id.prix_P);
                markpalce=(TextView) view.findViewById(R.id.markpalce);
                quant=(TextView) view.findViewById(R.id.quant);
            }
        }

        public SimpleStringRecyclerViewAdapt(RecyclerView recyclerView, ArrayList<Article> wishlistImageUri) {
            mCartlistImageUri = wishlistImageUri;
            mRecyclerView = recyclerView;
        }

        @Override
        public SimpleStringRecyclerViewAdapt.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.resulat_recherch, parent, false);
            return new SimpleStringRecyclerViewAdapt.ViewHolder(view);
        }

        @Override
        public void onViewRecycled(SimpleStringRecyclerViewAdapt.ViewHolder holder) {
            if (holder.mImageView.getController() != null) {
                holder.mImageView.getController().onDetach();
            }
            if (holder.mImageView.getTopLevelDrawable() != null) {
                holder.mImageView.getTopLevelDrawable().setCallback(null);
//                ((BitmapDrawable) holder.mImageView.getTopLevelDrawable()).getBitmap().recycle();
            }
        }

        @Override
        public void onBindViewHolder(final SimpleStringRecyclerViewAdapt.ViewHolder holder, final int position) {
            final Uri uri = Uri.parse("http://192.168.1.6/"+mCartlistImageUri.get(position).getImags_p().iterator().next());
            holder.mImageView.setImageURI(uri);
         /*   holder.mLayoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ItemDetailsActivity.class);
                    intent.putExtra(STRING_IMAGE_URI,mCartlistImageUri.get(position));
                    intent.putExtra(STRING_IMAGE_POSITION, position);
                    mContext.startActivity(intent);
                }
            });*/

            //Set click action
            Article article=mCartlistImageUri.get(position);
            holder.nom_P.setText("Nom : "+article.getNom_p());
            holder.catg_P.setText("Marque : "+article.getMarque_p()+" , "+article.getLibCat_p());
            holder.prix_P.setText(article.getPrix_P()+"");


            holder.quant.setText(article.getQuanSt_p()+"");

            holder.markpalce.setText("Magasine : "+article.getMarkeplace().getName());


            //Set click action

        }

        @Override
        public int getItemCount() {
            return mCartlistImageUri.size();
        }
    }
}



