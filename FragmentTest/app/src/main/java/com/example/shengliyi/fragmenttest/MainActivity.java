package com.example.shengliyi.fragmenttest;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button leftButton = null;
    int count = 0;
    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment = new RightFragment();
        final FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .add(R.id.right_fragment,fragment)
                .commit();

        leftButton = (Button)findViewById(R.id.left_button);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count%2 == 0){
                    fragment = new RightAnotherFragment();
                    count++;
                }else{
                    fragment = new RightFragment();
                    count++;
                }
                fm.beginTransaction()
                        .replace(R.id.right_fragment,fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}
