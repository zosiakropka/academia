package pl.killerapps.academia.api.command.subject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.killerapps.academia.api.command.ApiCommandAsync;
import pl.killerapps.academia.entities.Aktivity;
import pl.killerapps.academia.entities.Subject;
import pl.killerapps.academia.utils.exceptions.FaultyConnectionDetailsException;
import pl.killerapps.academia.utils.exceptions.PreferencesUninitializedException;
import pl.killerapps.academia.utils.safe.SafeActivity;

public abstract class SubjectsByAktivity extends ApiCommandAsync<List<Subject>> {

  public SubjectsByAktivity(SafeActivity activity)
          throws URISyntaxException, PreferencesUninitializedException, FaultyConnectionDetailsException {
    super("/subject/list/", activity);
  }

  @Override
  protected List<Subject> process_json(JSONArray subjects_json) {
    List<Subject> subjects = new ArrayList<Subject>();
    for (int i = 0; i < subjects_json.length(); i++) {
      try {
        JSONObject subject_json = subjects_json.getJSONObject(i);
        Subject subject = new Subject();
        subject.name = subject_json.getString("name");
        subject.abbr = subject_json.getString("abbr");
        subject.aktivities = new ArrayList<Aktivity>();
        JSONArray aktivities_json = subject_json.getJSONArray("activities");
        for (int j = 0; j < aktivities_json.length(); j++) {
          JSONObject aktivity_json = aktivities_json.getJSONObject(j);
          Aktivity aktivity = new Aktivity();
          aktivity.type = aktivity_json.getString("type");
          aktivity.id = aktivity_json.getInt("pk");
//                    JSONObject supervisor_json = aktivity_json.getJSONObject("supervisor");
//                    aktivity.supervisor = supervisor_json.getString("fistname") + ' ' + supervisor_json.getString("lastname");
          subject.aktivities.add(aktivity);
        }
        subjects.add(subject);
      } catch (JSONException ex) {
        log.e("Can't parse", ex);
      }
    }
    return subjects;
  }

}
