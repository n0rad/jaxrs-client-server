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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import fr.norad.jaxrs.client.server.resource.Error;

public final class ExceptionMapperUtils {

    private ExceptionMapperUtils() {
    }

    public static Error buildError(Exception ex) {
        Error error = new Error();
        error.setMessage(ex.getMessage());
        error.setException(ex.getClass());
        return error;
    }

    public static String getStackTrace(Exception ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    public static MediaType findMediaType() {
        Message msg = PhaseInterceptorChain.getCurrentMessage();
        List<MediaType> acceptContentType = JAXRSUtils.sortMediaTypes((String) msg.get(Message.ACCEPT_CONTENT_TYPE));
        for (MediaType mediaType : acceptContentType) {
            if (mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE)
                    || mediaType.isCompatible(MediaType.APPLICATION_XML_TYPE)) {
                return mediaType;
            }
        }
        return null;
    }
}
