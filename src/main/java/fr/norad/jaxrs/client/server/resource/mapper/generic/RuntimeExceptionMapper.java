/**
 *
 *     Copyright (C) Awired.net
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
package fr.norad.jaxrs.client.server.resource.mapper.generic;

import static fr.norad.jaxrs.client.server.resource.mapper.ExceptionMapperUtils.buildError;
import static fr.norad.jaxrs.client.server.resource.mapper.ExceptionMapperUtils.findMediaType;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fr.norad.jaxrs.client.server.resource.Error;

/**
 * Catch runtime and do a 500 as an {@link Error}
 */
@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public Response toResponse(RuntimeException exception) {
        log.warn("Respond uncaught RuntimeException", exception);
        return Response.status(INTERNAL_SERVER_ERROR).entity(buildError(exception)).type(findMediaType()).build();
    }
}
