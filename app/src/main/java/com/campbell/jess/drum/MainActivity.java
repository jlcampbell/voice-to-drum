package com.campbell.jess.drum;

import android.content.ActivityNotFoundException;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView mTextView;
    Button mButton;
    Button mDummy;
    Button mParse;
    Button mSubmit;
    EditText mEditText;

    public SoundPoolPlayer soundPoolPlayer;

    private String dummyText = "boom boom pop pop";

    private String[] mWords;

    private static final String TAG_MAIN = "main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mTextView = (TextView) findViewById(R.id.tv_speech);
        mButton = (Button) findViewById(R.id.btn_mic);
        mParse = (Button) findViewById(R.id.btn_parse_spoken_words);
        mEditText = (EditText) findViewById(R.id.edit_text_string);
        mSubmit = (Button) findViewById(R.id.btn_submit_text);

        soundPoolPlayer = new SoundPoolPlayer();
        soundPoolPlayer.StartSoundPoolPlayer(this);

        mButton.setOnClickListener(this);

        mParse.setOnClickListener(this);

        mSubmit.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_mic:
                Log.v(TAG_MAIN, "voiceMemo");
                btnResponse();
                break;

            case R.id.btn_parse_spoken_words:
                Log.v(TAG_MAIN, "play word drums now");
                new SoundPoolAsyncTask().execute(mWords);
                break;

            case R.id.btn_submit_text:
                Log.v(TAG_MAIN, "submit");
                String userSubText = (String) mEditText.getText().toString();
                mEditText.setText(userSubText);
                mWords = splitWords(userSubText);
                new SoundPoolAsyncTask().execute(mWords);
        }
    }
    public void onStart(){
        super.onStart();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    private void btnResponse() {
        displaySpeechRecognizer();
        mParse.setVisibility(View.VISIBLE);

    }
    public void playKick() {
        soundPoolPlayer.playSound("kick", 0.5f);
    }







    ///speech recognizer methods
    private static final int SPEECH_REQUEST_CODE = 0;

    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
// try to Start the activity, the intent will be populated with the speech text
        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE);
        } catch (ActivityNotFoundException a){
            Toast.makeText(getApplicationContext(),
                    "speech not supported",
                    Toast.LENGTH_SHORT).show();
        }
    }
    // This callback is invoked when the Speech Recognizer returns.
// This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);

            String spokenText = results.get(0);

            //do something with results here
            String[] words = splitWords(spokenText);
            mWords = words;
            displayText(spokenText);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String[] splitWords(String text){
        String s1 = text;
        String[] words=s1.split("\\s");
        return words;
    }

    private void displayText(String text){
        mTextView.setText(text);
    }





    ////async task for sequencing drum sounds
    public class SoundPoolAsyncTask extends AsyncTask<String[], String, String>
    {
        public String TAG_ASYNC = "SoundPoolAsyncTask";
        @Override
        protected void onProgressUpdate(String... values) {}

        @Override
        protected String doInBackground(String[]... params) {
            try {
                Log.v(TAG_ASYNC, "asyncing now");
                //For each SoundPool object
                // int soundLength = however long the SoundPool object is
                // Play the sound
                // Thread.sleep(soundLength);
                for (String s: params[0]){
                    Log.v(TAG_ASYNC, s);
                    switch (s){
                        case "Boom" :
                            soundPoolPlayer.playSound("kick", 2f);
                            Thread.sleep(1000);
                            break;
                        case "boom" :
                            soundPoolPlayer.playSound("kick", 2f);
                            Thread.sleep(1000);
                            break;
                        case "Pop":
                            soundPoolPlayer.playSound("snare", 2f);
                            Thread.sleep(1000);
                            break;
                        case "pop":
                            soundPoolPlayer.playSound("snare", 2f);
                            Thread.sleep(1000);
                            break;
                        case "else":
                            continue;

                    }

                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //Clean up your class objects
        }
    }


}
