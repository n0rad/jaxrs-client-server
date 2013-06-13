package net.awired.jaxrs.client.server.rest;

public class RestClient {

    private String name;

    public RestClient(String name, String secret) {
        this.name = name;
        this.secret = secret;
    }

    private String secret;

    public RestClient() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

}
