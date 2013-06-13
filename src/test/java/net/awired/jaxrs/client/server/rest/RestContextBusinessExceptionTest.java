package net.awired.jaxrs.client.server.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import net.awired.core.lang.exception.NotFoundException;
import net.awired.jaxrs.client.server.rest.RestBuilder;
import net.awired.jaxrs.client.server.rest.RestSession;
import org.junit.Test;

public class RestContextBusinessExceptionTest {

    private String url = "http://127.0.0.1:8080";
    private RestBuilder context = new RestBuilder();

    public RestContextBusinessExceptionTest() {
        context.withExceptionMapper();
    }

    @Path("/")
    private interface UsersResource {
        @GET
        String getUser() throws NotFoundException;
    }

    public class UsersService implements UsersResource {
        @Override
        public String getUser() throws NotFoundException {
            throw new NotFoundException("not found");
        }
    }

    @Test(expected = NotFoundException.class)
    public void should_receive_custom_exception_in_json() throws Exception {
        context.buildServer(url, new UsersService());
        UsersResource resource = context.buildClient(UsersResource.class, url);

        resource.getUser();
    }

    @Test(expected = NotFoundException.class)
    public void should_receive_custom_exception_in_xml() throws Exception {
        context.buildServer(url, new UsersService());
        UsersResource resource = context.buildClient(UsersResource.class, url, new RestSession().asXml());

        resource.getUser();
    }

}
