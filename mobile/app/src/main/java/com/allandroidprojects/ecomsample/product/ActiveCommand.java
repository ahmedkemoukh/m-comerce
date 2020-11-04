package com.allandroidprojects.ecomsample.product;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allandroidprojects.ecomsample.Mcommerce.Article;
import com.allandroidprojects.ecomsample.Mcommerce.Command;
import com.allandroidprojects.ecomsample.Mcommerce.LingeCommand;
import com.allandroidprojects.ecomsample.Mcommerce.Markeplace;
import com.allandroidprojects.ecomsample.R;
import com.allandroidprojects.ecomsample.agent.ChatClientAgent;
import com.allandroidprojects.ecomsample.agent.ChatClientInterface;
import com.allandroidprojects.ecomsample.gui.MainActivity1;
import com.allandroidprojects.ecomsample.notification.NotificationCountSetClass;
import com.allandroidprojects.ecomsample.startup.MainActivity;
import com.allandroidprojects.ecomsample.utility.ImageUrlUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.logging.Level;

import jade.core.MicroRuntime;
import jade.util.Logger;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

import static com.allandroidprojects.ecomsample.fragments.ImageListFragment.STRING_IMAGE_POSITION;
import static com.allandroidprojects.ecomsample.fragments.ImageListFragment.STRING_IMAGE_URI;

public class ActiveCommand extends AppCompatActivity {
    private Logger logger = Logger.getJADELogger(this.getClass().getName());
    private ChatClientInterface chatClientInterface;
    private static Context mContext;
    private MyReceiver myReceiver;
    private Button changEta;
    private SimpleStringRecyclerViewAdapter adabter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myReceiver = new MyReceiver();

        IntentFilter killFilter = new IntentFilter();
        killFilter.addAction("ChangeEtat");
        setContentView(R.layout.layout_recylerview_list);
        registerReceiver(myReceiver, killFilter);
        setContentView(R.layout.comand_detail);

        try {
            chatClientInterface = MicroRuntime.getAgent(MainActivity1.nickname)
                    .getO2AInterface(ChatClientInterface.class);
        } catch (StaleProxyException e) {
            logger.log(Level.INFO, "Received intent************************************ " + 1);
        } catch (ControllerException e) {
            logger.log(Level.INFO, "Received intent************************************ " + 2);
        }
        changEta=(Button)findViewById(R.id.changEtat);

        mContext = this;
        if (getIntent() != null) {
            Intent intent=getIntent();


            final Command  listArt=(Command) intent.getSerializableExtra("command");
            RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview1);
            RecyclerView.LayoutManager recylerViewLayoutManager = new LinearLayoutManager(mContext);

            recyclerView.setLayoutManager(recylerViewLayoutManager);

            adabter=new SimpleStringRecyclerViewAdapter(recyclerView,(ArrayList<LingeCommand>) listArt.getLignsCommand());

            recyclerView.setAdapter(adabter);
          TextView  getId=(TextView)findViewById(R.id.getId);
            TextView   getachteur=(TextView)findViewById(R.id.getachteur);
            TextView   getVendeur=(TextView)findViewById(R.id.getVendeur);
            TextView   getdate=(TextView)findViewById(R.id.getdate);
            TextView   getEta=(TextView)findViewById(R.id.getEta);
            TextView   textaction2=(TextView)findViewById(R.id.text_action2);



            getId.setText("#Identificateur : "+listArt.getId_C()+"");
            getachteur.setText("Achteur : "+listArt.getAchteur().getNom_U()+" "+listArt.getAchteur().getPrenom_U());
          getVendeur.setText("Vendeur : "+listArt.getVendeur().getNom_U()+" "+listArt.getVendeur().getPrenom_U());
            getdate.setText("Date : 29/08/2020");
            if(listArt.getEtat()==null) {
                getEta.setText("Etat : attende");
                }
                else
            {
                getEta.setText("Etat : " + listArt.getEtat());
            }

            if(ChatClientAgent.monAchteur.getType()==0 || listArt.getEtat()!=null || intent.getAction().equals("listAchat"))
            {
                changEta.setVisibility(View.GONE);
            }

