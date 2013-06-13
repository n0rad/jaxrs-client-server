package net.awired.ajsl.ws.resource.mapper;

import static net.awired.ajsl.ws.resource.mapper.ExceptionMapperUtils.buildError;
import static net.awired.ajsl.ws.resource.mapper.ExceptionMapperUtils.findMediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public Response toResponse(NotFoundException exception) {
        if (log.isDebugEnabled()) {
            log.debug("Respond NotFoundException", exception);
        } else {
            log.info("Respond NotFoundException : {}", exception.getMessage());
        }
        return Response.status(Status.NOT_FOUND).entity(buildError(exception)).type(findMediaType()).build();
    }
}
