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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.toolbar_about:
                   getSupportFragmentManager().beginTransaction().addToBackStack("").add(R.id.container, new AboutFragment()).commit();
               return true;
            case R.id.toolbar_exit:
                new DialogFragmentExit().show(getSupportFragmentManager(), DialogFragmentExit.TAG);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}