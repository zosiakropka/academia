package pl.killerapps.academia.api.command.note;

import android.app.Activity;
import java.net.URISyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import pl.killerapps.academia.api.command.ApiCommandAsync;
import pl.killerapps.academia.entities.Note;

public abstract class NoteGet extends ApiCommandAsync<Note> {

  public NoteGet(String base_url, Activity activity)
          throws URISyntaxException {
    super(base_url, "/note/get/", activity);
  }

  @Override
  protected Note process_json(JSONArray notes_json) {
    Note note = new Note();
    if (notes_json.length() == 1) {
      try {
        JSONObject note_json = notes_json.getJSONObject(0);
        note.access = note_json.getString("access");
        note.id = note_json.getInt("pk");
        note.aktivity_id = note_json.getInt("activity");
        note.title = note_json.getString("title");
        note.content = note_json.getString("content");
        // @todo fill missing parts
      } catch (JSONException e) {
        Log.e("academia_api", "Can't parse", e);
      }
    }
    return note;
  }

}
