package pl.hycom.jira.plugins.gitlab.integration.gitpanel;

import com.atlassian.jira.issue.tabpanels.GenericMessageAction;
import com.atlassian.jira.permission.ProjectPermissions;
import com.atlassian.jira.plugin.issuetabpanel.*;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.user.ApplicationUser;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.hycom.jira.plugins.gitlab.integration.model.Commit;
import pl.hycom.jira.plugins.gitlab.integration.service.CommitService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
@Log4j
@Controller
public class GitTabPanel extends AbstractIssueTabPanel2 {

    private IssueTabPanelModuleDescriptor descriptor;

    @Autowired private CommitService commitService;
    @Autowired private PermissionManager permissionManager;

    @Override
    public ShowPanelReply showPanel(ShowPanelRequest showPanelRequest) {
        try {
            final Project project = showPanelRequest.issue().getProjectObject();
            final ApplicationUser user = showPanelRequest.remoteUser();
            final boolean hasPermission = permissionManager.hasPermission(ProjectPermissions.WORK_ON_ISSUES, project, user);
            log.info("User: " + (user != null ? user.getUsername() : null) + " requested to see git panel for project: "
                    + (project != null ? project.getKey() : null) + ", issue: " + showPanelRequest.issue().getKey()
                    + ". Displaying panel? " + hasPermission);
            return ShowPanelReply.create(hasPermission);
        } catch (Exception e) {
            log.error("Exception occurred while trying to determine if panel should be seen, with message: " + e.getMessage(), e);
            return ShowPanelReply.create(Boolean.FALSE);
        }
    }


    @Override
    public GetActionsReply getActions(GetActionsRequest getActionsRequest) {
        try {
            List<Commit> commitsListForIssue = commitService.getAllIssueCommits(getActionsRequest.issue());

            Commit commit = commitsListForIssue != null && !commitsListForIssue.isEmpty() ? commitsListForIssue.get(0) : null;
            log.warn("Commit: " + commit);
            final List<IssueAction> actions = createActionList(commitsListForIssue);
            if (actions.isEmpty()) {
                actions.add(new GenericMessageAction("There are no commits for this issue, yet. Maybe you should add some?"));
            }
            return GetActionsReply.create(actions);
        } catch (IOException e) {
            log.info("There was an error while trying to get commits for issue: " + getActionsRequest.issue(), e);
        }
        return GetActionsReply.create(Collections.singletonList(new GenericMessageAction("There was an error processing commits for this issue. Please consult your administrator")));
    }

    private List<IssueAction> createActionList(List<Commit> commitsListForIssue) {
        return commitsListForIssue.stream().map(GitCommitAction::new).collect(Collectors.toList());
    }
}
