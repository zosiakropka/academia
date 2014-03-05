package pl.killerapps.academia.api.command.subject;

import java.net.URISyntaxException;
import java.util.List;
import org.json.JSONArray;
import pl.killerapps.academia.api.command.ApiCommand;
import pl.killerapps.academia.entities.Subject;
import pl.killerapps.academia.utils.exceptions.FaultyConnectionDetailsException;
import pl.killerapps.academia.utils.exceptions.PreferencesUninitializedException;

/**
 *
 * @author zosia
 */
public abstract class SubjectList extends ApiCommand<List<Subject>> {

  public SubjectList()
          throws URISyntaxException, PreferencesUninitializedException, FaultyConnectionDetailsException {
    super("/subject/list/");
  }

  @Override
  protected List<Subject> process_json(JSONArray json) {

    throw new UnsupportedOperationException("Not supported yet.");
  }

}
