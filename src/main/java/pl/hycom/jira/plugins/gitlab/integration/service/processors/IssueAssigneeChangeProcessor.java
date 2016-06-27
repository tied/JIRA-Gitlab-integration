package pl.hycom.jira.plugins.gitlab.integration.service.processors;

/*
 * <p>Copyright (c) 2016, Damian Deska
 * Project:  gitlab-integration.</p>
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

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.UpdateIssueRequest;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.UserUtils;
import com.atlassian.jira.user.util.UserManager;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import pl.hycom.jira.plugins.gitlab.integration.dao.ConfigEntity;
import pl.hycom.jira.plugins.gitlab.integration.dao.ConfigManagerDaoImpl;
import pl.hycom.jira.plugins.gitlab.integration.model.Commit;
import pl.hycom.jira.plugins.gitlab.integration.service.CommitMessageParserImpl;


/**
 * Created by Damian Deska on 6/14/16.
 */
@Log4j
public class IssueAssigneeChangeProcessor implements ProcessorInterface {

    @Autowired
    UserUtils userUtils;
    @Autowired
    IssueManager issueManager;
    @Autowired
    ApplicationUser applicationUser;
    @Autowired
    ConfigEntity configEntity;
    @Autowired
    UserManager userManager;
    @Autowired
    JiraAuthenticationContext authenticationContext;
    @Autowired
    UpdateIssueRequest.UpdateIssueRequestBuilder issueRequestBuilder;
    ConfigManagerDaoImpl configManagerDao;
    CommitMessageParserImpl commitMessageParser;


    public void execute(Commit commit){

        ApplicationUser executer = userManager.getUserByKey("admin");
        ApplicationUser newAssignee = userUtils.getUserByEmail(commit.getAuthorEmail());

        if(newAssignee == null) {
            log.warn("User not founded");
            return;
        }

        String issueKey = commit.getIssueKey();

        try {

            MutableIssue issue = issueManager.getIssueObject(issueKey);
            issue.setAssignee(newAssignee);

            issueManager.updateIssue(executer, issue, issueRequestBuilder.build());

        } catch (Exception e) {
            log.info("Failed to update issue. Enable debug for more info. Exception message:\n" + e.getMessage());
        }
    }

}
