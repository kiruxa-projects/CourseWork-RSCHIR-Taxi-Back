package com;

import com.security.JWebToken;

public class TokenManager {
    public JWebToken check(String token){
        JWebToken tk = null;
        if(token==null)
            return null;
        String[] parts = token.split(" ");
        try {
            tk = new JWebToken(parts[1]);
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
