package com.allandroidprojects.ecomsample.product;

import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Constraints;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.allandroidprojects.ecomsample.Login.CustomToast;
import com.allandroidprojects.ecomsample.Mcommerce.Article;
import com.allandroidprojects.ecomsample.R;
import com.allandroidprojects.ecomsample.agent.ChatClientInterface;
import com.allandroidprojects.ecomsample.fragments.ImageListFragment;
import com.allandroidprojects.ecomsample.gui.MainActivity1;
import com.allandroidprojects.ecomsample.options.CartListActivity;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import jade.core.MicroRuntime;
import jade.wrapper.ControllerException;

import static com.allandroidprojects.ecomsample.fragments.ImageListFragment.STRING_IMAGE_POSITION;
import static com.allandroidprojects.ecomsample.fragments.ImageListFragment.STRING_IMAGE_URI;

public class AddProduit extends AppCompatActivity {
    private static EditText fullName, emailId, mobileNumber, location,prenom,catg,
            password, confirmPassword,cartpost;

    private  ArrayList<String> listArt;
    private static final int PICK_IMAGE = 2;
    private Article addArt;
    private LinearLayout linearF;
    private ConstraintLayout constraint;
    Button button,buttonR,buttonadd;
    private ChatClientInterface chatClientInterface;
   private SimpleStringRecyclerViewAdapter adabter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_produit);
        try {
            chatClientInterface = MicroRuntime.getAgent(MainActivity1.nickname)
                    .getO2AInterface(ChatClientInterface.class);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        linearF=(LinearLayout)findViewById(R.id.linear_formul);
        constraint=(ConstraintLayout)findViewById(R.id.layout_container);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager recylerViewLayoutManager = new LinearLayoutManager(this);
        addArt=new Article();addArt.setListImage(new ArrayList<byte[]>());
        recyclerView.setLayoutManager(recylerViewLayoutManager);
        listArt=new ArrayList<String>();


        button = (Button)findViewById(R.id.buttonLoadPicture);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        buttonR = (Button)findViewById(R.id.button_V);
        buttonR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraint.setVisibility(View.INVISIBLE);
               linearF.setVisibility(View.VISIBLE);
            }
        });


        fullName = (EditText) findViewById(R.id.fullName);
        prenom = (EditText) findViewById(R.id.prenom);
        emailId = (EditText) findViewById(R.id.userEmailId);
        mobileNumber = (EditText) findViewById(R.id.mobileNumber);
        location = (EditText) findViewById(R.id.location);
        catg=(EditText)findViewById(R.id.catg);


        buttonadd=(Button)findViewById(R.id.signUpBtn);
        buttonadd.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                    addProduit();


            }
        });
        get_Intent();
        adabter=new SimpleStringRecyclerViewAdapter(recyclerView,listArt );
        recyclerView.setAdapter(adabter);
      
    }
public  void get_Intent()
{
    if (getIntent() != null) {
        Intent intent = getIntent();
        Article art = (Article) intent.getSerializableExtra("article");

        Log.d("d","****************************************"+art);
        if (art != null) {
addArt=art;
addArt.setListImage(new ArrayList<byte[]>());
            fullName.setText(addArt.getMarque_p());
            prenom.setText(addArt.getNom_p());
            emailId.setText(addArt.getPrix_P()+"");
            mobileNumber.setText(addArt.getDescription_p());
            location.setText(addArt.getQuanSt_p()+"");
            catg.setText(addArt.getLibCat_p());

          Log.d("sssssssssssssssssss",addArt.getImags_p().iterator().next()+"***********************");
            ArrayList<String >listArt1=new ArrayList<>(addArt.getImags_p());
            for(String img:listArt1)
            {
              listArt.add("http://192.168.1.6/"+img) ;
            }
        }
    }
}
    private void addProduit() {
        String getFullName = fullName.getText().toString();
        String getpren=prenom.getText().toString();
        String getEmailId = emailId.getText().toString();
        String getMobileNumber = mobileNumber.getText().toString();
        String getLocation = location.getText().toString();
        String getCat=catg.getText().toString();
        if (getFullName.equals("") || getFullName.length() == 0
                ||getpren.equals("") || getpren.length() == 0
                || getEmailId.equals("") || getEmailId.length() == 0
                || getMobileNumber.equals("") || getMobileNumber.length() == 0
                || getLocation.equals("") || getLocation.length() == 0
                || getCat.equals("") || getCat.length() == 0
        )

            Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();

            // Check if email id valid or not
        else {
            addArt.setMarque_p(getFullName);
            addArt.setNom_p(getpren);
            addArt.setPrix_P(Integer.parseInt(getEmailId));
            addArt.setDescription_p(getMobileNumber);
            addArt.setLibCat_p(getCat);
            addArt.setQuanSt_p(Integer.parseInt(getLocation));
            chatClientInterface.addproduit(addArt);
            Toast.makeText(AddProduit.this,"produit ajouter",Toast.LENGTH_LONG);
            finish();
        }

    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private ArrayList<String> listArticle;
        private RecyclerView mRecyclerView;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mImageView;
            public final ImageView supr;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.image_cartlist);
                supr = (ImageView) view.findViewById(R.id.suprimerImag);

            }
        }

        public SimpleStringRecyclerViewAdapter(RecyclerView recyclerView, ArrayList<String> Article) {
            listArticle = Article;
            mRecyclerView = recyclerView;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyout_imag, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onViewRecycled(ViewHolder holder) {

        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            Log.d("*************uri",listArticle.get(position).toString()+"position"+position);
            holder.mImageView.setImageURI(Uri.parse(listArticle.get(position)));
             holder.supr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRecyclerView.getAdapter().notifyItemRemoved(position);
                  listArticle.remove(position);


                }
            });

            /*holder.mLayoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ItemDetailsActivity.class);
                    intent.putExtra(STRING_IMAGE_URI,"http://192.168.1.3/"+listArticle.get(position).getImags_p().iterator().next());
                    intent.putExtra(STRING_IMAGE_POSITION, position);
                    intent.putExtra("Article",listArticle.get(position));
                    mContext.startActivity(intent);
                }
            });*/

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
         /*   holder.mLayoutEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });*/
        }

        @Override
        public int getItemCount() {
            return listArticle.size();
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        startActivityForResult(intent,PICK_IMAGE);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        final List<Bitmap> bitmaps = new ArrayList<>();
        ClipData clipData = data.getClipData();

        if (clipData != null) {
            //multiple images selecetd
            for (int i = 0; i < clipData.getItemCount(); i++) {
                Uri imageUri = clipData.getItemAt(i).getUri();
                listArt.add(imageUri.toString());


              addArt.getListImage().add(convertImageToByte(imageUri));
            }
        } else {
            //single image selected
            Uri imageUri = data.getData();
            listArt.add(imageUri.toString());

            SimpleDraweeView d=(SimpleDraweeView)findViewById(R.id.image1);
            d.setImageURI(imageUri);
            Log.d("URI", imageUri.toString());
            addArt.getListImage().add(convertImageToByte(imageUri));


        }
        Log.d("********************",listArt.size()+"");
        linearF.setVisibility(View.INVISIBLE);
        constraint.setVisibility(View.VISIBLE);
        adabter.notifyDataSetChanged();
    }

    public byte[] convertImageToByte(Uri uri){
        byte[] data = null;
        try {
            ContentResolver cr = getBaseContext().getContentResolver();
            InputStream inputStream = cr.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            data = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }
}
