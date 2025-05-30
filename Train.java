import java.util.*;
import java.lang.*;

public class Train extends Entity {
  private static HashMap<String, Train> createdTrains = new HashMap<>();
  
  private Train(String name) { super(name); }

  public static Train make(String name) {
    if (!createdTrains.keySet().contains(name)){
      createdTrains.put(name, new Train(name));
    }
    return createdTrains.get(name);
  }
}
