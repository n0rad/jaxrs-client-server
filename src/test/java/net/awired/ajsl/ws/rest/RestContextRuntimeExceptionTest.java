package net.awired.ajsl.ws.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.junit.Test;

public class RestContextRuntimeExceptionTest {

    private String url = "http://127.0.0.1:8080";
    private RestBuilder context = new RestBuilder();

    public RestContextRuntimeExceptionTest() {
        context.withExceptionMapper();
    }

    @Path("/")
    private interface UsersResource {
        @GET
        String getUser();
    }

    public class UsersService implements UsersResource {
        @Override
        public String getUser() {
            throw new SecurityException("security runtime !!");
        }
    }

    @Test(expected = SecurityException.class)
    public void should_receive_runtimeException_in_json() throws Exception {
        context.buildServer(url, new UsersService());
        UsersResource resource = context.buildClient(UsersResource.class, url);

        resource.getUser();
    }

    @Test(expected = SecurityException.class)
    public void should_receive_runtimeException_in_xml() throws Exception {
        context.buildServer(url, new UsersService());
        UsersResource resource = context.buildClient(UsersResource.class, url, new RestSession().asXml());

        resource.getUser();
    }

}
