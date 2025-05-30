import java.lang.*;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.google.gson.internal.Excluder;

public class PassengerThread extends Thread{
    Passenger passenger;
    PassengerInfo info;
    MBTA mbta;
    Log log;
    int index;

    public PassengerThread(Passenger p, PassengerInfo i ,MBTA m, Log l){
        mbta = m;
        passenger = p;
        info = i;
        log = l;
    }

    @Override
    public void run(){
        //passengers wait to be awaken
        //then board or deboard
        while(!mbta.config.reachedFinal(passenger)){
            if(info.lock.tryLock()){
                try{
                        info.condition.await();
                        if(shouldBoard()) Board();
                        else if(shouldDeboard()) DeBoard();
                }catch(InterruptedException e){
                    throw new RuntimeException("Passenger thread " + passenger + " interrupted");
                }finally{
                    info.lock.unlock();
                }
            }
        }
    }
 
//Helper methods for board and debaording
    public synchronized boolean shouldBoard(){
        if(!info.onTrain){
            Station loc = info.location;
            for(Train t : mbta.config.T.keySet()){
                if(mbta.config.isAtStation(t, loc) && !(mbta.config.reachedFinal(passenger))) 
                return (mbta.config.TrainMatchJourney(passenger, t, loc)
                 && mbta.config.HasNextStop(t, passenger));
            }
        }
        return false;
    } 

    public synchronized void Board(){
        for(Train t : mbta.config.T.keySet()){
            if(mbta.config.isAtStation(t, info.location)){
                mbta.config.board(passenger, t);
                log.passenger_boards(passenger, t, info.location);
                return;
            }
        }
    }
    
    public synchronized boolean shouldDeboard(){
        for(Train t : mbta.config.T.keySet()){
            if(mbta.config.T.get(t).passengers.contains(passenger)){
                Station loc = mbta.config.T.get(t).location;
                return mbta.config.canPassengerDeboard(passenger, t, loc);
            } 
        }
        return false;
    }   

    public synchronized void DeBoard(){ 
       for(Train t : mbta.config.T.keySet()){
            if(mbta.config.T.get(t).passengers.contains(passenger)){
                Station loc = mbta.config.T.get(t).location;
                mbta.config.deboard(passenger, t);
                log.passenger_deboards(passenger, t, loc); 
                return;
            }       
        }
    }
}


