package ru.geekbrains.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import ru.geekbrains.notes.R;
import ru.geekbrains.notes.ui.AboutFragment;
import ru.geekbrains.notes.ui.DialogFragmentExit;
import ru.geekbrains.notes.ui.SocialNetworkFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, SocialNetworkFragment.newInstance()).commit();
        }
        setSupportActionBar(findViewById(R.id.toolBar));
    }



}