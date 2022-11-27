package com;

import com.security.JWebToken;

public class TokenManager {
    public JWebToken check(String token){
        JWebToken tk = null;
        if(token==null)
            return null;
        try {
            tk = new JWebToken(token);
        } catch (Exception e) {
            return null;
        }
        try {
            if (!tk.isValid()) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
        return tk;
    }
}
