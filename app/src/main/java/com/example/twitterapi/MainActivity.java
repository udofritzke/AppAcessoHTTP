package com.example.twitterapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  {
    private static final String TAG = "MainActivity";

    private Button mBotaoBusca;
    private TextView mTextViewTweet;
    String tweetKobra = "1528527807328014337";
    String tweetSiqueira = "1458467063207497731";

    private class TarefaBuscaTweets extends AsyncTask<Void, Void, DadosTweet> {

        @Override
        protected DadosTweet doInBackground (Void... params){
            DadosTweet  dadosTweet;
            dadosTweet = new TwitterAPIAcess().buscaTweet(tweetKobra);
            Log.i(TAG, "doInBackground: "+ dadosTweet.getText());

            return dadosTweet;
        }

        protected void onPostExecute(DadosTweet result) {
            Log.i(TAG, "onPostExecute: metodo executado");
            if (result != null){
                Log.i(TAG, "onPostExecute: tweet recebido: " + result.getText());
                mTextViewTweet = (TextView) findViewById(R.id.view_texto_do_tweet);
                mTextViewTweet.setText(result.getText());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBotaoBusca = (Button) findViewById(R.id.botaoBusca);
        mBotaoBusca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask<Void,Void,DadosTweet> tar = new TarefaBuscaTweets();
                tar.execute();
            }
        });
    }
}