/*
This interface is for the communication between touch event and view
 */

package com.example.ballongame;

public interface TouchListener {
    public void onCount(int i);
    public void onTimeCost(long i);
    public void missedCount(int i);
}
