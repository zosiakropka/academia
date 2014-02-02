package pl.killerapps.academia.entities;

import java.util.List;

public class Subject {

  public Subject() {
    aktivities = null;
    name = null;
    abbr = null;
    id = -1;
  }

  public int id;
  public String name;
  public String abbr;
  public List<Aktivity> aktivities;
}
