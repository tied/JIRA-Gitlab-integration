package ut.pl.hycom.jira.plugins.gitlab.integration.search;

import lombok.extern.log4j.Log4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import pl.hycom.jira.plugins.gitlab.integration.search.CommitSearcher;
import pl.hycom.jira.plugins.gitlab.integration.search.LucenePathSearcher;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
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
 *
 */

@Log4j
@RunWith(MockitoJUnitRunner.class)
public class CommitSearcherTest {

    @Mock private
    LucenePathSearcher lucenePathSearcher;

    @InjectMocks private CommitSearcher commitSearcher = new CommitSearcher();

    @Test
    public void searchCommitsTest() throws ParseException, IOException {
        Mockito.when(lucenePathSearcher.getIndexPath()).thenReturn(Paths.get("./target/lucenetest/"));

        String fieldName = "author_name";
        String fieldValue = "kamilrogowski";
        List<Document> foundedCommitsList =  commitSearcher.searchCommits(fieldName, fieldValue);

        for(Document document : foundedCommitsList) {
            Assert.assertTrue(document.get("author_name").equals("kamilrogowski"));
        }

    }

    @Test
    public void checkIfCommitIsIndexedTest() throws ParseException, IOException {
        String validIdValue = "da3d482b7a675926502c20b0598b470f05ae8c57";
        String invalidIdValue = "xxx";

        Assert.assertTrue(commitSearcher.checkIfCommitIsIndexed(validIdValue));
        Assert.assertFalse(commitSearcher.checkIfCommitIsIndexed(invalidIdValue));
    }
}