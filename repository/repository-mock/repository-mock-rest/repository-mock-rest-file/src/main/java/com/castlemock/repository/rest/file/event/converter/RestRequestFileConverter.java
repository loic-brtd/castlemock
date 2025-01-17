/*
 * Copyright 2024 Karl Dahlgren
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

package com.castlemock.repository.rest.file.event.converter;

import com.castlemock.model.mock.rest.domain.RestRequest;
import com.castlemock.repository.core.file.http.converter.HttpHeaderFileConverter;
import com.castlemock.repository.core.file.http.converter.HttpParameterFileConverter;
import com.castlemock.repository.rest.file.event.model.RestRequestFile;

import java.util.stream.Collectors;

public final class RestRequestFileConverter {

    private RestRequestFileConverter() {

    }

    public static RestRequest toRestRequest(final RestRequestFile restRequest) {
        return RestRequest.builder()
                .body(restRequest.getBody())
                .contentType(restRequest.getContentType())
                .uri(restRequest.getUri())
                .httpMethod(restRequest.getHttpMethod())
                .httpHeaders(restRequest.getHttpHeaders()
                        .stream()
                        .map(HttpHeaderFileConverter::toHttpHeader)
                        .collect(Collectors.toList()))
                .httpParameters(restRequest.getHttpParameters()
                        .stream()
                        .map(HttpParameterFileConverter::toHttpParameter)
                        .collect(Collectors.toList()))
                .build();
    }

}
