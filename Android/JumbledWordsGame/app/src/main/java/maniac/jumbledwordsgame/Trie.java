package maniac.jumbledwordsgame;

import android.app.Application;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by SAWANT on 3/7/2016.
 */
public class Trie extends Application{

   private Node root;

    public void initializeTrie()throws IOException
    {
        root=new Node();

        InputStream isr=getAssets().open("words.txt") ;
        BufferedReader br=new BufferedReader(new InputStreamReader(isr));


        String next;
        while(( next=br.readLine())!=null)
        { //Log.d("msg",next);
           root.insert(next);



        }
        Log.d("msg", "trie done");
    }

    public int check(String key)
    {
        return root.check(key);


    }


}

class Node{

    char key;
    char isWord;  //'y' if word ends here else 'n'
    Node children[];

    Node()          //root node initialization
    {
        this.key='/';
        this.isWord='n';
        children=new Node[26];
    }

    Node(char s)         //char node initialization
    {
        this.key=s;
        this.isWord='n';
        children=new Node[26];

    }

    protected void insert(String key)
    {
        int charindex=key.charAt(0)-'a';
      //Log.d("msg",String.valueOf(key.charAt(0)));
        if(this.children[charindex]==null)
            this.createNode(charindex);

        if(key.length()==1)
            this.children[charindex].isWord='y';
        else
            this.children[charindex].insert(key.substring(1));

        return;
    }

    private void createNode(int charindex)
    {
        this.children[charindex]=new Node((char)('a'+charindex));



    }

    protected int check(String key)
    {

        int charindex=key.charAt(0)-'a';


    //  Log.d("msg",String.valueOf(key.charAt(0)));

        if(this.children[charindex]==null)
            return 3;                         //this key does not exist

        if(key.length()==1) {
            if (this.children[charindex].isWord == 'n')
                return 1;                 //this key does not exist as a WORD
            else return 2;                  //this key exists as a WORD
        }

       //  Log.d("msg",String.valueOf(this.children[charindex].isWord));
        return this.children[charindex].check(key.substring(1));

    }

}