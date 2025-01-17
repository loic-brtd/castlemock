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

package com.castlemock.service.mock.soap.project.output;

import com.castlemock.model.core.Output;
import com.castlemock.model.mock.soap.domain.SoapPort;

import java.util.Optional;

/**
 * @author Karl Dahlgren
 * @since 1.0
 */
public final class DeleteSoapPortOutput implements Output {

    private final SoapPort port;

    private DeleteSoapPortOutput(final Builder builder){
        this.port = builder.port;
    }

    public Optional<SoapPort> getPort() {
        return Optional.ofNullable(port);
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder {

        private SoapPort port;

        private Builder(){

        }

        public Builder port(final SoapPort port){
            this.port = port;
            return this;
        }

        public DeleteSoapPortOutput build(){
            return new DeleteSoapPortOutput(this);
        }
    }

}
