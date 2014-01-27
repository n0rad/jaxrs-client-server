/**
 *
 *     Copyright (C) norad.fr
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package fr.norad.jaxrs.client.server.resource.mapper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.cxf.endpoint.Server;
import org.junit.Test;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import fr.norad.core.io.PortFinder;
import fr.norad.jaxrs.client.server.rest.RestBuilder;


public class JaxrsDocAwareExceptionMapperTest {

    private String url = "http://localhost:" + PortFinder.randomAvailable();
    private Server server;
    private Resource client;

    @Test(expected = IllegalStateException.class)
    public void should_get_token() throws Exception {
        client.getSomething();
    }

    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    interface Resource {
        @GET
        public void getSomething();
    }

    public class ResourceImpl implements Resource {
        @Override
        public void getSomething() {
            throw new IllegalStateException("blocked");
        }
    }

    {
        RestBuilder builder = new RestBuilder();
        builder.addProvider(new JaxrsDocAwareExceptionMapper());
        builder.addProvider(new JacksonJaxbJsonProvider());
        builder.addProvider(new GenericResponseExceptionMapper(new JacksonJaxbJsonProvider()));
        server = builder.buildServer(url, new ResourceImpl());
        client = builder.buildClient(Resource.class, url);
    }

}