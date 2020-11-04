package com.allandroidprojects.ecomsample.options;

import android.app.Activity;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allandroidprojects.ecomsample.Mcommerce.Art;
import com.allandroidprojects.ecomsample.Mcommerce.Article;
import com.allandroidprojects.ecomsample.Mcommerce.Command;
import com.allandroidprojects.ecomsample.Mcommerce.LingeCommand;
import com.allandroidprojects.ecomsample.R;
import com.allandroidprojects.ecomsample.product.ItemDetailsActivity;
import com.allandroidprojects.ecomsample.product.Payment;
import com.allandroidprojects.ecomsample.startup.MainActivity;
import com.allandroidprojects.ecomsample.utility.ImageUrlUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import static com.allandroidprojects.ecomsample.fragments.ImageListFragment.STRING_IMAGE_POSITION;
import static com.allandroidprojects.ecomsample.fragments.ImageListFragment.STRING_IMAGE_URI;

public class CartListActivity extends AppCompatActivity {
    private static Context mContext;

    private Button action_pay;
    static LinearLayout layoutCartItems,layoutCartPayments,layoutCartNoItems;
    static TextView totalprix;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);
        mContext = CartListActivity.this;
        totalprix=(TextView)findViewById(R.id.text_action_bottom1);
        ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
        Command cartlistImageUri =imageUrlUtils.getCartListImageUri();
        //Show cart layout based on items

         layoutCartItems = (LinearLayout) findViewById(R.id.layout_items);
         layoutCartPayments = (LinearLayout) findViewById(R.id.layout_payment);
        layoutCartNoItems = (LinearLayout) findViewById(R.id.layout_cart_empty);
        setCartLayout();

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager recylerViewLayoutManager = new LinearLayoutManager(mContext);

        recyclerView.setLayoutManager(recylerViewLayoutManager);
        recyclerView.setAdapter(new CartListActivity.SimpleStringRecyclerViewAdapter(recyclerView, (ArrayList<LingeCommand>) cartlistImageUri.getLignsCommand()));
        action_pay=(Button)findViewById(R.id.action_Payment);
        action_pay.setOnClickListener(PaymentListener);
    }


    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<CartListActivity.SimpleStringRecyclerViewAdapter.ViewHolder> {

        private ArrayList<LingeCommand> mCartlistImageUri;
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

        public SimpleStringRecyclerViewAdapter(RecyclerView recyclerView, ArrayList<LingeCommand> wishlistImageUri) {
            mCartlistImageUri = wishlistImageUri;
            mRecyclerView = recyclerView;
        }

        @Override
        public CartListActivity.SimpleStringRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cartlist_item, parent, false);
            return new CartListActivity.SimpleStringRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onViewRecycled(CartListActivity.SimpleStringRecyclerViewAdapter.ViewHolder holder) {
            if (holder.mImageView.getController() != null) {
                holder.mImageView.getController().onDetach();
            }
            if (holder.mImageView.getTopLevelDrawable() != null) {
                holder.mImageView.getTopLevelDrawable().setCallback(null);
//                ((BitmapDrawable) holder.mImageView.getTopLevelDrawable()).getBitmap().recycle();
            }
        }

        @Override
        public void onBindViewHolder(final CartListActivity.SimpleStringRecyclerViewAdapter.ViewHolder holder, final int position) {
          final Uri uri = Uri.parse("http://192.168.1.6/"+mCartlistImageUri.get(position).getArticle().getImags_p().iterator().next());
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

            float total=0;
            for(LingeCommand e:mCartlistImageUri)
            {
               total=total+e.getQuant()*e.getArticle().getPrix_P();
            }

            totalprix.setText(total+" DA");
            Article article=mCartlistImageUri.get(position).getArticle();
            holder.nom_P.setText("Nom : "+article.getNom_p());
            holder.catg_P.setText("Marque : "+article.getMarque_p()+" , "+article.getLibCat_p());
            holder.prix_P.setText(article.getPrix_P()+"");


            holder.quant.setText(mCartlistImageUri.get(position).getQuant()+"");

            holder.markpalce.setText("Magasine : "+article.getMarkeplace().getName());
            holder.mLayoutRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
                    imageUrlUtils.removeCartListImageUri(position);
                    notifyDataSetChanged();
                    //Decrease notification count
                    MainActivity.notificationCountCart--;


                    if(MainActivity.notificationCountCart <=0){

                        layoutCartNoItems.setVisibility(View.VISIBLE);
                        layoutCartItems.setVisibility(View.GONE);
                        layoutCartPayments.setVisibility(View.GONE);

                            }



                }
            });

            //Set click action
            holder.mLayoutEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ItemDetailsActivity.class);
                   intent.putExtra(STRING_IMAGE_URI, "http://192.168.1.6/"+mCartlistImageUri.get(position).getArticle().getImags_p().iterator().next());
                    intent.putExtra(STRING_IMAGE_POSITION, position);
                    intent.putExtra("command",mCartlistImageUri.get(position));
                    ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
                    imageUrlUtils.removeCartListImageUri(position);
                    notifyDataSetChanged();
                    //Decrease notification count
                    MainActivity.notificationCountCart--;
                    intent.setAction("command");


                   mContext.startActivity(intent);
                    Activity a=(Activity)mContext;
                    a.finish();

                }
            });
        }

        @Override
        public int getItemCount() {
            return mCartlistImageUri.size();
        }
    }

    protected void setCartLayout(){


        if(MainActivity.notificationCountCart >0){
            layoutCartNoItems.setVisibility(View.GONE);
            layoutCartItems.setVisibility(View.VISIBLE);
            layoutCartPayments.setVisibility(View.VISIBLE);
        }else {
            layoutCartNoItems.setVisibility(View.VISIBLE);
            layoutCartItems.setVisibility(View.GONE);
            layoutCartPayments.setVisibility(View.GONE);

            Button bStartShopping = (Button) findViewById(R.id.bAddNew);
            bStartShopping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }

    private OnClickListener PaymentListener=new OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent= new Intent(CartListActivity.this,Payment.class);
            Toast.makeText(v.getContext(),"ssss",Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
        }
    };


}
