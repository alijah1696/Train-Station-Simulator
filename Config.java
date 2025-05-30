import java.util.*;
import java.lang.*;


public class Config {

    public Map<Train, TrainInfo> T = new LinkedHashMap<>();
    public Map<Passenger, PassengerInfo> P = new LinkedHashMap<>();

    public Map<String, List<String>> lines = new LinkedHashMap<>();
    public Map<String, List<String>> trips = new LinkedHashMap<>();

//Constructors  
  public Config(Map<String, List<String>> lines, Map<String, List<String>> trips){
        this.lines = lines;
        this.trips = trips;
    }

    public Config(){}

//Basic methods
    public void setUp(){
        for(String t : lines.keySet()){
            addLine(t, lines.get(t));
        }
        for(String p : trips.keySet()){
            addJourney(p, trips.get(p));
        }
    }

    public void checkStart(){
        for(Train t : T.keySet()){
            if (!TrainAtStart(t)) throw new RuntimeException("Invalid Start");
        }
        for(Passenger p : P.keySet()){
            if (!PassengerAtStart(p)) throw new RuntimeException("Invalid Start");
        }
    }

    public void checkEnd(){
        for(Passenger p : P.keySet()){
            if(PassengerOnTrain(p) || !reachedFinal(p)){
                throw new RuntimeException("Invalid end : " + p.toString() + P.get(p));
            }
        }
    }

    public void addLine(String name, List<String> stations){
        Train train = Train.make(name);
        ArrayList<Station> route = new ArrayList<>();
        for(String s : stations){
            route.add(Station.make(s));
        }
        T.put(train, new TrainInfo(new ArrayList<Passenger>(), route.getFirst(), route));
    }

    public void addJourney(String name, List<String> stations){
        Passenger passenger = Passenger.make(name);
        ArrayList<Station> journey = new ArrayList<>();
        for(String j : stations){
            journey.add(Station.make(j));
        }
        P.put(passenger, new PassengerInfo(journey.getFirst(), journey));
    }

    public ArrayList<Passenger> PassengersAtStation(Station s){
        ArrayList<Passenger> passengers = new ArrayList<>();
        for(Passenger p : P.keySet()){
            if(P.get(p).location.equals(s) && !P.get(p).onTrain) passengers.add(p);
        }
        return passengers;
    }

//Change state methods
    public synchronized void move(Train t, Station s1, Station s2){
        T.get(t).location = s2;
        if(isAtStation(t, T.get(t).route.getLast()) 
        || isAtStation(t, T.get(t).route.getFirst())) 
        swapDirections(t);
    }

    public synchronized void board(Passenger p, Train t){
        T.get(t).passengers.add(p);
        P.get(p).onTrain = true;
    }

    public synchronized void deboard(Passenger p, Train t){
        P.get(p).location = T.get(t).location;
        P.get(p).onTrain = false;
        T.get(t).passengers.remove(p);
}


//Get State boolean Methods
    public boolean hasTrain(Train t){
        return T.containsKey(t);
    }

    public boolean hasPassenger(Passenger p){
        return P.containsKey(p);
    }

    public boolean TrainAtStart(Train t){
        if(hasTrain(t)){
            if(!T.get(t).route.isEmpty()) 
            return T.get(t).location.equals(T.get(t).route.getFirst());
        }
        return false;
    }

    public boolean PassengerAtStart(Passenger p){
        if(hasPassenger(p)){
            if(!P.get(p).journey.isEmpty())
            return P.get(p).location.equals(P.get(p).journey.getFirst());
        }
        return false;
    }

    public boolean TrainHaveRoute(Train t, Station s){
        if(hasTrain(t)){
            return T.get(t).route.contains(s);
        }
        return false;
    }

    public boolean PassengerHaveJourney(Passenger p, Station s){
        if(hasPassenger(p)){
            return P.get(p).journey.contains(s);
        }   
        return false;
    }

    public boolean TrainMoveOnce(Train t, Station s1, Station s2){
        if(TrainHaveRoute(t, s2) && TrainHaveRoute(t, s2)){
            int val = T.get(t).route.indexOf(s2) - T.get(t).route.indexOf(s1);
            if(val == 1 && T.get(t).forward) return true;
            else if(val == -1 && !T.get(t).forward) return true;
        }
        return false;
    }

    public boolean isStationOccupied(Station s){
        for(Train t : T.keySet()){
            if(T.get(t).location.equals(s)) return true;
        }
        return false;
    }

    public boolean isAtStation(Train t, Station s){
        return T.get(t).location.equals(s);
    }

    public boolean HasNextStop(Train t, Passenger p){
        int index = P.get(p).journey.indexOf(P.get(p).location);
        if((index+1) < P.get(p).journey.size() && P.get(p).journey.get(index+1) != null){
            return TrainHaveRoute(t, P.get(p).journey.get(index+1));
        }
        return false;
    }

    public boolean TrainMatchJourney(Passenger p, Train t, Station s){
            
        return (TrainHaveRoute(t, s) && T.get(t).location.equals(s)
                && PassengerHaveJourney(p, s) && P.get(p).location.equals(s));
    }

    public boolean PassengerOnTrain(Passenger p){
        if(hasPassenger(p)){
            return P.get(p).onTrain;
        }
        return false;
    }
 
    public boolean canPassengerDeboard(Passenger p, Train t, Station s){
        if(T.get(t).passengers.contains(p)){
            if(T.get(t).passengers.contains(p) && PassengerHaveJourney(p, s)){
                int index = P.get(p).journey.indexOf(P.get(p).location);
                if((index + 1) < P.get(p).journey.size()){
                    return s.equals(P.get(p).journey.get(index+1));
                }
            }
        }
        return false;  
    }

    public boolean reachedFinal(Passenger p){
        if(hasPassenger(p) && !P.get(p).journey.isEmpty()){
            return P.get(p).location.equals(P.get(p).journey.getLast());
        }
        return false;
    }

    public boolean Completed(){
        for(Passenger p : P.keySet()){
            if(!reachedFinal(p)) return false;
        }
        return true;
    }

    public boolean swapDirections(Train t){
        T.get(t).forward = !T.get(t).forward;
        return T.get(t).forward;
    }
//Helper Methods/Debugging
    public String toString(){
        String ts = "";
        for(Train t : T.keySet()){
            ts+= "(" + t.toString() + " Train)\n";
            ts+= "  Location : " + T.get(t).location + "\n";
            ts+= "  Route : " + T.get(t).route + "\n";
            ts+= "  Passengers : " + T.get(t).passengers + "\n";
            ts+= "  Direction : " + (T.get(t).forward ? "Forward" : "Backward") + "\n";
        }
        String ps = "\n";
        for(Passenger p : P.keySet()){
            ps+= "<Passenger " + p.toString() + ">\n";
            ps+= "  Location : " + P.get(p).location + "\n";
            ps+= "  Journey : " + P.get(p).journey + "\n";
            ps+= "  Action : " + (P.get(p).onTrain ? "  on Train " : " at Station " + "\n");
        }
        return ts+ps;
    }
}
