package de.trier.hs.inf.tipp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SCORE_PER_TAP = 4;
    private static final int WIN_SCORE = 100;
    private static double currBest = Double.MAX_VALUE;

    private Button btnStartGame;
    private Button btnPlayerOne;
    private Button btnPlayerOneTimeOut;


    private EditText etPlayerOne;
    private TextView txtPlayerOneTimeOut;
    private ProgressBar barPlayerOne;

    private int playerOneScore;

    private Button btnPlayerTwo;
    private Button btnPlayerTwoTimeOut;

    private EditText etPlayerTwo;
    private TextView txtPlayerTwoTimeOut;
    private ProgressBar barPlayerTwo;

    private int playerTwoScore;

    private long startTime;
    private int Timeoutmil = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        btnStartGame = findViewById(R.id.btnStartGame);
        btnPlayerOne = findViewById(R.id.btnPlayerOne);
        btnPlayerOneTimeOut = findViewById(R.id.btnPlayerOneTimeOut);

        etPlayerOne = findViewById(R.id.etPlayerOne);
        barPlayerOne = findViewById(R.id.barPlayerOne);
        txtPlayerOneTimeOut =findViewById(R.id.txtPlayerOneTimeOut);

        btnPlayerOne.setEnabled(false);
        btnPlayerOneTimeOut.setEnabled(false);
        btnPlayerOne.setOnClickListener(this);
        btnPlayerOneTimeOut.setOnClickListener(this);


        btnPlayerTwo = findViewById(R.id.btnPlayerTwo);
        btnPlayerTwoTimeOut = findViewById(R.id.btnPlayerTwoTimeOut);

        etPlayerTwo = findViewById(R.id.etPlayerTwo);
        barPlayerTwo = findViewById(R.id.barPlayerTwo);
        txtPlayerTwoTimeOut =findViewById(R.id.txtPlayerTwoTimeOut);

        btnPlayerTwo.setEnabled(false);
        btnPlayerTwoTimeOut.setEnabled(false);
        btnPlayerTwo.setOnClickListener(this);
        btnPlayerTwoTimeOut.setOnClickListener(this);
        btnStartGame.setOnClickListener(e -> onClickStartGame(e));
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        currBest = preferences.getFloat("CURR_BEST",Float.MAX_VALUE);


    }

    private void onClickStartGame(View v) {

        btnPlayerOne.setEnabled(true);
        btnPlayerTwo.setEnabled(true);
        btnPlayerOneTimeOut.setEnabled(false);
        btnPlayerTwoTimeOut.setEnabled(false);
        btnStartGame.setVisibility(View.INVISIBLE);
        etPlayerTwo.setEnabled(false);
        etPlayerOne.setEnabled(false);
        startTimer();
        Toast.makeText(this, "Lasset die Spiele beginnen!", Toast.LENGTH_SHORT).show();

    }

    private void startTimer() {
        startTime = System.currentTimeMillis();

    }

    private double stopTimer() {
        long time = System.currentTimeMillis() - startTime;
        if (currBest > time) {
            currBest = time;
        }
        return time / 1000.0;
    }

    private void GameOver() {
        String winMessage = "";
        if (playerOneScore>playerTwoScore){
            winMessage = etPlayerOne.getText() + " has won the match!" ;
        }
        if (playerOneScore<playerTwoScore){
            winMessage = etPlayerTwo.getText() + " has won the match!" ;
        }
        if (playerTwoScore==playerOneScore){
            winMessage = "Its a draw! Play Again!";
        }
        String message1 = etPlayerOne.getText() + " has " + playerOneScore + " points!";
        String message2 = etPlayerTwo.getText() + " has " + playerTwoScore + " points!";
        String message3 = "Time needed: " + stopTimer() + "s";

        String message4 = "High Score: " + (currBest / 1000.0) + "s";


        btnPlayerOne.setEnabled(false);
        etPlayerOne.setEnabled(true);
        btnPlayerTwo.setEnabled(false);
        etPlayerTwo.setEnabled(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(winMessage + "\n"+message1 + "\n" + message2 + "\n" + message3 + "\n" + message4);
        builder.setPositiveButton("Play Again!", (dialog, id) -> startGame());
        builder.setNegativeButton("Stop Playing!", (dialog, id) -> finish());
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void startGame() {
        btnPlayerOne.setEnabled(false);
        btnPlayerOneTimeOut.setVisibility(View.VISIBLE);
        btnPlayerOneTimeOut.setEnabled(false);
        btnPlayerTwo.setEnabled(false);
        btnPlayerTwoTimeOut.setVisibility(View.VISIBLE);
        btnPlayerTwoTimeOut.setEnabled(false);
        btnStartGame.setVisibility(View.VISIBLE);

        playerOneScore = 0;
        barPlayerOne.setProgress(0);

        playerTwoScore = 0;
        barPlayerTwo.setProgress(0);

    }

    protected void onDestroy() {
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("CURR_BEST",(float) currBest);
        editor.apply();
        super.onDestroy();
        Toast.makeText(this, "Spiel beendet!\nBis bald!", Toast.LENGTH_SHORT).show();
    }

    protected void onResume() {
        super.onResume();
        startGame();
    }
    public void Timeout(int player){
        if(player == 1){
            btnPlayerTwo.setEnabled(false);
            new CountDownTimer(Timeoutmil, 1) {
                @Override
                public void onTick(long l) {
                    txtPlayerTwoTimeOut.setText(""+l/1000.0+"s");

                }

                @Override
                public void onFinish() {
                    btnPlayerTwo.setEnabled(true);
                    txtPlayerTwoTimeOut.setText("");
                }
            }.start();
            }

        if (player == 2){
            btnPlayerOne.setEnabled(false);
            new CountDownTimer(Timeoutmil, 1) {
                @Override
                public void onTick(long l) {
                    txtPlayerOneTimeOut.setText(""+l/1000.0+"s");
                }

                @Override
                public void onFinish() {
                    btnPlayerOne.setEnabled(true);
                    txtPlayerOneTimeOut.setText("");
                }
            }.start();
        }
        }
    @Override
    public void onClick(View view) {
        if (view == btnPlayerOne) {
            if(playerOneScore >= 75){
                btnPlayerTwoTimeOut.setEnabled(true);
            }
            if(playerTwoScore<4 ) {
                playerOneScore += SCORE_PER_TAP;
                barPlayerOne.setProgress(playerOneScore);

                if (playerOneScore >= WIN_SCORE) {
                    GameOver();
                }
            }
            else{
                playerTwoScore -= SCORE_PER_TAP;
                barPlayerTwo.setProgress(playerTwoScore);
                if ( playerTwoScore < 75) {
                    btnPlayerOneTimeOut.setEnabled(false);
                }
            }
        }
        if (view == btnPlayerTwo) {
            if(playerTwoScore >= 75){
                btnPlayerOneTimeOut.setEnabled(true);
            }
            if(playerOneScore < 4) {
                playerTwoScore += SCORE_PER_TAP;
                barPlayerTwo.setProgress(playerTwoScore);

                if (playerTwoScore >= WIN_SCORE) {
                    GameOver();
                }
            }
            else{
                playerOneScore += -SCORE_PER_TAP;
                barPlayerOne.setProgress(playerOneScore);
                if ( playerOneScore < 75){
                    btnPlayerTwoTimeOut.setEnabled(false);
                }
            }
        }
        if (view== btnPlayerOneTimeOut && playerTwoScore >= 75){
            btnPlayerOneTimeOut.setVisibility(view.INVISIBLE);
            Timeout(1);
        }
        if (view== btnPlayerTwoTimeOut && playerOneScore >= 75){
            btnPlayerTwoTimeOut.setVisibility(view.INVISIBLE);
            Timeout(2);
        }

    }
}