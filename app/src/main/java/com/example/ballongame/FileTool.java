/*
This class is used for file I/O.
 */

package com.example.ballongame;

import android.app.Activity;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileTool {
    private Activity activity;

    FileTool(Activity activity){
        this.activity=activity;

    }

    public boolean FileExist(){
        File file = new File(activity.getFilesDir(), "scoreRecord");
        return file.exists();

    }
    public void writeFile(ArrayList<Record> recordGroup){
        try {
            File file = new File(this.activity.getFilesDir(), "scoreRecord");
            //Log.d("filepath", activity.getFilesDir().toString());

            PrintWriter score = new PrintWriter(file);
            for(int i=0; i<recordGroup.size(); i++){
                Record r=recordGroup.get(i);
                score.write(r.getPlayer_name()+"\t"+r.getScore()+"\t"+r.getDate());
                if (i!=recordGroup.size()-1)
                    score.write("\n");
            }

            score.close();

        }
        catch(IOException e){
            Log.d("write_error", "Write File error occurred!");

        }
    }

    public void readFile(ArrayList<Record> recordGroup){
        try {
            File load_file = new File(this.activity.getFilesDir(), "scoreRecord");

            Scanner score = new Scanner(load_file);
            while (score.hasNextLine()) {
                String str=score.nextLine();
                String[] arrStr = str.split("\t");
                Record player = new Record();
                player.setPlayer_name(arrStr[0]);
                player.setScore(arrStr[1]);
                player.setDate(arrStr[2]);
                recordGroup.add(player);

            }
            score.close();
        }catch(IOException e){
            Log.d("loaderror","Load file error");
        }



    }

}
