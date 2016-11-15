/*
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
package pl.hycom.jira.plugins.gitlab.integration.dao;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import pl.hycom.jira.plugins.gitlab.integration.model.GitlabProject;
import pl.hycom.jira.plugins.gitlab.integration.util.HttpHeadersBuilder;
import pl.hycom.jira.plugins.gitlab.integration.util.TemplateFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Log4j
@Repository
@NoArgsConstructor
public class GitlabCommunicationDao implements IGitlabCommunicationDao {

    private static final String PROJECT = "/api/v3/projects/owned";

    @Autowired private ConfigManagerDao configManagerDao;
    @Autowired private TemplateFactory templateFactory;

    @Override
    public List<GitlabProject> getGitlabProjects(ConfigEntity configEntity){
        HttpEntity<?> requestEntity = new HttpEntity<>(HttpHeadersBuilder.getInstance().setAuth(configEntity.getGitlabSecretToken()).get());
        ResponseEntity<List<GitlabProject>> response = templateFactory.getRestTemplate().exchange(configEntity.getGitlabURL() + PROJECT, HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<List<GitlabProject>>() {
                });
        return response.getBody();
    }
    @Override
    public Long findGitlabProjectId(Long jiraProjectId) throws SQLException {

        final ConfigEntity configEntity = configManagerDao.getProjectConfig(jiraProjectId);
        final List<GitlabProject> gitlabProjectList = this.getGitlabProjects(configEntity);
        final Optional<Long> gitlabProjectId = gitlabProjectList.stream().filter(p -> p.getGitlabProjectName().equalsIgnoreCase(configEntity.getGitlabProjectName())).map(GitlabProject::getGitlabProjectId).findAny();
        if (gitlabProjectId.isPresent()) {
            configManagerDao.updateGitlabProjectId(jiraProjectId, gitlabProjectId.get());
            return gitlabProjectId.get();
        } else {
            return -1L;
        }
    }
}
