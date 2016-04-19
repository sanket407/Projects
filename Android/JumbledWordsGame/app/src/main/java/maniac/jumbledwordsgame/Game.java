package maniac.jumbledwordsgame;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class Game extends Activity {

    private int n,duplicate[],n_words,answered[];  //duplicate used to prevent regeneration of same word from repeating chars in q
    private boolean visited[];
    private char q[];
    private ArrayList<String> words[];
   private  Trie trie=null;
    private HashSet<String> toBeAnsweredSet;
    private HashMap<String,Integer> answeredSet;
    private  TextView text[][][];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_game);




            startNewRound();
    }

    private void startNewRound()
    {  n_words=0;
        Random r= new Random();

         n = r.nextInt(5)+4;      //generates rand no. between 4 and 9
         q=new char[n];

        for(int i=0;i<n;i++)
            q[i]=(char)('a' +r.nextInt(25));

         visited=new boolean[n];
        duplicate=new int[n];    //store 1st occurence of char 'a'+i

        Arrays.fill(visited, false);
        answered=new int[10];
        String qs=String.valueOf(q);
        toBeAnsweredSet=new HashSet<String>();
        answeredSet=new HashMap<String,Integer>();
        for(int i=0;i<n;i++)
        {
            duplicate[i]=qs.indexOf(q[i]);


        }

        StringBuilder sb=new StringBuilder("");

        trie =((Trie)getApplicationContext());

         words=new ArrayList[10];

        for(int i=3;i<=9;i++)
            words[i]=new ArrayList<String>();


        findAllPossibleWords(sb);


        if(n_words<4 || n_words>8)
        {
            ((ImageButton)findViewById(R.id.restart)).performClick();


        }
       else
        {
        TextView tv = (TextView)findViewById(R.id.ques);
        tv.setText(q,0,n);

            text = new TextView[10][8][9];

            LinearLayout vertical = (LinearLayout)findViewById(R.id.vertical);
            LinearLayout.LayoutParams vp = (new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            vp.setMargins(10,15,10,15);

            for(int i=3;i<=9;i++)
        { if(words[i].size()<1)
            continue;

         int blocks=words[i].size();

         for(int j=0;j<blocks;j++)
         {
             LinearLayout horizontal= new LinearLayout(this);

             vertical.addView(horizontal,vp);
             horizontal.setOrientation(LinearLayout.HORIZONTAL);

             horizontal.setBackgroundColor(Color.parseColor("#005580"));


             for(int k=0;k<i;k++) {

                 text[i][j][k] = new TextView(this);

                 LinearLayout.LayoutParams lp = (new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                 text[i][j][k].setText("");
                 text[i][j][k].setTextSize(15);

                 text[i][j][k].setBackgroundResource(R.drawable.gradient);

                 if(k==0 )
                     lp.setMargins(6,6,3,6);
                 else if(k==i-1)
                     lp.setMargins(3,6,6,6);
                 else
                 lp.setMargins(3, 6 , 3, 6);


                 text[i][j][k].setLayoutParams(lp);
                 text[i][j][k].setWidth(90);
                 text[i][j][k].setPadding(2, 2, 2, 2);
                 text[i][j][k].setGravity(Gravity.CENTER);
                 text[i][j][k].setTypeface(null, Typeface.BOLD);
                 horizontal.addView(text[i][j][k],lp);

             }
         }



        }

    }}

    private void findAllPossibleWords(StringBuilder word)
    {

        for(int i=0;i<n;i++)
        {


            if(duplicate[i]!=i)     //this is 2nd occurence
               if(!visited[duplicate[i]])    //if 1st occurence not present in word
               {
                continue;}

            if(visited[i])
            {
             continue;}           //if this char is already included in formed word

          String key=(word.append(q[i])).toString();     //add next char to SB and convert to String

            if(trie.check(key)==3)      //this key does not exist in trie
            {
                word=new StringBuilder(key.substring(0,key.length()-1));    //remove last char from SB
                {
                 continue;}
            }
            if (trie.check(key)==2)        //this key exists as A WORD
            {
                if(!toBeAnsweredSet.contains(key)) {
                    words[key.length()].add(key);    //ADD word to LIST
                    n_words++;

                    toBeAnsweredSet.add(key);
                }
            }
            visited[i]=true;
            findAllPossibleWords(word);      //find further possible words with suffix as this WORD

            visited[i]=false;
           word=new StringBuilder(key.substring(0,key.length()-1));

        }





    }

    public void check(View view)
    {

        String key=((EditText)findViewById(R.id.edit_message)).getText().toString();


        TextView status=(TextView)findViewById(R.id.status);
        if(toBeAnsweredSet.contains(key))
        {   Log.d("msg","success");




            status.setText("success");
            ((EditText)findViewById(R.id.edit_message)).setText("");
            answeredSet.put(key,answered[key.length()]);
            toBeAnsweredSet.remove(key);
            for(int i=0;i<key.length();i++)

                text[key.length()][answered[key.length()]][i].setText(String.valueOf(key.charAt(i)));

             answered[key.length()]++;}



else
        if(answeredSet.containsKey(key))
        {
             final int wordindex = answeredSet.get(key);

            for(int i=0;i<key.length();i++)
            {

                text[key.length()][wordindex][i].setBackgroundResource(R.drawable.gradient1);

            }

           final int len=key.length();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    for(int i=0;i<len;i++)
                    {

                        text[len][wordindex][i].setBackgroundResource(R.drawable.gradient);

                    }
                }
            }, 2000);

        }
        else

        {   status.setText("fail");
            ((EditText)findViewById(R.id.edit_message)).setText("");



    }
       hideKeyBoard();

        if(toBeAnsweredSet.isEmpty())
            ((ImageButton)findViewById(R.id.help)).performClick();

}

    public void help(View view)
    {

        Iterator <String> itr = toBeAnsweredSet.iterator();

        while(itr.hasNext())
        {
            String next = itr.next();

            for(int i=0;i<next.length();i++)
            {
                text[next.length()][answered[next.length()]][i].setText(String.valueOf(next.charAt(i)));




            }

            answered[next.length()]++;

        }

        ((RelativeLayout)findViewById(R.id.relative)).removeView(findViewById(R.id.edit_message));
        ((RelativeLayout)findViewById(R.id.relative)).removeView(findViewById(R.id.button));
        ((RelativeLayout)findViewById(R.id.relative)).removeView(findViewById(R.id.help));



    }


    private void resetStatus()
    {


        TextView status=(TextView)findViewById(R.id.status);

        status.setText("");





    }

    public void restartRound(View view)
    {
        Intent intent=getIntent();
        finish();
        startActivity(intent);


    }

    private void hideKeyBoard()
    {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                resetStatus();
            }
        }, 2000);


        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

    }

}
