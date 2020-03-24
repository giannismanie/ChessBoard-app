package com.manie.chessboardrender;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText sizeText;
    private Button button;
    private GridLayout gridLayout;
    private int size, moves = 0, x =0;
    private  int[] row = new int[2];
    private  int[] col = new int[2];
    private List<Button> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        sizeText = (EditText) findViewById(R.id.sizeId);
        Button run = (Button) findViewById(R.id.run);
        Button reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(this);
        run.setOnClickListener(this);
    }

    //  It creates the chessboard for 6 <= N <= 16
    private void initGridView() {
        size = Integer.parseInt(sizeText.getText().toString());
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                GridLayout.Spec rowSpec = GridLayout.spec(row);
                GridLayout.Spec colSpec = GridLayout.spec(col);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, colSpec);
                params.width = 900 / size;
                params.height = 900 / size;
                params.rightMargin = 5;
                params.topMargin = 1;
                button = new Button(this);
                button.setOnClickListener(this);
                button.setId(x);
                x++;
                gridLayout.addView(button, params);
                if (row % 2 == col % 2){
                    button.setBackgroundColor(Color.LTGRAY);
                }else button.setBackgroundColor(Color.BLACK);
            }
        }
        list = new ArrayList<>();
        for (int id = 0; id < size * size; id++) {
            button = (Button) gridLayout.findViewById(id);
            list.add(button);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.run) { //  Run button
            if (list.size() != 0) {
                for (int id = 0; id < size * size; id++) {
                    gridLayout.removeView(list.get(id));
                }
            }
            if ("".equals(sizeText.getText().toString())){
                Toast.makeText(getApplication(), "Enter something from 6-16 !!", Toast.LENGTH_SHORT).show();
            } else {
                int N = Integer.parseInt(sizeText.getText().toString());
                if (N > 5 && N < 17){
                    initGridView();
                } else {
                    Toast.makeText(getApplication(), "Enter number from 6-16 !!", Toast.LENGTH_SHORT).show();
                }
            }
        }else if (v.getId() == R.id.reset){ //  Reset button
            for (int id = 0; id < size * size; id++) {
                gridLayout.removeView(list.get(id));
            }moves = 0;
            Toast.makeText(getApplication(), "Don't works correctly yet, clear it from RAM and open it again!", Toast.LENGTH_SHORT).show();
        } else {//   grid buttons
            if (moves < 2){
                ((Button) v).setText("X");
                v.setBackgroundColor(Color.RED);
                row[moves] = v.getId() / size; //    row
                col[moves] = v.getId() % size; //    col
                moves++;
                button = list.get(v.getId());
                if (moves == 2) checkForWin();
            }else checkForWin();
        }
    }

    private void checkForWin(){
        if (ValidMove()){
            Toast.makeText(getApplication(), "Great job!!!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplication(), "Knight cant go there!!", Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplication(), "Reset and try again!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean ValidMove(){
        //  Knight moves
        int[] jumpx = {2,+2,-2,-2,1,+1,-1,-1};
        int[] jumpy = {1,-1,+1,-1,2,-2,+2,-2};
        int x = 0, y, count = 0;
        boolean suc = false, outOfRegion = false;

        for (int a = 0; a < 8; a++){
            x = y = 0; // Reset values
            //  if don't exceeds the field
            if ( ((row[0] + jumpx[a]) <= size) && ((row[0] + jumpx[a]) >= 0) && ((col[0] + jumpy[a]) <= size) && ((col[0] + jumpy[a]) >= 0) ){
                x = row[0] + jumpx[a];
                y = col[0] + jumpy[a];
                outOfRegion = false;
            }else {//   else out of field
                outOfRegion = true;
            }
            for (int b = 0; b < 8; b++){
                if (!outOfRegion){ // if previously was in the field
                    if ( (x + jumpx[b] <= size) && (x + jumpx[b] >= 0) && (y + jumpy[b] <= size) && (y + jumpy[b] >= 0) ) {
                        x = row[0] + jumpx[a];
                        y = col[0] + jumpy[a];
                        x = x + jumpx[b];
                        y = y + jumpy[b];
                        outOfRegion = false;
                    }else { // if was not
                        outOfRegion = true;
                        x = y = -100;
                    }
                }
                for (int c = 0; c < 8; c++){
                    if (!outOfRegion){// if previously was in the field
                        if ( ( (x + jumpx[c] <= size) && (x + jumpx[c] >= 0) && (y + jumpy[c] <= size) && (y + jumpy[c] >= 0) ) ){
                            x = x + jumpx[c];
                            y = y + jumpy[c];
                        }else x = y = -100;// if was not
                    }
                    if ( (row[1] == x) && (col[1] == y) ){
                        suc = true;
                        count++;
                    }else {
                        x = row[0] + jumpx[a] + jumpx[b];
                        y = col[0] + jumpy[a] + jumpy[b];
                    }
                }
            }
        }Toast.makeText(this,String.valueOf(count) + " Different Ways",Toast.LENGTH_SHORT).show();
        if (suc){
            return true;
        }else return false;
    }
}
