package maniac.jumbledwordsgame;

import android.app.assist.AssistContent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Setup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d("msg", "setup started");

        setContentView(R.layout.activity_setup);
    try {
    setupTries();
        }
    catch(Exception e)
    {}
    }

    protected void setupTries()throws IOException{

         final Trie trie =((Trie)getApplicationContext());

        trie.initializeTrie();

        Intent intent=new Intent(this,Game.class);

        Log.d("msg","setup done");
        startActivity(intent);


    }
}
