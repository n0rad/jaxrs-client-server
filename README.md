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
- Api: containing the POJO payloads, exceptions and JAX-RS interfaces 
- Server: depending on the api and is implementing the JAX-RS interfaces
- Client: also depending on the api and is using the interfaces to create the client proxy

The RestBuilder provide you : 
- in and out logger
- jackson json provider
- in and out interceptor
- generic server exception mapper with some default exceptions
- generic client exception mapper

With the client and server exception mapper, when a fault occur on the server side, here is what will be done for you :
- you throw an exception
- the *ExceptionMapper will catch it and will transform it into a http error with a specific payload containing message, class and code
- by default a runtime will be a 500 HTTP error and a checked will be a 400 HTTP error (you can choose the code per exception)
- the http error will be received by the client and processed in a specific part
- the ClientExceptionMapper will process the payload and recreate the exception
- the client will throw the exception that you can handle in your client's code


On top of the builder, you can use the RestSession that contains all config to communicate and contains :
- auto transfer of the sessionID
- content type of what will be sent (with help for json and xml)
- content type of what should be received (with help for json and xml)
- help to add headers
- current client for communication

The client is exactly what a client is in Oauth2 spec and contain client-id and client-secret




