import java.util.*;
import java.lang.*;

public class Station extends Entity {
  private static HashMap<String, Station> createdStations = new HashMap<>();

  private Station(String name) { super(name); }

  public static Station make(String name) {
    if (!createdStations.keySet().contains(name)){
      createdStations.put(name, new Station(name));
    }
    return createdStations.get(name);
  }
}
