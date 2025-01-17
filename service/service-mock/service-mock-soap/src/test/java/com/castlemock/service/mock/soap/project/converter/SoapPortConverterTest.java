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

package com.castlemock.service.mock.soap.project.converter;

import com.castlemock.model.core.http.HttpMethod;
import com.castlemock.model.mock.soap.domain.SoapOperation;
import com.castlemock.model.mock.soap.domain.SoapOperationIdentifier;
import com.castlemock.model.mock.soap.domain.SoapOperationStatus;
import com.castlemock.model.mock.soap.domain.SoapPort;
import com.castlemock.model.mock.soap.domain.SoapResourceType;
import com.castlemock.model.mock.soap.domain.SoapResponseStrategy;
import com.castlemock.model.mock.soap.domain.SoapVersion;
import com.castlemock.service.core.manager.FileManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
public class SoapPortConverterTest {

    @InjectMocks
    private SoapPortConverter soapPortConverter;

    @Mock
    private FileManager fileManager;

    @Before
    public void initiateTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetSoapPortsFiles() {
        try {
            final List<File> files = this.loadFile("ServiceExample1.wsdl");
            final Set<SoapPortConverterResult> results = soapPortConverter.getSoapPorts(files, UUID.randomUUID().toString(), true);

            Assert.assertEquals(1, results.size());

            SoapPortConverterResult result = results.stream().filter(tmpResult -> tmpResult
                    .getName().equals("ServiceExample1.wsdl"))
                    .findFirst()
                    .orElse(null);

            Assert.assertNotNull(result);
            Assert.assertEquals(SoapResourceType.WSDL, result.getResourceType());
            final Set<SoapPort> soapPorts = result.getPorts();

            final SoapPort soapPort = soapPorts.stream().findFirst().orElse(null);

            Assert.assertNotNull(soapPort);
            this.verify(soapPort, "ServiceExample1", SoapVersion.SOAP11);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGetSoapPortsLink() {
        try {
            final String wsdlLocation = "http://castlemock.com/ServiceExample1.wsdl";
            final List<File> files = this.loadFile("ServiceExample1.wsdl");

            Mockito.when(fileManager.uploadFiles(wsdlLocation)).thenReturn(files);

            final Set<SoapPortConverterResult> results = soapPortConverter.getSoapPorts(wsdlLocation, UUID.randomUUID().toString(), true, false);

            Assert.assertEquals(1, results.size());

            SoapPortConverterResult result = results.stream().filter(tmpResult -> tmpResult
                    .getName().equals("ServiceExample1.wsdl"))
                    .findFirst()
                    .orElse(null);

            Assert.assertNotNull(result);
            Assert.assertEquals(SoapResourceType.WSDL, result.getResourceType());
            final Set<SoapPort> soapPorts = result.getPorts();

            final SoapPort soapPort = soapPorts.stream().findFirst().orElse(null);
            Assert.assertNotNull(soapPort);
            this.verify(soapPort, "ServiceExample1", SoapVersion.SOAP11);

            Mockito.verify(this.fileManager, Mockito.times(1)).uploadFiles(wsdlLocation);

        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGetSoapPortsLinkImportExternal() {
        try {
            final String wsdlLocation = "http://castlemock.com/ServiceExample1.wsdl";
            final String externalWsdlLocation2 = "http://castlemock.com/ServiceExample2.wsdl";
            final String externalWsdlLocation3 = "http://castlemock.com/ServiceExample3.wsdl";

            final List<File> files = this.loadFile("ServiceExample1.wsdl");
            final List<File> externalFiles2 = this.loadFile("ServiceExample2.wsdl");
            final List<File> externalFiles3 = this.loadFile("ServiceExample3.wsdl");

            Mockito.when(fileManager.uploadFiles(Mockito.eq(wsdlLocation))).thenReturn(files);
            Mockito.when(fileManager.uploadFiles(Mockito.eq(externalWsdlLocation2))).thenReturn(externalFiles2);
            Mockito.when(fileManager.uploadFiles(Mockito.eq(externalWsdlLocation3))).thenReturn(externalFiles3);

            // External files

            final Set<SoapPortConverterResult> results = soapPortConverter.getSoapPorts(wsdlLocation, UUID.randomUUID().toString(), true, true);

            Assert.assertEquals(3, results.size());

            SoapPortConverterResult result1 = results.stream()
                    .filter(result -> result.getName().equals("ServiceExample1.wsdl"))
                    .findFirst()
                    .orElse(null);

            SoapPortConverterResult result2 = results.stream()
                    .filter(result -> result.getName().equals("ServiceExample2.wsdl"))
                    .findFirst()
                    .orElse(null);

            SoapPortConverterResult result3 = results.stream()
                    .filter(result -> result.getName().equals("ServiceExample3.wsdl"))
                    .findFirst()
                    .orElse(null);

            Assert.assertNotNull(result1);
            Assert.assertNotNull(result2);
            Assert.assertNotNull(result3);
            Assert.assertEquals(SoapResourceType.WSDL, result1.getResourceType());
            Assert.assertEquals(SoapResourceType.WSDL_IMPORT, result2.getResourceType());
            Assert.assertEquals(SoapResourceType.WSDL_IMPORT, result3.getResourceType());

            final SoapPort serviceExample1 = result1
                    .getPorts()
                    .stream()
                    .findFirst()
                    .orElse(null);

            final SoapPort serviceExample2 = result2
                    .getPorts()
                    .stream()
                    .findFirst()
                    .orElse(null);

            final SoapPort serviceExample3 = result3
                    .getPorts()
                    .stream()
                    .findFirst()
                    .orElse(null);

            Assert.assertNotNull(serviceExample1);
            Assert.assertNotNull(serviceExample2);
            Assert.assertNotNull(serviceExample3);

            this.verify(serviceExample1, "ServiceExample1" ,SoapVersion.SOAP11);
            this.verify(serviceExample2, "ServiceExample2", SoapVersion.SOAP11);
            this.verify(serviceExample3, "ServiceExample3", SoapVersion.SOAP11);

            Mockito.verify(this.fileManager, Mockito.times(1)).uploadFiles(wsdlLocation);
            Mockito.verify(this.fileManager, Mockito.times(1)).uploadFiles(externalWsdlLocation2);
            Mockito.verify(this.fileManager, Mockito.times(1)).uploadFiles(externalWsdlLocation3);

        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGetSoapPortsMultipleInputParts() {
        try {
            final List<File> files = this.loadFile("ServiceExample4.wsdl");
            final Set<SoapPortConverterResult> results = soapPortConverter.getSoapPorts(files, UUID.randomUUID().toString(), true);

            Assert.assertEquals(1, results.size());

            SoapPortConverterResult result = results.stream().filter(tmpResult -> tmpResult
                    .getName().equals("ServiceExample4.wsdl"))
                    .findFirst()
                    .orElse(null);

            Assert.assertNotNull(result);
            Assert.assertEquals(SoapResourceType.WSDL, result.getResourceType());
            final Set<SoapPort> soapPorts = result.getPorts();

            final SoapPort soapPort = soapPorts.stream().findFirst().orElse(null);
            this.verify(soapPort, "ServiceExample4", SoapVersion.SOAP11);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGetSoapPortsWithoutBodyParts() {
        try {
            final List<File> files = this.loadFile("ServiceExample5.wsdl");
            final Set<SoapPortConverterResult> results = soapPortConverter.getSoapPorts(files, UUID.randomUUID().toString(), true);

            Assert.assertEquals(1, results.size());

            SoapPortConverterResult result = results.stream().filter(tmpResult -> tmpResult
                    .getName().equals("ServiceExample5.wsdl"))
                    .findFirst()
                    .orElse(null);

            Assert.assertNotNull(result);
            Assert.assertEquals(SoapResourceType.WSDL, result.getResourceType());
            final Set<SoapPort> soapPorts = result.getPorts();

            final SoapPort soapPort = soapPorts.stream().findFirst().orElse(null);
            this.verify(soapPort, "ServiceExample5", SoapVersion.SOAP11);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGetSoapPortsSoap12() {
        try {
            final List<File> files = this.loadFile("ServiceExampleSoap12.wsdl");
            final Set<SoapPortConverterResult> results = soapPortConverter.getSoapPorts(files, UUID.randomUUID().toString(), true);

            Assert.assertEquals(1, results.size());

            SoapPortConverterResult result = results.stream().filter(tmpResult -> tmpResult
                    .getName().equals("ServiceExampleSoap12.wsdl"))
                    .findFirst()
                    .orElse(null);
            Assert.assertNotNull(result);
            Assert.assertEquals(SoapResourceType.WSDL, result.getResourceType());
            final Set<SoapPort> soapPorts = result.getPorts();

            final SoapPort soapPort = soapPorts.stream().findFirst().orElse(null);
            this.verify(soapPort, "ServiceExampleSoap12", SoapVersion.SOAP12);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGetSoapPorts() {
        try {
            final List<File> files = this.loadFile("wsdl.wsdl");
            final Set<SoapPortConverterResult> results = soapPortConverter.getSoapPorts(files, UUID.randomUUID().toString(), true);

            Assert.assertEquals(1, results.size());

            SoapPortConverterResult result = results.stream().filter(tmpResult -> tmpResult
                    .getName().equals("wsdl.wsdl"))
                    .findFirst()
                    .orElse(null);

            Assert.assertNotNull(result);
            Assert.assertEquals(SoapResourceType.WSDL, result.getResourceType());
            final Set<SoapPort> soapPorts = result.getPorts();

            final SoapPort soapPort = soapPorts.stream().findFirst().orElse(null);
            //this.verify(soapPort, "ServiceExampleSoap12", SoapVersion.SOAP12);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGetOperationIdentifier() throws URISyntaxException {
        final List<File> files = this.loadFile("ServiceExample7.wsdl");
        final Set<SoapPortConverterResult> results = soapPortConverter.getSoapPorts(files, UUID.randomUUID().toString(), true);

        Assert.assertEquals(1, results.size());

        SoapPortConverterResult result = results.stream().filter(tmpResult -> tmpResult
                .getName().equals("ServiceExample7.wsdl"))
                .findFirst()
                .orElse(null);

        Assert.assertNotNull(result);
        Assert.assertEquals(SoapResourceType.WSDL, result.getResourceType());
        final Set<SoapPort> soapPorts = result.getPorts();
        final SoapPort soapPort = soapPorts.stream().findFirst().orElse(null);

        Assert.assertNotNull(soapPort);
        Assert.assertEquals("ServiceExample7.Endpoint", soapPort.getName());
        Assert.assertEquals("ServiceExample7.Endpoint", soapPort.getUri());
        Assert.assertEquals(1, soapPort.getOperations().size());

        SoapOperation soapOperation = soapPort.getOperations().getFirst();

        Assert.assertEquals("ServiceExample7", soapOperation.getName());
        Assert.assertEquals(SoapResponseStrategy.RANDOM, soapOperation.getResponseStrategy());
        Assert.assertEquals(SoapOperationStatus.MOCKED, soapOperation.getStatus());
        Assert.assertEquals(SoapVersion.SOAP11, soapOperation.getSoapVersion());
        Assert.assertEquals(HttpMethod.POST, soapOperation.getHttpMethod());

        SoapOperationIdentifier operationIdentifier = soapOperation.getOperationIdentifier();

        Assert.assertNotNull(operationIdentifier);
        Assert.assertEquals("Request", operationIdentifier.getName());
        Assert.assertNull(operationIdentifier.getNamespace().orElse(null));
    }
    
    private void verify(final SoapPort soapPort,
                        final String name,
                        final SoapVersion soapVersion){
        Assert.assertNotNull(soapPort);
        Assert.assertEquals(name + ".Endpoint", soapPort.getName());
        Assert.assertEquals(name + ".Endpoint", soapPort.getUri());
        Assert.assertEquals(1, soapPort.getOperations().size());

        SoapOperation soapOperation = soapPort.getOperations().getFirst();

        Assert.assertEquals(name, soapOperation.getName());
        Assert.assertEquals(SoapResponseStrategy.RANDOM, soapOperation.getResponseStrategy());
        Assert.assertEquals(SoapOperationStatus.MOCKED, soapOperation.getStatus());
        Assert.assertEquals(soapVersion, soapOperation.getSoapVersion());
        Assert.assertEquals(HttpMethod.POST, soapOperation.getHttpMethod());

        SoapOperationIdentifier operationIdentifier = soapOperation.getOperationIdentifier();

        Assert.assertNotNull(operationIdentifier);
        Assert.assertEquals("Request", operationIdentifier.getName());
        Assert.assertEquals("http://Services/ServiceExample/ServiceExample/1/Schema",
                operationIdentifier.getNamespace().orElse(null));
    }

    private List<File> loadFile(final String location) throws URISyntaxException {
        final URL resourceUrl = SoapPortConverterTest.class.getResource(location);
        final File file;
        if (resourceUrl != null) {
            file = new File(resourceUrl.toURI());
            return List.of(file);
        }
        return Collections.emptyList();
    }


}
