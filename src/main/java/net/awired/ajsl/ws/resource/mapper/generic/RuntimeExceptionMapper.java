package net.awired.ajsl.ws.resource.mapper.generic;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static net.awired.ajsl.ws.resource.mapper.ExceptionMapperUtils.buildError;
import static net.awired.ajsl.ws.resource.mapper.ExceptionMapperUtils.findMediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import net.awired.ajsl.ws.resource.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Catch runtime and do a 500 as an {@link Error}
 */
@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public Response toResponse(RuntimeException exception) {
        log.warn("Respond uncaught RuntimeException", exception);
        return Response.status(INTERNAL_SERVER_ERROR).entity(buildError(exception)).type(findMediaType()).build();
    }
}
