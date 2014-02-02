package pl.killerapps.academia.api.command.note;

import java.net.URISyntaxException;

import org.json.JSONArray;

import pl.killerapps.academia.api.command.ApiCommandAsync;
import pl.killerapps.academia.entities.Note;

public abstract class NoteGet extends ApiCommandAsync<Note> {

  public NoteGet(String base_url)
    throws URISyntaxException {
    super(base_url, "/note/get/");
  }

  @Override
  protected Note process_json(JSONArray json) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
