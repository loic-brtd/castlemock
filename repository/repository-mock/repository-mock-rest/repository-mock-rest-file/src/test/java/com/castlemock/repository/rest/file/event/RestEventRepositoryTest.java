/*
 * Copyright 2018 Karl Dahlgren
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

package com.castlemock.repository.rest.file.event;


import com.castlemock.model.mock.rest.domain.RestEvent;
import com.castlemock.model.mock.rest.domain.RestEventTestBuilder;
import com.castlemock.repository.core.file.FileRepositorySupport;
import com.castlemock.repository.rest.file.event.model.RestEventFile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Karl Dahlgren
 * @since 1.4
 */
public class RestEventRepositoryTest {

    @Mock
    private FileRepositorySupport fileRepositorySupport;
    @InjectMocks
    private RestEventFileRepository repository;
    private static final String DIRECTORY = "/directory";
    private static final String EXTENSION = ".extension";

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(repository, "restEventFileDirectory", DIRECTORY);
        ReflectionTestUtils.setField(repository, "restEventFileExtension", EXTENSION);
    }

    @Test
    public void testInitialize(){
        List<RestEvent> restEvents = new ArrayList<>();
        RestEvent restEvent = RestEventTestBuilder.builder().build();
        restEvents.add(restEvent);
        Mockito.when(fileRepositorySupport.load(RestEvent.class, DIRECTORY, EXTENSION)).thenReturn(restEvents);
        repository.initialize();
        Mockito.verify(fileRepositorySupport, Mockito.times(1)).load(RestEventFile.class, DIRECTORY, EXTENSION);
    }

    @Test
    public void testFindOne(){
        final RestEvent restEvent = save();
        final RestEvent returnedRestEvent = repository.findOne(restEvent.getId()).orElse(null);
        Assert.assertNotNull(returnedRestEvent);
        Assert.assertEquals(returnedRestEvent.getResourceId(), restEvent.getResourceId());
        Assert.assertEquals(returnedRestEvent.getApplicationId(), restEvent.getApplicationId());
        Assert.assertEquals(returnedRestEvent.getMethodId(), restEvent.getMethodId());
        Assert.assertEquals(returnedRestEvent.getProjectId(), restEvent.getProjectId());
        Assert.assertEquals(returnedRestEvent.getResourceName(), restEvent.getResourceName());
    }

    @Test
    public void testFindAll(){
        final RestEvent restEvent = save();
        final List<RestEvent> restEvents = repository.findAll();
        Assert.assertEquals(restEvents.size(), 1);
        Assert.assertEquals(restEvents.getFirst().getResourceId(), restEvent.getResourceId());
        Assert.assertEquals(restEvents.getFirst().getApplicationId(), restEvent.getApplicationId());
        Assert.assertEquals(restEvents.getFirst().getMethodId(), restEvent.getMethodId());
        Assert.assertEquals(restEvents.getFirst().getProjectId(), restEvent.getProjectId());
        Assert.assertEquals(restEvents.getFirst().getResourceName(), restEvent.getResourceName());
    }

    @Test
    public void testSave(){
        final RestEvent restEvent = save();
        Mockito.verify(fileRepositorySupport, Mockito.times(1)).save(Mockito.any(RestEventFile.class), Mockito.anyString());
    }

    @Test
    public void testDelete(){
        final RestEvent restEvent = save();
        repository.delete(restEvent.getId());
        Mockito.verify(fileRepositorySupport, Mockito.times(1)).delete(DIRECTORY + File.separator + restEvent.getId() + EXTENSION);
    }

    @Test
    public void testCount(){
        final RestEvent restEvent = save();
        final Integer count = repository.count();
        Assert.assertEquals(Integer.valueOf(1), count);
    }

    private RestEvent save(){
        final RestEvent restEvent = RestEventTestBuilder.builder().build();
        repository.save(restEvent);
        return restEvent;
    }

}
