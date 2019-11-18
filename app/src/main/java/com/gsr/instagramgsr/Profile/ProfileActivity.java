package com.gsr.instagramgsr.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.gsr.instagramgsr.R;
import com.gsr.instagramgsr.utils.BottomNavigationViewHelper;
import com.gsr.instagramgsr.utils.GridImageAdapter;
import com.gsr.instagramgsr.utils.UniversalImageLoader;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private Context mContext = ProfileActivity.this;
    private static final int ACTIVITY_NUM = 4;
    private static final int NUM_GRID_COLUMN = 3;

    ImageView menu, profilePhoto;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        init();


       /* profilePhoto = findViewById(R.id.profile_photo);
        progressBar = findViewById(R.id.progressBar);

        Log.d(TAG, "onCreate: ..started");
        setupBottomNavigation();

        menu = findViewById(R.id.img_menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, AccountSettingActivity.class));
            }
        });

        setupProfilePhoto();
        gridSetup();*/

    }


    private void init(){
        FragmentProfile fragment = new FragmentProfile();
        //this will add the fragments to frameLayout
        FragmentTransaction transaction = ProfileActivity.this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        //This will put all fragment on stack because fragments are not defaultly put on stack
        transaction.addToBackStack(getString(R.string.profile_fragment));
        transaction.commit();
    }

   /* private void gridSetup(){
        ArrayList<String> imgURLs = new ArrayList<>();
        imgURLs.add("https://thumbor.forbes.com/thumbor/960x0/https%3A%2F%2Fspecials-images.forbesimg.com%2Fimageserve%2F483691258%2F960x0.jpg%3Ffit%3Dscale");
        imgURLs.add("https://c.ndtvimg.com/2019-09/h9ihshcg_ranbir-kapoor-instagram_625x300_11_September_19.jpg");
        imgURLs.add("https://img.etimg.com/thumb/height-450,width-800,imgsize-59216,msid-49168643/.jpg");
        imgURLs.add("https://www.indiewire.com/wp-content/uploads/2019/05/Oncebradleoshutterstock_10242888de.jpg?w=780");
        imgURLs.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTifrUm7Tre4qN8vu41xoOD-SDnpHDRrn0u55EKG8-OeviK1eY&s");
        imgURLs.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRhwjDkkGHpT38ltUoKFo87APNQz9ERh_oB_NBUoQ5-HYtKvIwn&s");
        imgURLs.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS2HayWeDbq-TlDDbj7C1vJOmU6Cvk8W2VwZm1ZF2kndkmZ8ufi&s");
        imgURLs.add("https://i.pinimg.com/originals/66/ba/b1/66bab1a77417a6d60307dbfa0af6c5fd.jpg");
        imgURLs.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcROcjs17k2Sd97dBoqbAG35EUupe7xadJNHz0atpISQnn_CbjEy&s");
        imgURLs.add("https://www.dailyexcelsior.com/wp-content/uploads/2018/12/kader-759.jpg");
        imgURLs.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRhwjDkkGHpT38ltUoKFo87APNQz9ERh_oB_NBUoQ5-HYtKvIwn&s");
        imgURLs.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS2HayWeDbq-TlDDbj7C1vJOmU6Cvk8W2VwZm1ZF2kndkmZ8ufi&s");
        imgURLs.add("https://i.pinimg.com/originals/66/ba/b1/66bab1a77417a6d60307dbfa0af6c5fd.jpg");
        imgURLs.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcROcjs17k2Sd97dBoqbAG35EUupe7xadJNHz0atpISQnn_CbjEy&s");
        imgURLs.add("https://www.dailyexcelsior.com/wp-content/uploads/2018/12/kader-759.jpg");
        imgURLs.add("https://img.etimg.com/thumb/height-450,width-800,imgsize-59216,msid-49168643/.jpg");
        imgURLs.add("https://www.indiewire.com/wp-content/uploads/2019/05/Oncebradleoshutterstock_10242888de.jpg?w=780");
        imgURLs.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTifrUm7Tre4qN8vu41xoOD-SDnpHDRrn0u55EKG8-OeviK1eY&s");
        imgURLs.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRhwjDkkGHpT38ltUoKFo87APNQz9ERh_oB_NBUoQ5-HYtKvIwn&s");
        imgURLs.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS2HayWeDbq-TlDDbj7C1vJOmU6Cvk8W2VwZm1ZF2kndkmZ8ufi&s");
        imgURLs.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRhwjDkkGHpT38ltUoKFo87APNQz9ERh_oB_NBUoQ5-HYtKvIwn&s");
        imgURLs.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS2HayWeDbq-TlDDbj7C1vJOmU6Cvk8W2VwZm1ZF2kndkmZ8ufi&s");
        imgURLs.add("https://i.pinimg.com/originals/66/ba/b1/66bab1a77417a6d60307dbfa0af6c5fd.jpg");
        setupImageGrid(imgURLs);
    }

    private void setupImageGrid(ArrayList<String> imageURLs){
        GridView gridView = findViewById(R.id.profile_gridView);

        //This will make all image in grid in square shape
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_GRID_COLUMN;
        gridView.setColumnWidth(imageWidth);


        GridImageAdapter adapter = new GridImageAdapter(mContext, R.layout.layout_grid_imageview, "", imageURLs);
        gridView.setAdapter(adapter);
    }

    private void setupProfilePhoto(){
        Log.d(TAG, "setupProfilePhoto: setting profile photo");
        String imageURL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSVIge0crcD4hTzaTALTeaxjQP0HTrAXsBP0xlb0OSv3NmX_g8R&s";
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        universalImageLoader.setImage(imageURL, profilePhoto, progressBar, "");
    }


    private void setupBottomNavigation(){
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigationViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }*/
}
