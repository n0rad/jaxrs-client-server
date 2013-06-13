package net.awired.jaxrs.client.server.resource.mapper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import javax.ws.rs.core.MediaType;
import net.awired.jaxrs.client.server.resource.Error;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;

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
        List<MediaType> acceptContentType = JAXRSUtils.sortMediaTypes((String) msg.get(Message.ACCEPT_CONTENT_TYPE),
                null);
        for (MediaType mediaType : acceptContentType) {
            if (mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE)
                    || mediaType.isCompatible(MediaType.APPLICATION_XML_TYPE)) {
                return mediaType;
            }
        }
        return null;
    }
}
