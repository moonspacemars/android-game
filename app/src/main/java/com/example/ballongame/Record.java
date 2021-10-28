/*
This class is for player record.
 */

package com.example.ballongame;

import android.icu.text.SimpleDateFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

public class Record implements Serializable, Comparable<Record> {
    private String rank_no;
    private String player_name;
    private String score;
    private String date;

    public String getRank_no() {
        return rank_no;
    }

    public void setRank_no(String rank_no) {
        this.rank_no = rank_no;
    }

    public String getPlayer_name() {
        return player_name;
    }

    public void setPlayer_name(String player_name) {
        this.player_name = player_name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    //Sort score by descending order. If the scores are same, the recent date has higher priority.
    @RequiresApi(api = Build.VERSION_CODES.N)
    public int compareTo(Record compare) {
        float com_score = Float.parseFloat(compare.getScore());
        float this_score = Float.parseFloat(this.score);
        if (com_score == this_score) {
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date com_date = dateformat.parse(compare.getDate());
                Date this_date = dateformat.parse(this.date);
                return com_date.compareTo(this_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        } else//sort score by descending order
            return (int)(this_score-com_score);


    }
}
