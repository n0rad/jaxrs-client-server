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
package fr.norad.jaxrs.client.server.resource.mapper.generic;

import static fr.norad.jaxrs.client.server.resource.mapper.ExceptionMapperUtils.buildError;
import static fr.norad.jaxrs.client.server.resource.mapper.ExceptionMapperUtils.findMediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fr.norad.jaxrs.client.server.resource.Error;

/**
 * catch exception and do a 400 as en {@link Error} this allow any exception to be thrown by the resource without
 * having to create a specific mapper for each business exception
 */
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public Response toResponse(Exception exception) {
        if (log.isDebugEnabled()) {
            log.debug("Respond Exception", exception);
        } else {
            log.info("Respond Exception : {}", exception.getMessage());
        }
        return Response.status(Status.BAD_REQUEST).entity(buildError(exception)).type(findMediaType()).build();
    }
}
