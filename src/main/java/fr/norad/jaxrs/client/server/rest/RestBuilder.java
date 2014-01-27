/**
 *
 *     Copyright (C) norad.fr
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package fr.norad.jaxrs.client.server.rest;

import static java.util.Arrays.asList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.HttpHeaders;
import org.apache.cxf.binding.BindingFactoryManager;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.AbstractJAXRSFactoryBean;
import org.apache.cxf.jaxrs.JAXRSBindingFactory;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.Client;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.HTTPConduit;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * context.addProvider(new JaxbAnnotationIntrospector(TypeFactory.defaultInstance()));
 * context.addProvider(new JaxrsDocAwareExceptionMapper());
 * context.addProvider(new JacksonJaxbJsonProvider());
 * context.addProvider(new GenericResponseExceptionMapper(new JacksonJaxbJsonProvider()));
 * context.addProvider(new WebApplicationExceptionMapper());
 */
public class RestBuilder {

    public static final TrustManager[] TRUST_ALL_CERTS = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
    };
    private final List<Object> providers = new ArrayList<>();
    private final List<Interceptor<? extends Message>> inInterceptors = new ArrayList<>();
    private final List<Interceptor<? extends Message>> outInterceptors = new ArrayList<>();
    private final List<Interceptor<? extends Message>> inFaultInterceptors = new ArrayList<>();
    private final List<Interceptor<? extends Message>> outFaultInterceptors = new ArrayList<>();
    private boolean threadSafe;
    private boolean trustAllCertificates;

    public static RestBuilder rest() {
        return new RestBuilder();
    }

    public RestBuilder addProvider(Object provider) {
        this.providers.add(provider);
        return this;
    }

    public RestBuilder addAllProvider(Collection<Object> providers) {
        this.providers.addAll(providers);
        return this;
    }

    public RestBuilder addAllInInterceptor(Collection<Interceptor<? extends Message>> inInterceptors) {
        this.inInterceptors.addAll(inInterceptors);
        return this;
    }

    public RestBuilder addAllInFaultInterceptor(Collection<Interceptor<? extends Message>> inInterceptors) {
        this.inFaultInterceptors.addAll(inInterceptors);
        return this;
    }

    public RestBuilder addAllOutInterceptor(Collection<Interceptor<? extends Message>> outInterceptors) {
        this.outInterceptors.addAll(outInterceptors);
        return this;
    }

    public RestBuilder addAllOutFaultInterceptor(Collection<Interceptor<? extends Message>> outInterceptors) {
        this.outFaultInterceptors.addAll(outInterceptors);
        return this;
    }

    public RestBuilder addInInterceptor(Interceptor<? extends Message> inInterceptor) {
        this.inInterceptors.add(inInterceptor);
        return this;
    }

    public RestBuilder addInFaultInterceptor(Interceptor<? extends Message> inInterceptor) {
        this.inFaultInterceptors.add(inInterceptor);
        return this;
    }

    public RestBuilder addOutInterceptor(Interceptor<? extends Message> outInterceptor) {
        this.outInterceptors.add(outInterceptor);
        return this;
    }

    public RestBuilder addOutFaultInterceptor(Interceptor<? extends Message> inInterceptor) {
        this.inInterceptors.add(inInterceptor);
        return this;
    }

    public <U> U buildClient(Class<U> clazz, String connectionUrl) {
        return buildClient(clazz, connectionUrl, new RestSession());
    }

    public <U> U buildClient(Class<U> clazz, String connectionUrl, RestSession<?, ?> session) {
        JAXRSClientFactoryBean cf = new JAXRSClientFactoryBean();
        cf.setThreadSafe(threadSafe);

        prepareFactory(connectionUrl, cf);
        cf.setResourceClass(clazz);
        BindingFactoryManager manager = cf.getBus().getExtension(BindingFactoryManager.class);
        JAXRSBindingFactory factory = new JAXRSBindingFactory();
        factory.setBus(cf.getBus());
        manager.registerBindingFactory(JAXRSBindingFactory.JAXRS_BINDING_ID, factory);
        U service = cf.create(clazz);
        if (session != null) {
            prepareClient(session, WebClient.client(service));
        }
        if (trustAllCertificates) {
            HTTPConduit conduit = WebClient.getConfig(WebClient.client(service)).getHttpConduit();
            TLSClientParameters params = conduit.getTlsClientParameters();
            if (params == null) {
                params = new TLSClientParameters();
                conduit.setTlsClientParameters(params);
            }
            params.setTrustManagers(TRUST_ALL_CERTS);
            params.setDisableCNCheck(true);
        }
        return service;
    }

    public Server buildServer(String listenUrl, Class<? extends Object> resourceClass) {
        List<Class<? extends Object>> classes = new ArrayList<>(1);
        classes.add(resourceClass);
        return buildServer(listenUrl, null, classes);
    }

    public Server buildServer(String listenUrl, Object resource) {
        return buildServer(listenUrl, asList(resource), null);
    }

    public Server buildServer(String listenUrl, Collection<Object> resource) {
        return buildServer(listenUrl, resource, null);
    }

    public Server buildServer(String listenUrl, Class<? extends Object>... resourceClass) {
        return buildServer(listenUrl, null, asList(resourceClass));
    }

    public Server buildServer(String listenUrl, Collection resources, List<Class<? extends Object>> resourceClasses) {
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        prepareFactory(listenUrl, sf);

        if (resources != null) {
            sf.setServiceBeans(new ArrayList<Object>(resources));
        }
        if (resourceClasses != null) {
            sf.setResourceClasses(resourceClasses);
        }
        sf.setAddress(listenUrl);
        return sf.create();
    }

    private void prepareFactory(String address, AbstractJAXRSFactoryBean f) {
        f.setProviders(providers);

        f.getInInterceptors().addAll(inInterceptors);
        f.getInFaultInterceptors().addAll(inInterceptors);
        f.getOutInterceptors().addAll(outInterceptors);
        f.getOutFaultInterceptors().addAll(outFaultInterceptors);

        f.setAddress(address);
    }

    protected void prepareClient(RestSession<?, ?> session, Client client) {
        if (session.getAcceptType() != null) {
            client.accept(session.getAcceptType());
        }
        if (session.getContentType() != null) {
            client.type(session.getContentType());
        }
        if (session.getSessionId() != null) {
            client.header(HttpHeaders.COOKIE, "JSESSIONID=" + session.getSessionId());
            //        headers.put("Cookie2", "$Version=1");
        }
        //        if (session.getToken() != null) {
        //            client.header(HttpHeaders.AUTHORIZATION, "Bearer " + session.getToken().getAccessToken());
        //        }
        Map<String, String> headers = session.getHeaders();
        for (String key : headers.keySet()) {
            client.header(key, headers.get(key));
        }
    }

    public RestBuilder threadSafe(boolean threadSafe) {
        this.threadSafe = threadSafe;
        return this;
    }

    /////////////////////////////////////////

    public RestBuilder trustAllCertificates(boolean trustAllCertificates) {
        this.trustAllCertificates = trustAllCertificates;
        return this;
    }

    public List<Object> providers() {
        return providers;
    }

    ////////////////////////////////////////////

    public boolean threadSafe() {
        return threadSafe;
    }

    public boolean trustAllCertificates() {
        return trustAllCertificates;
    }

    public List<Interceptor<? extends Message>> inInterceptors() {
        return inInterceptors;
    }

    public List<Interceptor<? extends Message>> inFaultInterceptors() {
        return inFaultInterceptors;
    }

    public List<Interceptor<? extends Message>> outInterceptors() {
        return outInterceptors;
    }

    public List<Interceptor<? extends Message>> outFaultInterceptors() {
        return outFaultInterceptors;
    }

    @Data
    @Accessors(fluent = true)
    public static class Generic {

        public static final LoggingInInterceptor inStdoutLogger;
        public static final LoggingInInterceptor inStderrLogger;
        public static final LoggingOutInterceptor outStdoutLogger;
        public static final LoggingOutInterceptor outStderrLogger;

        static {
            inStdoutLogger = new LoggingInInterceptor();
            inStdoutLogger.setPrettyLogging(true);
            inStdoutLogger.setOutputLocation("<stdout>");
            inStderrLogger = new LoggingInInterceptor();
            inStderrLogger.setPrettyLogging(true);
            inStderrLogger.setOutputLocation("<stderr>");
            outStdoutLogger = new LoggingOutInterceptor();
            outStdoutLogger.setPrettyLogging(true);
            outStdoutLogger.setOutputLocation("<stdout>");
            outStderrLogger = new LoggingOutInterceptor();
            outStderrLogger.setPrettyLogging(true);
            outStderrLogger.setOutputLocation("<stderr>");
        }

    }

}