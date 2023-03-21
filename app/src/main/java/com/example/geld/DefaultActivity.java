package com.example.geld;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


public class DefaultActivity extends AppCompatActivity {

    int i= 0;
    int j = 0;

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String KEY_GELD = "geld";
//tictactoe

    private static final String PLAYER_0 = "O";
    private static final String PLAYER_X = "X";
    String currentPlayer = PLAYER_X;
    Button[][] buttons = new Button[3][3];

    TextView gewinnText;

    private TextView myTextView;
    private int geld;

    //geht auf die erste seite, wenn der zurück knopf gedrückt wird
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, DefaultActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        increaseMoneyBy50EveryMonth();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //konstruktor
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainfront);


        //stocks
        AsyncTask<String, Void, String> preis = new stockAPI().execute("NVS");
        TextView myPreis = findViewById(R.id.textPreisNVS);
        try {
            String p = preis.get();
            p = p.substring(0,5);
            myPreis.setText(p);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        //stocks
        AsyncTask<String, Void, String> preis1 = new stockAPI().execute("BP");
        TextView myPreis1 = findViewById(R.id.textPreisBP);
        try {
            String p = preis1.get();
            p = p.substring(0,5);
            myPreis1.setText(p);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }


        //Button to get to the tictactoe window
        Button buttontic = findViewById(R.id.tictac);
        buttontic.setText("TicTacToe");
        buttontic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setContentView(R.layout.tictactoe);
                gewinnText = findViewById(R.id.gewinner);
                buttons[0][0] = findViewById(R.id.btn1);
                buttons[0][1] = findViewById(R.id.btn2);
                buttons[0][2] = findViewById(R.id.btn3);
                buttons[1][0] = findViewById(R.id.btn4);
                buttons[1][1] = findViewById(R.id.btn5);
                buttons[1][2] = findViewById(R.id.btn6);
                buttons[2][0] = findViewById(R.id.btn7);
                buttons[2][1] = findViewById(R.id.btn8);
                buttons[2][2] = findViewById(R.id.btn9);




            }
        });

        //geld anzeigen
        myTextView = findViewById(R.id.geld);

        SharedPreferences sharedPrefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        geld = sharedPrefs.getInt(KEY_GELD, -456);

        myTextView.setText(String.valueOf(geld));


        //+10 button
        Button buttonPlus = findViewById(R.id.button);
        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geld += 10;
                myTextView.setText(String.valueOf(geld));

                //speichert geld
                SharedPreferences sharedPrefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putInt(KEY_GELD, geld);
                editor.apply();
            }
        });

        //-10 button
        Button buttonminus = findViewById(R.id.button2);
        buttonminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geld -= 10;
                myTextView.setText(String.valueOf(geld));

                //speichert geld
                SharedPreferences sharedPrefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putInt(KEY_GELD, geld);
                editor.apply();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences sharedPrefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(KEY_GELD, geld);
        editor.apply();
    }

    private void increaseMoneyBy50EveryMonth() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int currentMoney = prefs.getInt(KEY_GELD, 0);
        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH);
        int lastMonth = prefs.getInt("last_month", -1);
        if (lastMonth == -1) {
            // Erste Ausführung: Setze das letzte Monat auf den aktuellen Monat
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("last_month", currentMonth);
            editor.apply();
        } else if (currentMonth != lastMonth) {
            // Letzte Ausführung war in einem früheren Monat
            int monthsElapsed = currentMonth - lastMonth;
            int amountToAdd = monthsElapsed * 24;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(KEY_GELD, currentMoney + amountToAdd);
            editor.putInt("last_month", currentMonth);
            editor.apply();
            geld = currentMoney + amountToAdd;
            myTextView.setText(String.valueOf(geld));
        }
    }








//tictactoe button geklickt
    public void buttonClicked(View v) throws InterruptedException {

        //alle Buttons Grau machen
        if(currentPlayer.equals("X"))
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    buttons[i][j].setBackgroundColor(Color.GRAY);
                }
            }
        //vorbereiten für Buttons verändern
        Button button = (Button) v;
        if (button.getText().equals("")) {
            gewinnText.setVisibility(View.INVISIBLE);
            findeButton(button);

            //Button ändern
            buttons[i][j].setText(Objects.equals(currentPlayer, "X") ? "X" : "O");
            buttons[i][j].setTextColor(Objects.equals(currentPlayer, "X") ? Color.GREEN : Color.RED);

            //Prüfen, ob spiel zu ende ist
            if (checkForWin(currentPlayer)) {
                if(currentPlayer.equals("X")) {
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            buttons[i][j].setBackgroundColor(Color.GREEN);
                        }
                    }
                    gewinnText.setTextColor(Color.GREEN);
                }
                else{
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            buttons[i][j].setBackgroundColor(Color.RED);
                        }
                    }
                    gewinnText.setTextColor(Color.RED);
                }
                gewinnText.setText("Player "+currentPlayer+" has won");
                gewinnText.setVisibility(View.VISIBLE);
                endGame();

            } else if (checkForDraw()) {
               endGame();
            } else {


                if(Objects.equals(currentPlayer, PLAYER_X)) currentPlayer = PLAYER_0;
                else currentPlayer = PLAYER_X;
            }
        }

    }






//überprüft, ob das Spiel gewonnen wurde
    private boolean checkForWin(String player) {
        // check rows
        for (int row = 0; row < 3; row++) {
            if (buttons[row][0].getText() == player && buttons[row][1].getText() == player && buttons[row][2].getText() == player) {
                return true;
            }
        }
        // check columns
        for (int column = 0; column < 3; column++) {
            if (buttons[0][column].getText() == player && buttons[1][column].getText() == player && buttons[2][column].getText() == player) {
                return true;
            }
        }
        // check diagonals
        if (buttons[0][0].getText() == player && buttons[1][1].getText() == player && buttons[2][2].getText() == player) {
            return true;
        }
        if (buttons[0][2].getText() == player && buttons[1][1].getText() == player && buttons[2][0].getText() == player) {
            return true;
        }
        return false;
    }

//setzt das Spiel zurück
    private void endGame() {
        System.out.println("player"+currentPlayer+"has won");
        currentPlayer = "X";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
    }


//überprüft, ob unentschieden
    private boolean checkForDraw() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (buttons[row][col].getText() == "") {
                    return false; // there is still an empty cell, so the game is not a draw
                }
            }
        }
        return true; // all cells are filled, so the game is a draw
    }

    public void findeButton(Button button){
        outerloop:
        for (i=0; i <=2; i++) {
            for (j=0; j <= 2; j++) {
                if(button == buttons[i][j])break outerloop;
            }
        }
    }

}