/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.killerapps.academia.api.command.subject;

import java.net.URISyntaxException;
import java.util.List;
import org.json.JSONArray;
import pl.killerapps.academia.api.command.ApiCommand;
import pl.killerapps.academia.entities.Subject;

/**
 *
 * @author zosia
 */
public abstract class SubjectList extends ApiCommand<List<Subject>> {

  public SubjectList(String base_url)
    throws URISyntaxException {
    super(base_url, "/subject/list/");
  }

  @Override
  protected List<Subject> process_json(JSONArray json) {

    throw new UnsupportedOperationException("Not supported yet.");
  }

}
