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

package com.castlemock.service.mock.soap.project.input;

import com.castlemock.model.core.Input;

import java.util.Objects;

/**
 * @author Karl Dahlgren
 * @since 1.0
 */
public final class ReadSoapProjectInput implements Input {

    private final String projectId;

    private ReadSoapProjectInput(final Builder builder) {
        this.projectId = Objects.requireNonNull(builder.projectId, "projectId");
    }

    public String getProjectId() {
        return projectId;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder {
        private String projectId;

        private Builder(){

        }

        public Builder projectId(final String projectId){
            this.projectId = projectId;
            return this;
        }

        public ReadSoapProjectInput build(){
            return new ReadSoapProjectInput(this);
        }
    }
}
