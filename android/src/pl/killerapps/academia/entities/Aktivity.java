package pl.killerapps.academia.entities;

import java.util.HashMap;
import java.util.List;

/**
 * "k" in Aktivity comes into play to distinguish between Android Activity and Activity entity in Academia notes
 * hierarchy.
 *
 * @author zosia
 *
 */
public class Aktivity {

  public Aktivity() {
    supervisor = null;
    type = null;
    notes = null;
  }

  public int id;

  public String supervisor;
  public String type;
  public List<Note> notes;

  public static final String LECTURE = "lect";
  public static final String LABORATORIUM = "lab";
  public static final String PROJECT = "proj";
  public static final String CONVERSATORY = "conv";
  public static final String FOREIGN_LANGUAGE = "lang";
  public static final String EXERCISE = "exer";
  public static final HashMap<String, String> TYPES = new HashMap<String, String>();

  {
    TYPES.put(LECTURE, "Lecture");
    TYPES.put(LABORATORIUM, "Laboratorium");
    TYPES.put(PROJECT, "Project");
    TYPES.put(CONVERSATORY, "Conversatory");
    TYPES.put(FOREIGN_LANGUAGE, "Foreign language");
    TYPES.put(EXERCISE, "Exercise");
  }
}
