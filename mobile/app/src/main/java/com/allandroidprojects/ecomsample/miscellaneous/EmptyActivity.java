package com.allandroidprojects.ecomsample.miscellaneous;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allandroidprojects.ecomsample.Mcommerce.Article;
import com.allandroidprojects.ecomsample.Mcommerce.Command;
import com.allandroidprojects.ecomsample.Mcommerce.Vente;
import com.allandroidprojects.ecomsample.R;
import com.allandroidprojects.ecomsample.options.SearchResultActivity;
import com.allandroidprojects.ecomsample.product.ActiveCommand;
import com.allandroidprojects.ecomsample.product.ItemDetailsActivity;
import com.allandroidprojects.ecomsample.utility.ImageUrlUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import static com.allandroidprojects.ecomsample.fragments.ImageListFragment.STRING_IMAGE_POSITION;
import static com.allandroidprojects.ecomsample.fragments.ImageListFragment.STRING_IMAGE_URI;

public class EmptyActivity extends AppCompatActivity {
    private SimpleStringRecyclerViewAdapter adabter;

    private static Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
  mContext=EmptyActivity.this;

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager recylerViewLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(recylerViewLayoutManager);
        adabter=new SimpleStringRecyclerViewAdapter(recyclerView, ImageUrlUtils.listArt );
        recyclerView.setAdapter(adabter);
    }




    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private ArrayList<Command> listArticle;
        private RecyclerView mRecyclerView;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;

            public final LinearLayout  mLayoutEdit;
           public final TextView getId,getachteur,getVendeur,getdate,getEta,textaction2;

            public ViewHolder(View view) {
                super(view);
                mView = view;

                mLayoutEdit = (LinearLayout) view.findViewById(R.id.layout_action2);
               getId=(TextView)view.findViewById(R.id.getId);
               getachteur=(TextView)view.findViewById(R.id.getachteur);
                getVendeur=(TextView)view.findViewById(R.id.getVendeur);
                getdate=(TextView)view.findViewById(R.id.getdate);
                getEta=(TextView)view.findViewById(R.id.getEta);
                textaction2=(TextView)view.findViewById(R.id.text_action2);

            }
        }

        public SimpleStringRecyclerViewAdapter(RecyclerView recyclerView, ArrayList<Command> Article) {
            listArticle = Article;
            mRecyclerView = recyclerView;
        }

        @Override
        public SimpleStringRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layou_command, parent, false);
            return new SimpleStringRecyclerViewAdapter.ViewHolder(view);
        }




        public void onViewRecycled(SimpleStringRecyclerViewAdapter.ViewHolder holder) {

        }

        @Override
        public void onBindViewHolder(final SimpleStringRecyclerViewAdapter.ViewHolder holder, final int position) {
        //    final Uri uri = Uri.parse("http://192.168.1.3/"+listArticle.get(position).getArticle().getImags_p().iterator().next());
        //    holder.mImageView.setImageURI(uri);

            holder.getId.setText("#Id : "+listArticle.get(position).getId_C()+"");
            holder.getachteur.setText("Achteur : "+listArticle.get(position).getAchteur().getNom_U()+" "+listArticle.get(position).getAchteur().getPrenom_U());
            holder.getVendeur.setText("Vendeur : "+listArticle.get(position).getVendeur().getNom_U()+" "+listArticle.get(position).getVendeur().getPrenom_U());
            holder.getdate.setText("Date : 29/08/2020");
            holder.getEta.setText("Etat : "+listArticle.get(position).getEtat());
           /* holder.mLayoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ItemDetailsActivity.class);
                  //  intent.putExtra(STRING_IMAGE_URI,"http://192.168.1.3/"+listArticle.get(position).getArticle().getImags_p().iterator().next());
                    intent.putExtra(STRING_IMAGE_POSITION, position);
               //     intent.putExtra("Article",listArticle.get(position).getArticle());
                    ImageUrlUtils.listArt.remove( ImageUrlUtils.listArt.get(position));
                   mRecyclerView.getAdapter().notifyDataSetChanged();
                    mContext.startActivity(intent);
                }
            });*/

           holder.textaction2.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(v.getContext(), ActiveCommand.class);


                       intent.putExtra("command",listArticle.get(position));
                   ImageUrlUtils.listArt.remove( ImageUrlUtils.listArt.get(position));
                   mRecyclerView.getAdapter().notifyDataSetChanged();
                   mContext.startActivity(intent);
               }
           });

            //Set click action
            /*holder.mLayoutRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
                    imageUrlUtils.removeCartListImageUri(position);
                    notifyDataSetChanged();
                    //Decrease notification count
                    MainActivity.notificationCountCart--;

                }
            });*/

            //Set click action
            holder.mLayoutEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }

        @Override
        public int getItemCount() {
            return listArticle.size();
        }
    }
}
