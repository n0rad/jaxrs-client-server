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


import fr.norad.core.io.PortFinder;
import fr.norad.jaxrs.client.server.rest.RestBuilder;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import static java.util.Arrays.asList;

public class HttpStatusExceptionMapperTest {

    public static String url = "http://localhost:" + PortFinder.randomAvailable();

    RestBuilder builder = new RestBuilder()
            .addInFaultInterceptor(RestBuilder.Generic.inStderrLogger)
            .addInInterceptor(RestBuilder.Generic.inStdoutLogger)
            .addOutFaultInterceptor(RestBuilder.Generic.outStderrLogger)
            .addOutInterceptor(RestBuilder.Generic.outStdoutLogger)
            .addProvider(new HttpStatusExceptionMapper())
            .addProvider(new HttpStatusResponseExceptionMapper(asList(IllegalStateException.class, ForTestNotFoundException.class)));
    private final SimpleInterface resource;

    public HttpStatusExceptionMapperTest() {
        builder.buildServer(url, new SimpleResource());
        resource = builder.buildClient(SimpleInterface.class, url);
    }

    @Path("/")
    public interface SimpleInterface {
        @GET
        public void callSuccess();

        @GET
        public void callNotFound() throws ForTestNotFoundException;
    }

    public class SimpleResource implements SimpleInterface {
        public void callSuccess() {
        }

        public void callNotFound() throws ForTestNotFoundException {
            throw new ForTestNotFoundException("not found");
        }
    }

    @Test(expected = ForTestNotFoundException.class)
    public void should_support_http_status_error() throws Exception {
        resource.callNotFound();
    }
}
