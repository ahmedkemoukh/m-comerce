package com.allandroidprojects.ecomsample.product;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allandroidprojects.ecomsample.Mcommerce.Article;
import com.allandroidprojects.ecomsample.Mcommerce.Markeplace;
import com.allandroidprojects.ecomsample.R;
import com.allandroidprojects.ecomsample.fragments.ImageListFragment;
import com.allandroidprojects.ecomsample.options.SearchResultActivity;
import com.allandroidprojects.ecomsample.utility.ImageUrlUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.logging.Level;

import jade.util.Logger;

import static com.allandroidprojects.ecomsample.fragments.ImageListFragment.STRING_IMAGE_POSITION;
import static com.allandroidprojects.ecomsample.fragments.ImageListFragment.STRING_IMAGE_URI;

public class ToutProduit extends AppCompatActivity {
    private static Context mContext;
    private  TextView markting,showmor;
    private SimpleStringRecyclerViewAdapter adabter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_listitem);
        markting=(TextView)findViewById(R.id.markting);
        showmor=(TextView)findViewById(R.id.showmor);
        showmor.setVisibility(View.GONE);
        mContext = ToutProduit.this;
        if (getIntent() != null) {
            Intent intent=getIntent();
           Markeplace listArt=(Markeplace) intent.getSerializableExtra("Articles");;
            RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview1);
            RecyclerView.LayoutManager recylerViewLayoutManager = new LinearLayoutManager(mContext);

            recyclerView.setLayoutManager(recylerViewLayoutManager);
            markting.setText(listArt.getName());
            adabter=new SimpleStringRecyclerViewAdapter(recyclerView,(ArrayList<Article>) listArt.getListArticle());
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adabter);
        }
    }



    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private ArrayList<Article> mValues;
        private RecyclerView mRecyclerView;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final SimpleDraweeView mImageView;
            public final LinearLayout mLayoutItem;
            public final ImageView mImageViewWishlist;
            public final TextView nom_P,marq_P,prix_P;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (SimpleDraweeView) view.findViewById(R.id.image1);
                mLayoutItem = (LinearLayout) view.findViewById(R.id.layout_item);
                mImageViewWishlist = (ImageView) view.findViewById(R.id.ic_wishlist);
                nom_P=(TextView)view.findViewById(R.id.nom_P);
                marq_P=(TextView)view.findViewById(R.id.marq_P);
                prix_P=(TextView)view.findViewById(R.id.prix_P);
            }
        }

        public SimpleStringRecyclerViewAdapter(RecyclerView recyclerView, ArrayList<Article> items) {
            mValues = items;
            mRecyclerView = recyclerView;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onViewRecycled(ViewHolder holder) {
            if (holder.mImageView.getController() != null) {
                holder.mImageView.getController().onDetach();
            }
            if (holder.mImageView.getTopLevelDrawable() != null) {
                holder.mImageView.getTopLevelDrawable().setCallback(null);
//                ((BitmapDrawable) holder.mImageView.getTopLevelDrawable()).getBitmap().recycle();
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
           /* FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) holder.mImageView.getLayoutParams();
            if (mRecyclerView.getLayoutManager() instanceof GridLayoutManager) {
                layoutParams.height = 200;
            } else if (mRecyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                layoutParams.height = 600;
            } else {
                layoutParams.height = 800;
            }*/
            final Uri uri = Uri.parse("http://192.168.1.6/"+mValues.get(position).getImags_p().iterator().next());
            Logger logger = Logger.getJADELogger(this.getClass().getName());

            logger.log(Level.INFO, "******************************http://192.168.1.6/"+mValues.get(position).getImags_p().iterator().next());
            holder.mImageView.setImageURI(uri);
            holder.mLayoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ItemDetailsActivity.class);
                    intent.putExtra(STRING_IMAGE_URI, "http://192.168.1.6/"+mValues.get(position).getImags_p().iterator().next());
                    intent.putExtra(STRING_IMAGE_POSITION, position);
                    intent.setAction("article");
                    intent.putExtra("Article",mValues.get(position));
                   mContext.startActivity(intent);

                }
            });


            //Set click action for wishlist
     /*       holder.mImageViewWishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
                    imageUrlUtils.addWishlistImageUri("http://192.168.1.6/"+mValues.get(position).getImags_p().iterator().next());
                    holder.mImageViewWishlist.setImageResource(R.drawable.ic_favorite_black_18dp);
                    notifyDataSetChanged();
                    Toast.makeText(mActivity,"Item added to wishlist.",Toast.LENGTH_SHORT).show();

                }
            });*/

            holder.nom_P.setText(mValues.get(position).getNom_p());
            holder.marq_P.setText(mValues.get(position).getMarque_p());
            holder.prix_P.setText(mValues.get(position).getPrix_P()+"");

        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }
}
