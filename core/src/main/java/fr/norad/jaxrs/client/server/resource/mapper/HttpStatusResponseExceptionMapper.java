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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.cxf.jaxrs.client.ResponseExceptionMapper;
import org.apache.cxf.message.Message;
import fr.norad.core.lang.reflect.AnnotationUtils;
import fr.norad.jaxrs.client.server.api.HttpStatus;

public class HttpStatusResponseExceptionMapper implements ResponseExceptionMapper<Exception> {
    @Override
    public Exception fromResponse(Response r) {
        Status responseStatus = Status.fromStatusCode(r.getStatus());
        Class<?>[] exceptions = findExceptions(r);
        for (Class<?> exception : exceptions) {
            HttpStatus status = AnnotationUtils.findAnnotation(exception, HttpStatus.class);
            if (responseStatus.equals(status.value())) {
                Error error = new Error();
                error.setException((Class<? extends Exception>) exception);
                return ExceptionMapperUtils.buildException(r, error);
            }
        }
        return null;
    }

    public Class<?>[] findExceptions(Response r) {
        try {
            Field f = r.getClass().getDeclaredField("responseMessage"); // I know
            f.setAccessible(true);
            Message message = (Message) f.get(r);
            Method method = message.getExchange().get(Method.class);
            return method.getExceptionTypes();
        } catch (NoSuchFieldException | IllegalAccessException e) {
        }
        return new Class<?>[0];
    }

}
