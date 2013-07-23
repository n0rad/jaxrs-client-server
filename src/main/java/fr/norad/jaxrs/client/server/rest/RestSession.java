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

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import lombok.Data;

@Data
public class RestSession<SESSION extends RestSession<SESSION, CLIENT>, CLIENT extends RestClient> {

    private String sessionId;
    private MediaType contentType;
    private MediaType acceptType;
    //    private Token token;
    private CLIENT client;
    private Map<String, String> headers = new HashMap<>();

    public SESSION asJson() {
        contentType = MediaType.APPLICATION_JSON_TYPE;
        acceptType = MediaType.APPLICATION_JSON_TYPE;
        return (SESSION) this;
    }

    public SESSION asXml() {
        contentType = MediaType.APPLICATION_XML_TYPE;
        acceptType = MediaType.APPLICATION_XML_TYPE;
        return (SESSION) this;
    }

    public SESSION header(String name, String value) {
        headers.put(name, value);
        return (SESSION) this;
    }

    public SESSION client(CLIENT client) {
        this.client = client;
        return (SESSION) this;
    }

}
