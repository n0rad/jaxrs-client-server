package net.awired.jaxrs.client.server.resource.mapper;

import static net.awired.jaxrs.client.server.resource.mapper.ExceptionMapperUtils.buildError;
import static net.awired.jaxrs.client.server.resource.mapper.ExceptionMapperUtils.findMediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import net.awired.core.lang.exception.UpdateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class UpdateExceptionMapper implements ExceptionMapper<UpdateException> {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public Response toResponse(UpdateException exception) {
        if (log.isDebugEnabled()) {
            log.debug("Respond UpdateException", exception);
        } else {
            log.info("Respond UpdateException : {}", exception.getMessage());
        }
        return Response.status(Status.FORBIDDEN).entity(buildError(exception)).type(findMediaType()).build();
    }
}
