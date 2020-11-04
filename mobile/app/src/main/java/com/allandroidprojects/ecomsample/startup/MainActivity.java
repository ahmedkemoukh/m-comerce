package com.allandroidprojects.ecomsample.startup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.allandroidprojects.ecomsample.Mcommerce.Markeplace;
import com.allandroidprojects.ecomsample.R;
import com.allandroidprojects.ecomsample.agent.ChatClientAgent;
import com.allandroidprojects.ecomsample.agent.ChatClientInterface;
import com.allandroidprojects.ecomsample.fragments.ImageListFragment;
import com.allandroidprojects.ecomsample.miscellaneous.EmptyActivity;
import com.allandroidprojects.ecomsample.notification.NotificationCountSetClass;
import com.allandroidprojects.ecomsample.options.CartListActivity;
import com.allandroidprojects.ecomsample.options.SearchResultActivity;
import com.allandroidprojects.ecomsample.product.AddProduit;
import com.allandroidprojects.ecomsample.product.AddVendeur;
import com.allandroidprojects.ecomsample.product.List_Achat;
import com.allandroidprojects.ecomsample.product.List_Vend;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import jade.core.MicroRuntime;
import jade.util.Logger;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static int notificationCountCart = 0;
    static ViewPager viewPager;
    static TabLayout tabLayout;
    private MyReceiver myReceiver;
    private ChatClientInterface chatClientInterface;
    private String nickname;
    public static int curentype=0;

    private Logger logger = Logger.getJADELogger(this.getClass().getName());
    ArrayList<ImageListFragment> listFrag=new ArrayList<ImageListFragment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nickname = extras.getString("nickname");
        }
        try {
            chatClientInterface = MicroRuntime.getAgent(nickname)
                    .getO2AInterface(ChatClientInterface.class);
        } catch (StaleProxyException e) {
            logger.log(Level.INFO, "Received intent************************************ " + 1);
        } catch (ControllerException e) {
            logger.log(Level.INFO, "Received intent************************************ " + 2);
        }
        logger.log(Level.INFO, "Received intent************************************ "+ 3 );

        myReceiver = new MyReceiver();

        IntentFilter killFilter = new IntentFilter();
        killFilter.addAction("ListArticle");

       // chatClientInterface.DemandArticle("ssss");
        registerReceiver(myReceiver, killFilter);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu=navigationView.getMenu();
      if(ChatClientAgent.monAchteur.getType()==1) {
          MenuItem item1 = menu.findItem(R.id.addMarketing);
          item1.setVisible(false);
      }
      else
      {
          MenuItem item1 = menu.findItem(R.id.My_Articlet);
          item1.setVisible(false);
           item1 = menu.findItem(R.id.vent);
          item1.setVisible(false);
          item1 = menu.findItem(R.id.addArticle);
          item1.setVisible(false);
      }

         viewPager = (ViewPager) findViewById(R.id.viewpager);
         tabLayout = (TabLayout) findViewById(R.id.tabs);

        if (viewPager != null) {
            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
        }


      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(MainActivity.this,"resum",Toast.LENGTH_SHORT).show();
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Get the notifications MenuItem and
        // its LayerDrawable (layer-list)
        MenuItem item = menu.findItem(R.id.action_cart);
        NotificationCountSetClass.setAddToCart(MainActivity.this, item,notificationCountCart);
        // force the ActionBar to relayout its MenuItems.
     //   onCreateOptionsMenu(menu);
        invalidateOptionsMenu();


        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if(id==null){
            startActivity(new Intent(MainActivity.this, AddProduit.class));

        }else*/ if (id == R.id.action_search) {
            startActivity(new Intent(MainActivity.this, SearchResultActivity.class));
            return true;
        } else if (id == R.id.action_cart) {

           /* NotificationCountSetClass.setAddToCart(MainActivity.this, item, notificationCount);
            invalidateOptionsMenu();*/
            startActivity(new Intent(MainActivity.this, CartListActivity.class));

           /* notificationCount=0;//clear notification count
            invalidateOptionsMenu();*/
            return true;
        }else {
            startActivity(new Intent(MainActivity.this, EmptyActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        ImageListFragment fragment = new ImageListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        Toast.makeText(this,"1",Toast.LENGTH_SHORT).show();
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.item_1));

        listFrag.add(fragment);
        fragment = new ImageListFragment();

        bundle = new Bundle();
        bundle.putInt("type", 2);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.item_2));

        listFrag.add(fragment);
        fragment = new ImageListFragment();

        bundle = new Bundle();
        bundle.putInt("type", 3);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.item_3));

        listFrag.add(fragment);
        fragment = new ImageListFragment();
        bundle = new Bundle();
        bundle.putInt("type", 4);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.item_4));

        listFrag.add(fragment);
        fragment = new ImageListFragment();
        bundle = new Bundle();
        bundle.putInt("type", 5);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.item_5));
        listFrag.add(fragment);
        fragment = new ImageListFragment();

        bundle = new Bundle();
        bundle.putInt("type", 6);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, getString(R.string.item_6));
        viewPager.setAdapter(adapter);
        listFrag.add(fragment);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_item1) {
            viewPager.setCurrentItem(0);
        } else if (id == R.id.nav_item2) {
            viewPager.setCurrentItem(1);
        } else if (id == R.id.nav_item3) {
            viewPager.setCurrentItem(2);
        } else if (id == R.id.nav_item4) {
            viewPager.setCurrentItem(3);
        } else if (id == R.id.nav_item5) {
            viewPager.setCurrentItem(4);
        }else if (id == R.id.nav_item6) {
            viewPager.setCurrentItem(5);

        }else if (id == R.id.my_Home){
            curentype=0;
            ImageListFragment.electronique.clear();
            chatClientInterface.DemandArticle(getString(R.string.item_1),MainActivity.curentype);
        }else if (id == R.id.my_cart){
            startActivity(new Intent(MainActivity.this, CartListActivity.class));
        }

            else if (id == R.id.addMarketing) {
          //  startActivity(new Intent(MainActivity.this, WishlistActivity.class));
            startActivity(new Intent(MainActivity.this, AddVendeur.class));
        }else if(id==R.id.My_Articlet) {
curentype=1;
ImageListFragment.electronique.clear();
            chatClientInterface.DemandArticle(getString(R.string.item_1),MainActivity.curentype);
        }else if(id==R.id.vent){
            startActivity(new Intent(MainActivity.this, List_Vend.class));
        }
        else if(id==R.id.my_demande){  startActivity(new Intent(MainActivity.this, List_Achat.class));}
        else if(id==R.id.addArticle){
            startActivity(new Intent(MainActivity.this, AddProduit.class));
        }

        else{startActivity(new Intent(MainActivity.this, EmptyActivity.class));}

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            logger.log(Level.INFO, "Received intent " + action);

            if (action.equalsIgnoreCase("ListArticle")) {

                Markeplace A = (Markeplace) intent.getSerializableExtra("ListArt");
                logger.log(Level.INFO, "Received intent************************************ " + A.getType());
                if (A.getType().equals(getString(R.string.item_1)))
                {
//                    logger.log(Level.INFO, "http://192.168.1.6/"+A.getList().get(0).getImags_p().iterator().next());

                    String[] items = new String[]{
                        "http://192.168.1.6/c1.jpg",
                        "http://192.168.1.6/c2.jpg",
                        "http://192.168.1.6/c3.jpg",
                        "http://192.168.1.6/c4.jpg",
                        "http://192.168.1.6/c5.jpg",
                        "http://192.168.1.6/c6.jpg",
                        "http://192.168.1.6/c7.jpg",
                        "http://192.168.1.6/c8.jpg",
                        "http://192.168.1.6/c8.jpg",

                            "http://192.168.1.6/a.jpg"
                };

                  ImageListFragment.electronique.add(A);


                    listFrag.get(0).rv.getAdapter().notifyDataSetChanged();
                    /*items[1] = "http://192.168.1.6/a.jpg";
                    a.notifyDataSetChanged();*/
                }
                else
                {
                    if (A.getType().equals(getString(R.string.item_2)))
                    {
                        ImageListFragment.informatique.add(A);
                        listFrag.get(1).rv.getAdapter().notifyDataSetChanged();
                    }
                    else
                    {
                        if (A.getType().equals(getString(R.string.item_3)))
                        {
                            ImageListFragment.mobile.add(A);
                            listFrag.get(2).rv.getAdapter().notifyDataSetChanged();
                        }

                        else
                        {
                            if (A.getType().equals(getString(R.string.item_4)))
                            {
                                ImageListFragment.chaussure.add(A);
                                listFrag.get(3).rv.getAdapter().notifyDataSetChanged();
                            }
                        }
                    }

                }
                }
            }
        }
    }

