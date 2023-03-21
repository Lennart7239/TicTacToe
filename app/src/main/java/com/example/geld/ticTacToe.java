package com.example.geld;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;


import java.util.Objects;

class tictactoe extends AppCompatActivity {

    private static final int EMPTY = 0;
    private static final String PLAYER_0 = "1";
    private static final String PLAYER_X = "2";
    String currentPlayer = PLAYER_X;

    Button[][] buttons = new Button[3][3];





    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tictactoe);
        // Initialisierung der Buttons
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

    // Diese Methode wird aufgerufen, wenn ein Button geklickt wird
    public void buttonClicked(View view) {

            Button button = (Button) view;
            if (button.getText() == null) {
                System.out.println("test");
                button.setText(Objects.equals(currentPlayer, PLAYER_X) ? "X" : "O");
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);
                button.setTypeface(null, Typeface.BOLD);
                button.setTextColor(Objects.equals(currentPlayer, PLAYER_X) ? Color.BLUE : Color.RED);

                if (checkForWin(currentPlayer)) {
                    endGame();
                } else if (checkForDraw()) {
                    endGame();
                } else {


                if(Objects.equals(currentPlayer, PLAYER_X)) currentPlayer = PLAYER_0;
                        else currentPlayer = PLAYER_X;
            }
        }

}

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

    private void endGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText(null);
            }
        }
    }

    private boolean checkForDraw() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (buttons[row][col].getText() == null) {
                    return false; // there is still an empty cell, so the game is not a draw
                }
            }
        }
        return true; // all cells are filled, so the game is a draw
    }

}