            else
            {
                changEta.setVisibility(View.VISIBLE);
            }
            changEta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chatClientInterface.changeEtat(listArt.getId_C()+"");

                }
            });

        }
        else
        {
            changEta.setVisibility(View.GONE);
        }
    }
    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private ArrayList<LingeCommand> mValues;
        private RecyclerView mRecyclerView;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final SimpleDraweeView mImageView;
            public final TextView nom_P,catg_P,markpalce,prix_P,quant,text_action2;


            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (SimpleDraweeView) view.findViewById(R.id.image_cartlist);
               nom_P=(TextView)view.findViewById(R.id.nom_P);
                catg_P=(TextView)view.findViewById(R.id.catg_P);
                markpalce=(TextView)view.findViewById(R.id.markpalce);
                prix_P=(TextView)view.findViewById(R.id.prix_P);
                quant=(TextView)view.findViewById(R.id.quant);
                text_action2=(TextView)view.findViewById(R.id.text_action2);

            }
        }

        public SimpleStringRecyclerViewAdapter(RecyclerView recyclerView, ArrayList<LingeCommand> items) {
            mValues = items;
            mRecyclerView = recyclerView;
        }

        @Override
        public SimpleStringRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ling_commd, parent, false);
            return new SimpleStringRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onViewRecycled(SimpleStringRecyclerViewAdapter.ViewHolder holder) {
            if (holder.mImageView.getController() != null) {
                holder.mImageView.getController().onDetach();
            }
            if (holder.mImageView.getTopLevelDrawable() != null) {
                holder.mImageView.getTopLevelDrawable().setCallback(null);
//                ((BitmapDrawable) holder.mImageView.getTopLevelDrawable()).getBitmap().recycle();
            }
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
            final Uri uri = Uri.parse("http://192.168.1.6/"+mValues.get(position).getArticle().getImags_p().iterator().next());
            Logger logger = Logger.getJADELogger(this.getClass().getName());
           holder.mImageView.setImageURI(uri);
            logger.log(Level.INFO, "******************************http://192.168.1.6/"+mValues.get(position).getArticle().getImags_p().iterator().next());
            holder.mImageView.setImageURI(uri);
            holder.nom_P.setText(mValues.get(position).getArticle().getNom_p());
            holder.catg_P.setText(mValues.get(position).getArticle().getMarque_p()+" , "+mValues.get(position).getArticle().getLibCat_p());
            holder.markpalce.setText(mValues.get(position).getArticle().getMarkeplace().getName());
            holder.prix_P.setText(mValues.get(position).getArticle().getPrix_P()+"");
            holder.quant.setText(mValues.get(position).getQuant()+"");

            holder.text_action2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ItemDetailsActivity.class);
                    intent.putExtra(STRING_IMAGE_URI, "http://192.168.1.6/"+mValues.get(position).getArticle().getImags_p().iterator().next());
                    intent.putExtra(STRING_IMAGE_POSITION, position);
                    intent.setAction("article");
                    intent.putExtra("Article",mValues.get(position).getArticle());
                    mContext.startActivity(intent);

                }
            });

            //Set click action for wishlist
           /* holder.mImageViewWishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
                    imageUrlUtils.addWishlistImageUri("http://192.168.1.3/"+mValues.get(position).getArticle().getImags_p().iterator().next());
                    holder.mImageViewWishlist.setImageResource(R.drawable.ic_favorite_black_18dp);
                    notifyDataSetChanged();
                    Toast.makeText(mContext,"Item added to wishlist.",Toast.LENGTH_SHORT).show();

                }
            });*/

        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }


    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            logger.log(Level.INFO, "ChangeEtat" + action);

            if (action.equalsIgnoreCase("ChangeEtat")) {
                String etat=(String)intent.getSerializableExtra("resultaPaymrnt");
                changEta.setVisibility(View.GONE);
                TextView   getEta=(TextView)findViewById(R.id.getEta);
                getEta.setText("Etat : livre");
            }
        }
    }
}
