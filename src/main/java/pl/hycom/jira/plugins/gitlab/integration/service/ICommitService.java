package pl.hycom.jira.plugins.gitlab.integration.service;

import com.atlassian.jira.issue.Issue;
import org.apache.lucene.queryparser.classic.ParseException;
import pl.hycom.jira.plugins.gitlab.integration.model.Commit;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * <p>Copyright (c) 2016, Authors</p>
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at</p>
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0</p>
 *
 * <p>Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.</p>
 */
public interface ICommitService {

    List<Commit> getNewCommits(Long projectId) throws SQLException, ParseException, IOException;
    Commit getOneCommit(Long projectId, String shaSum) throws SQLException;
    List<Commit> getAllIssueCommits(Issue jiraIssue) throws IOException;
}
