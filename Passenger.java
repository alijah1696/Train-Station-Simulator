import java.util.*;
import java.lang.*;

public class Passenger extends Entity {
  private static HashMap<String, Passenger> createdPassengers = new HashMap<>();

  private Passenger(String name) { super(name); }

  public static Passenger make(String name) {
    if (!createdPassengers.keySet().contains(name)){
      createdPassengers.put(name, new Passenger(name));
    }
    return createdPassengers.get(name);
  }
}
