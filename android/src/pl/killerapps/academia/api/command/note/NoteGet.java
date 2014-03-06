package pl.killerapps.academia.api.command.note;

import java.net.URISyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.killerapps.academia.api.command.ApiCommandAsync;
import pl.killerapps.academia.entities.Note;
import pl.killerapps.academia.utils.Log;
import pl.killerapps.academia.utils.exceptions.FaultyConnectionDetailsException;
import pl.killerapps.academia.utils.exceptions.PreferencesUninitializedException;
import pl.killerapps.academia.utils.safe.SafeActivity;

public abstract class NoteGet extends ApiCommandAsync<Note> {

  Log log = new Log("NoteGet");

  public NoteGet(SafeActivity activity)
          throws URISyntaxException, PreferencesUninitializedException, FaultyConnectionDetailsException {
    super("/note/get/", activity);
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
      } catch (JSONException ex) {
        log.e("Can't parse json", ex);
      }
    }
    return note;
  }

}
