package pl.killerapps.academia.entities;

import java.util.Date;
import java.util.HashMap;

public class Note {

  public Note() {
    id = 0;
    owner = null;
    aktivity_id = 0;
    date = null;
    title = null;
    slug = null;
    content = null;
    access = null;
  }

  public int id;
  
  public String owner;

  public int aktivity_id;

  public Date date;

  public String title;

  public String slug;

  public String content;

  public String access;

  public static final String OPEN = "open";
  public static final String PUBLIC = "public";
  public static final String PRIVATE = "private";
  public static final HashMap<String, String> NOTE_ACCESS = new HashMap<String, String>();

  {
    NOTE_ACCESS.put(OPEN, "Open");
    NOTE_ACCESS.put(PUBLIC, "Public");
    NOTE_ACCESS.put(PRIVATE, "Private");
  }
}
