jaxrs-client-server a lib that completly hide the http stuff and allow communication with full java.
It's a wrapper around cxf client and server that simplify instanciation. 

Supported features :
- create server from java service class
- create client from java interface
- support interceptor
- support exception mappers
- support JSON and XML by default
- some more deeper stuff...

## Fast description

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

#### Create server

    new RestBuilder().buildServer("http://localhost:4242", new UsersService());
    
and in a shell :
    
    localhost ~ $ curl localhost:4242
    Hello!    
    localhost ~ $ 

#### Create client

    HelloResource helloResource = new RestBuilder().buildClient(HelloResource.class, "http://localhost:4242");
    String hello = helloResource.getHello();

# More advanced description on how to use it

You should be working with 3 seperated projects :
- api: containing the POJO payload, exceptions and JAX-RS interface 
- server: depending on the api and is implementing the JAX-RS interface
- client: also depending on the api and is using the interface to create the client proxy


