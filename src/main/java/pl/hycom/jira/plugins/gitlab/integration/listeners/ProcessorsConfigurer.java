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
package pl.hycom.jira.plugins.gitlab.integration.listeners;

import com.atlassian.sal.api.lifecycle.LifecycleAware;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import pl.hycom.jira.plugins.gitlab.integration.service.ProcessorManager;
import pl.hycom.jira.plugins.gitlab.integration.service.processors.ProcessorInterface;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Log4j
@RequiredArgsConstructor(onConstructor = @__(@Inject)) //Inject all final variables.
public class ProcessorsConfigurer implements LifecycleAware {

    private final ProcessorManager processorManager;
    private final ApplicationContext context;

    @Override
    public void onStart() {

        List<ProcessorInterface> processorsList = new ArrayList<>();
        log.info("Started searching for processors");
        long startTime = System.currentTimeMillis();
        String[] beans = context.getBeanNamesForType(ProcessorInterface.class);
        for (String name : Arrays.asList(beans)) {
            Object bean = context.getBean(name);
            if (bean instanceof ProcessorInterface) {
                ProcessorInterface myBean = (ProcessorInterface) bean;
                processorsList.add(myBean);
            }
        }

        processorManager.setProcessorsList(processorsList);
        log.info("Found "+ processorsList.size() + " in " +
                (System.currentTimeMillis() - startTime) + " ms.");
    }

    public void onStop() {
        //Nothing to see here. This method does nothing intentionally.
    }

}
