package net.awired.ajsl.ws.resource.mapper.generic;

import static net.awired.ajsl.ws.resource.mapper.ExceptionMapperUtils.buildError;
import static net.awired.ajsl.ws.resource.mapper.ExceptionMapperUtils.findMediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import net.awired.ajsl.ws.resource.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
