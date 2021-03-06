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
package pl.hycom.jira.plugins.gitlab.integration.model.events;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.List;

@Generated("org.jsonschema2pojo")
@JsonAutoDetect
@Data
public class Push {

    @JsonProperty("object_kind")
    public String objectKind;
    @JsonProperty("before")
    public String before;
    @JsonProperty("after")
    public String after;
    @JsonProperty("ref")
    public String ref;
    @JsonProperty("checkout_sha")
    public String checkoutSha;
    @JsonProperty("user_id")
    public Integer userId;
    @JsonProperty("user_name")
    public String userName;
    @JsonProperty("user_email")
    public String userEmail;
    @JsonProperty("user_avatar")
    public String userAvatar;
    @JsonProperty("project_id")
    public Integer projectId;
    @JsonProperty("project")
    public Project project;
    @JsonProperty("repository")
    public Repository repository;
    @JsonProperty("commits")
    public List<CommitEvent> commits = new ArrayList<CommitEvent>();
    @JsonProperty("total_commits_count")
    public Integer totalCommitsCount;

}
