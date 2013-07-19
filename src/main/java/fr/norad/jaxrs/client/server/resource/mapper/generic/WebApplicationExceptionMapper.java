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

import static fr.norad.jaxrs.client.server.resource.mapper.ExceptionMapperUtils.findMediaType;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fr.norad.jaxrs.client.server.resource.mapper.ExceptionMapperUtils;

public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public Response toResponse(WebApplicationException ex) {
        if (ex.getClass().isAssignableFrom(ServerErrorException.class)) {
            log.warn("Respond Web Server Error Exception", ex);
        } else if (log.isDebugEnabled()) {
            log.debug("Respond Web Exception", ex);
        } else {
            log.info("Respond Web Exception : {}", ex.getMessage());
        }

        ResponseBuilder responseBuilder = null;
        if (ex.getResponse() == null) {
            responseBuilder = Response.serverError();
        } else {
            responseBuilder = Response.fromResponse(ex.getResponse()); //TODO handle javax.ws.rs.ValidationException
        }
        return responseBuilder.entity(ExceptionMapperUtils.buildError(ex)).type(findMediaType()).build();
    }
}
