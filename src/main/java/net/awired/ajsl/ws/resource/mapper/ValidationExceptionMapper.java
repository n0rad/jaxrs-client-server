package net.awired.ajsl.ws.resource.mapper;

import static net.awired.ajsl.ws.resource.mapper.ExceptionMapperUtils.buildError;
import static net.awired.ajsl.ws.resource.mapper.ExceptionMapperUtils.findMediaType;
import javax.validation.ValidationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO should be replace by javax.ws.rs.ValidationException processing
 */
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String RESPOND = "Respond ValidationException";

    @Override
    public Response toResponse(ValidationException exception) {
        if (log.isDebugEnabled()) {
            log.debug(RESPOND, exception);
        } else {
            log.info(RESPOND, exception.getMessage());
        }
        return Response.status(Status.BAD_REQUEST).entity(buildError(exception)).type(findMediaType()).build();
    }
}
