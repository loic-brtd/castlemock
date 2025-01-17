/*
 * Copyright 2015 Karl Dahlgren
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.castlemock.service.mock.soap.project.adapter;

import com.castlemock.model.core.ServiceProcessor;
import com.castlemock.model.mock.soap.domain.SoapProject;
import com.castlemock.model.mock.soap.domain.SoapProjectTestBuilder;
import com.castlemock.service.mock.soap.project.input.ReadAllSoapProjectsInput;
import com.castlemock.service.mock.soap.project.output.ReadAllSoapProjectsOutput;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

/**
 * @author Karl Dahlgren
 * @since 1.0
 */
public class SoapProjectServiceAdapterTest {

    @Mock
    private ServiceProcessor serviceProcessor;

    @InjectMocks
    private SoapProjectServiceAdapter adapter;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testReadAll(){
        final SoapProject project = SoapProjectTestBuilder.builder().build();
        final List<SoapProject> projects = List.of(project);
        final ReadAllSoapProjectsOutput output = ReadAllSoapProjectsOutput.builder()
                .projects(projects)
                .build();

        Mockito.when(serviceProcessor.process(Mockito.any(ReadAllSoapProjectsInput.class))).thenReturn(output);

        final List<SoapProject> returnedProjects = adapter.readAll();

        Assert.assertEquals(projects, returnedProjects);
        Mockito.verify(serviceProcessor, Mockito.times(1)).process(Mockito.any(ReadAllSoapProjectsInput.class));
    }

}
