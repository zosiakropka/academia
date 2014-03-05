package pl.killerapps.academia.api.command.note;

import android.util.Log;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pl.killerapps.academia.api.command.ApiCommandAsync;
import pl.killerapps.academia.entities.Note;
import pl.killerapps.academia.utils.exceptions.FaultyConnectionDetailsException;
import pl.killerapps.academia.utils.exceptions.PreferencesUninitializedException;
import pl.killerapps.academia.utils.safe.SafeActivity;

/**
 *
 * @author zosia
 */
public abstract class NoteList extends ApiCommandAsync<List<Note>> {

  public NoteList(SafeActivity activity)
          throws URISyntaxException, PreferencesUninitializedException, FaultyConnectionDetailsException {
    super("/note/list/", activity);
  }

  @Override
  protected List<Note> process_json(JSONArray notes_json) {
    List<Note> notes = new ArrayList<Note>();
    for (int i = 0; i < notes_json.length(); i++) {
      try {
        JSONObject note_json = notes_json.getJSONObject(i);
        Note note = new Note();
        note.id = note_json.getInt("pk");
        note.access = note_json.getString("access");
        note.date = (new SimpleDateFormat("yyyy-mm-dd")).parse(note_json.getString("date"));
        note.slug = note_json.getString("slug");
        note.title = note_json.getString("title");
        notes.add(note);
      } catch (JSONException ex) {
        Log.e("academia_api", "cant parse json", ex);
      } catch (ParseException ex) {
        Log.e("academia_api", "cant parse date", ex);
      }
    }
    return notes;
  }

}
