
# Fast description

    @Path("/")
    private interface HelloResource {
        @GET
        String getHello();
    }

    public class HelloService implements HelloResource {
        @Override
        public String getHello() {
            return "Hello!";
        }
    }

## Create server

    new RestBuilder().buildServer("http://localhost:4242", new UsersService());
    
    localhost ~ $ curl localhost:4242
    Hello!    
    localhost ~ $ 

## Create client

    HelloResource helloResource = new RestBuilder().buildClient(HelloResource.class, "http://localhost:4242");
    String hello = helloResource.getHello();

# More advanced description

