/**
 *
 *     Copyright (C) Awired.net
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
package fr.norad.jaxrs.client.server.resource;

import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.core.userdetails.UserDetails;

public class MixedSaltSource implements SaltSource {

    private SaltSource source1;
    private SaltSource source2;

    @Override
    public Object getSalt(UserDetails user) {
        String res = "";
        Object salt = source1.getSalt(user);
        if (salt != null) {
            res += salt;
        }
        Object salt2 = source2.getSalt(user);
        if (salt2 != null) {
            res += salt2;
        }
        return res;
    }

    public void setSource1(SaltSource source1) {
        this.source1 = source1;
    }

    public void setSource2(SaltSource source2) {
        this.source2 = source2;
    }

}
