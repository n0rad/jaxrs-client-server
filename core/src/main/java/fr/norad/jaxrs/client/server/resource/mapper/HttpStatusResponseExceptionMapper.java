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

import fr.norad.core.lang.reflect.AnnotationUtils;
import fr.norad.jaxrs.client.server.api.HttpStatus;
import org.apache.cxf.jaxrs.client.ResponseExceptionMapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

public class HttpStatusResponseExceptionMapper implements ResponseExceptionMapper<Exception> {


    private List<Class<? extends Exception>> exceptionClasses;

    public HttpStatusResponseExceptionMapper(List<Class<? extends Exception>> exceptionClasses) {
        this.exceptionClasses = exceptionClasses;
    }

    @Override
    public Exception fromResponse(Response r) {
        Status responseStatus = Status.fromStatusCode(r.getStatus());
        for (Class<?> exception : exceptionClasses) {
            HttpStatus status = AnnotationUtils.findAnnotation(exception, HttpStatus.class);
            if (status != null && responseStatus.equals(status.value())) {
                Error error = new Error();
                error.setException((Class<? extends Exception>) exception);
                return ExceptionMapperUtils.buildException(r, error);
            }
        }
        return null;
    }

}
