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
package fr.norad.jaxrs.client.server.resource.mapper.jaxrsdoc;


import static fr.norad.jaxrs.client.server.resource.mapper.ExceptionMapperUtils.findMediaType;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fr.norad.core.lang.reflect.AnnotationUtils;
import fr.norad.jaxrs.client.server.resource.Error;
import fr.norad.jaxrs.client.server.resource.mapper.ExceptionMapperUtils;
import fr.norad.jaxrs.oauth2.HttpStatus;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class JaxrsDocAwareExceptionMapper implements ExceptionMapper<Exception> {

    private Logger log = LoggerFactory.getLogger(getClass());
    private boolean hideExceptionClass = false;
    private boolean hideMessage = false;
    private boolean logRuntimeError = true;
    private boolean logCheckedError = false;
    private int defaultCheckedExceptionHttpCode = 400;
    private int defaultRuntimeExceptionHttpCode = 500;

    @Override
    public Response toResponse(Exception exception) {
        logError(exception);

        Error error;
        int httpCode = isRuntimeClass(exception.getClass()) ?
                defaultRuntimeExceptionHttpCode : defaultCheckedExceptionHttpCode;
        try {
            Integer code = findCode(exception);
            if (code != null) {
                httpCode = code;
            }

            error = buildError(exception);
            if (hideExceptionClass) {
                error.setException(null);
            }
            if (hideMessage) {
                error.setMessage(null);
            }
        } catch (Exception e) {
            httpCode = 500;
            error = new Error(e);
            logError(e);
        }

        return Response.status(httpCode).entity(error).type(findMediaType()).build();
    }

    private void logError(Exception exception) {
        if (RuntimeException.class.isAssignableFrom(exception.getClass())) {
            if (logRuntimeError) {
                log.error("Technical exception", exception);
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Respond error", exception);
            } else if (logCheckedError) {
                log.info("Respond error : {}", exception.getMessage());
            }
        }
    }

    public Error buildError(Exception exception) throws Exception {
        return ExceptionMapperUtils.buildError(exception);
    }

    public Integer findCode(Exception exception) throws Exception {
        Integer code = null;
        HttpStatus status = AnnotationUtils.findAnnotation(exception.getClass(), HttpStatus.class);
        if (status != null) {
            code = status.value().getStatusCode();
        } else if (exception.getClass().equals(NotFoundException.class)) {
            code = Response.Status.NOT_FOUND.getStatusCode();
        }
        return code;
    }

    public static boolean isRuntimeClass(Class<? extends Exception> e) {
        return RuntimeException.class.isAssignableFrom(e);
    }
}
