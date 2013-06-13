package net.awired.ajsl.ws.resource.mapper.generic;

import static net.awired.ajsl.ws.resource.mapper.ExceptionMapperUtils.findMediaType;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import net.awired.ajsl.ws.resource.mapper.ExceptionMapperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
