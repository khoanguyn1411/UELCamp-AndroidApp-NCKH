package nguyenhoanganhkhoa.com.myapplication.home.transfer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import nguyenhoanganhkhoa.com.adapter.FriendAdapter;
import nguyenhoanganhkhoa.com.myapplication.R;
import nguyenhoanganhkhoa.com.thirdlink.ReusedConstraint;

public class TransferMainScreen extends AppCompatActivity {
    ImageView imvBack;
    ReusedConstraint reusedConstraint = new ReusedConstraint(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_main_screen);
        linkView();
        addFragment();
        addEvents();

    }

    private void linkView() {
        imvBack = findViewById(R.id.imvBackTopUpMain);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        if(drawerLayout.isDrawerVisible(GravityCompat.END)){
            drawerLayout.closeDrawer(GravityCompat.END);
        }
        else{
            if(getFragmentManager().getBackStackEntryCount()>0) {
                getFragmentManager().popBackStack();
            }
            else TransferMainScreen.super.onBackPressed();
        }
    }

    private void addEvents() {
        reusedConstraint.openNav(this);
        imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getFragmentManager().getBackStackEntryCount()>0) {
                    getFragmentManager().popBackStack();
                }
                else TransferMainScreen.super.onBackPressed();
            }
        });
    }

    private void addFragment() {
        FragmentManager fragmentManager;
        fragmentManager = getSupportFragmentManager();
        if(FriendAdapter.screenToChangeFragment == FriendAdapter.toFrag2){
            try {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                TransferMoneyFragment fragment = new TransferMoneyFragment();

                fragmentTransaction.replace(R.id.lnFragmentReplaceTransfer, fragment);
                fragmentTransaction.commit();

            }
            catch (Exception e) {
                Log.d("Error", "Fail to addFragment in TopUpScreen: " + e);
            }
        }
        else{
            try {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                TransferHomeFragment transferHomeFragment = new TransferHomeFragment();

                fragmentTransaction.replace(R.id.lnFragmentReplaceTransfer, transferHomeFragment);
                fragmentTransaction.commit();
            }
            catch (Exception e) {
                Log.d("Error", "Fail to addFragment in TopUpScreen: " + e);
            }
        }



    }


}