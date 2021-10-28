/*

This activity tracks the top 12 scores of players in a game. Click the ADD button to add a new
high score record. The user enter the name, score and the date to save the record. The program only
displays the top 12 scores records.
 */

package com.example.ballongame;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class RankActivity extends AppCompatActivity {
    private static final int REQUEST_CODE=1;
    private ArrayList<Record> loadGroup;
    private ListView rank_list;
    private String costTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        Button btn_add = findViewById((R.id.btn_add));

        rank_list = findViewById(R.id.list_view);


        FileTool fileTool = new FileTool(this);
        if (!fileTool.FileExist()){
            ArrayList<Record> playGroup = new ArrayList<Record>();
            //create initial 12 records
            for(int i=1; i<=12; i++){
                Record player = new Record();
                String name="Andy"+Integer.toString(i);
                player.setPlayer_name(name);
                String score = Float.toString((float) (50+i*0.1));
                player.setScore(score);
                player.setDate("2020-10-30");
                player.setRank_no(Integer.toString(i));
                playGroup.add(player);
            }
            fileTool.writeFile(playGroup);
        }



        loadGroup = new ArrayList<Record>();


        //read file
        fileTool.readFile(loadGroup);

        //set rank number
        for(int i=0; i<loadGroup.size(); i++){
            Record record =loadGroup.get(i);
            record.setRank_no(Integer.toString(i+1));
        }

        //display on list view
        LVAdapter adapter = new LVAdapter(this, loadGroup);
        rank_list.setAdapter(adapter);

        Intent fromGame=getIntent();
        costTime=fromGame.getStringExtra("score");


        //if the score is not in top 12, it can't be added.
        if (loadGroup.size()==12){
            float curr_score= Float.parseFloat(costTime);
            Record record =loadGroup.get(11);

            float last_score=Float.parseFloat(record.getScore());
            if (curr_score<last_score)
                btn_add.setEnabled(true);
            else
                btn_add.setEnabled(false);
        }
        else
            btn_add.setEnabled(true);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RankActivity.this, AddActivity.class);
                intent.putExtra("score", costTime);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case REQUEST_CODE:
                if (data!=null) {
                    Record player = (Record) data.getSerializableExtra("play_record");
//                    Log.d("getdataback", player.getPlayer_name() + player.getScore());
                    loadGroup.add(player);
                    Collections.sort(loadGroup);


                    //only keep 12 records in the list
                    if (loadGroup.size()>12)
                        loadGroup.remove(12);
                    //set rank number
                    for(int i=0; i<loadGroup.size(); i++){
                        Record record =loadGroup.get(i);
                        record.setRank_no(Integer.toString(i+1));
                    }


                    LVAdapter adapter = new LVAdapter(this, loadGroup);
                    rank_list.setAdapter(adapter);
                    FileTool fileTool = new FileTool(this);
                    fileTool.writeFile(loadGroup);
                }




                break;
        }
    }
}