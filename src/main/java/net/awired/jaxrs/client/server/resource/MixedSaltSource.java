package net.awired.jaxrs.client.server.resource;

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
