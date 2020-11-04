/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.allandroidprojects.ecomsample.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.allandroidprojects.ecomsample.agent.ChatClientInterface;
import com.allandroidprojects.ecomsample.gui.MainActivity1;
import com.allandroidprojects.ecomsample.product.ItemDetailsActivity;
import com.allandroidprojects.ecomsample.product.ToutProduit;
import com.allandroidprojects.ecomsample.startup.MainActivity;
import com.allandroidprojects.ecomsample.utility.ImageUrlUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import jade.core.MicroRuntime;
import jade.util.Logger;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;


public class ImageListFragment extends Fragment {

    public static final String STRING_IMAGE_URI = "ImageUri";
    public static final String STRING_IMAGE_POSITION = "ImagePosition";
    private static MainActivity mActivity;
    private ChatClientInterface chatClientInterface;
    private String nickname;
    private MyReceiver myReceiver;
    public static  ArrayList<Markeplace> electronique=new ArrayList<>();
    public static  ArrayList<Markeplace> informatique=new ArrayList<>();
    public static  ArrayList<Markeplace> mobile=new ArrayList<>();
    public static  ArrayList<Markeplace> chaussure=new ArrayList<>();
    private  Logger logger = Logger.getJADELogger(this.getClass().getName());

 public  RecyclerView rv;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
        Bundle extras = mActivity.getIntent().getExtras();
        if (extras != null) {
            nickname = extras.getString("nickname");
        }
        try {
            chatClientInterface =MicroRuntime.getAgent(nickname)
                    .getO2AInterface(ChatClientInterface.class);
        } catch (StaleProxyException e) {
            logger.log(Level.INFO, "Received intent************************************ " + 1);
        } catch (ControllerException e) {
            logger.log(Level.INFO, "Received intent************************************ " + 2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         rv = (RecyclerView) inflater.inflate(R.layout.layout_recylerview_list, container, false);
        setupRecyclerView(rv);
        Toast.makeText(mActivity,"create",Toast.LENGTH_SHORT).show();

        return rv;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
      /*  if (ImageListFragment.this.getArguments().getInt("type") == 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        } else if (ImageListFragment.this.getArguments().getInt("type") == 2) {
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
        } else {
            GridLayoutManager layoutManager = new GridLayoutManager(recyclerView.getContext(), 3);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
        }*/
        String[] items=null;
        if (ImageListFragment.this.getArguments().getInt("type") == 1){
           // items =ImageUrlUtils.getOffersUrls();
            electronique.clear();
            chatClientInterface.DemandArticle(getString(R.string.item_1),MainActivity.curentype);
            recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(recyclerView,electronique));
            Toast.makeText(mActivity,"offer",Toast.LENGTH_SHORT).show();
        }else if (ImageListFragment.this.getArguments().getInt("type") == 2){
           // items =ImageUrlUtils.getElectronicsUrls();
            informatique.clear();
            chatClientInterface.DemandArticle(getString(R.string.item_2),MainActivity.curentype);
            recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(recyclerView,informatique));
        }else if (ImageListFragment.this.getArguments().getInt("type") == 3){
            mobile.clear();
            chatClientInterface.DemandArticle(getString(R.string.item_3),MainActivity.curentype);
            recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(recyclerView,mobile));
            items =ImageUrlUtils.getLifeStyleUrls();
            Toast.makeText(mActivity,"style",Toast.LENGTH_SHORT).show();
        }else if (ImageListFragment.this.getArguments().getInt("type") == 4){
            chaussure.clear();
            chatClientInterface.DemandArticle(getString(R.string.item_4),MainActivity.curentype);
            recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(recyclerView,chaussure));
            items =ImageUrlUtils.getHomeApplianceUrls();
            Toast.makeText(mActivity,"applica",Toast.LENGTH_SHORT).show();
        }else if (ImageListFragment.this.getArguments().getInt("type") == 5){
            items =ImageUrlUtils.getBooksUrls();
            Toast.makeText(mActivity,"book",Toast.LENGTH_SHORT).show();
        }else {
            items = ImageUrlUtils.getImageUrls();
            Toast.makeText(mActivity,"util",Toast.LENGTH_SHORT).show();
        }
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private ArrayList<Markeplace> mValues;
        private RecyclerView mRecyclerView;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
           public final RecyclerView recyclerView1;
           public final TextView markting,showmor;


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
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_listitem, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onViewRecycled(ViewHolder holder) {

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

           holder.markting.setText(mValues.get(position).getName());
           holder.showmor.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(mActivity, ToutProduit.class);
                   intent.putExtra("Articles",mValues.get(position));
                   mActivity.startActivity(intent);
               }
           });
            SimpleStringRecyclerViewAdapt a=new SimpleStringRecyclerViewAdapt(holder.recyclerView1,(ArrayList<Article>) mValues.get(position).getListArticle());
            holder.recyclerView1.setAdapter(a);
            StaggeredGridLayoutManager layoutManager;
            if(MainActivity.curentype==0)
             layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
            else
                layoutManager  = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            holder.recyclerView1.setLayoutManager(layoutManager);
           Toast.makeText(holder.mView.getContext(),"fffffffffffff",Toast.LENGTH_SHORT).show();
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
            logger.log(Level.INFO, "Received intent " + action);

            if (action.equalsIgnoreCase("ListArticle")) {
                List<Article> A;
                A = (List<Article>)intent.getSerializableExtra("ListArt");
                logger.log(Level.INFO, "Received intent************************************ " + A.get(0).getMarque_p());

            }
        }
    }

    public static class SimpleStringRecyclerViewAdapt
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapt.ViewHolder> {

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

        public SimpleStringRecyclerViewAdapt(RecyclerView recyclerView, ArrayList<Article> items) {
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
                    Intent intent = new Intent(mActivity, ItemDetailsActivity.class);
                    intent.putExtra(STRING_IMAGE_URI, "http://192.168.1.6/"+mValues.get(position).getImags_p().iterator().next());
                    intent.putExtra(STRING_IMAGE_POSITION, position);
                    intent.setAction("article");
                    intent.putExtra("Article",mValues.get(position));
                    mActivity.startActivity(intent);

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

            holder.nom_P.setText("Nom : "+mValues.get(position).getNom_p());
            holder.marq_P.setText("Marque : "+mValues.get(position).getMarque_p()+" , "+mValues.get(position).getLibCat_p());
            holder.prix_P.setText(mValues.get(position).getPrix_P()+"");

        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }


}
