package ru.geekbrains.notes.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ru.geekbrains.notes.R;
import ru.geekbrains.notes.observe.Publisher;
import ru.geekbrains.notes.ui.Navigation;
import ru.geekbrains.notes.ui.main.SocialNetworkFragment;

public class MainActivity extends AppCompatActivity {
    private Publisher publisher;
    private Navigation navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        publisher = new Publisher();
        navigation = new Navigation(getSupportFragmentManager());
        if (savedInstanceState == null){
            navigation.replaceFragment(SocialNetworkFragment.newInstance(),false);
        }
        setSupportActionBar(findViewById(R.id.toolBar));
    }

    public Publisher getPublisher(){
        return publisher;
    }

    public Navigation getNavigation(){
        return navigation;
    }

}