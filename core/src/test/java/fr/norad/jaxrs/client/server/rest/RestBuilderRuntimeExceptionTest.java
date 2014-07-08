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
package fr.norad.jaxrs.client.server.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.junit.Test;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import fr.norad.core.io.PortFinder;
import fr.norad.jaxrs.client.server.resource.mapper.ErrorResponseExceptionMapper;
import fr.norad.jaxrs.client.server.resource.mapper.ErrorExceptionMapper;

public class RestBuilderRuntimeExceptionTest {

    private String url = "http://localhost:" + PortFinder.randomAvailable();
    private RestBuilder context = new RestBuilder();

    public RestBuilderRuntimeExceptionTest() {
        context.addProvider(new JacksonJaxbJsonProvider());
        context.addProvider(new ErrorExceptionMapper());
        context.addProvider(new ErrorResponseExceptionMapper(new JacksonJaxbJsonProvider()));
        context.buildServer(url, new UsersService());
    }

    @Path("/")
    private interface UsersResource {
        @GET
        String getUser();
    }

    public class UsersService implements UsersResource {
        @Override
        public String getUser() {
            throw new SecurityException("security runtime !!");
        }
    }

    @Test(expected = SecurityException.class)
    public void should_receive_runtimeException_in_json() throws Exception {
        UsersResource resource = context.buildClient(UsersResource.class, url, new RestSession().asJson());

        resource.getUser();
    }

    @Test(expected = SecurityException.class)
    public void should_receive_runtimeException_in_xml() throws Exception {
        UsersResource resource = context.buildClient(UsersResource.class, url, new RestSession().asXml());

        resource.getUser();
    }

}
