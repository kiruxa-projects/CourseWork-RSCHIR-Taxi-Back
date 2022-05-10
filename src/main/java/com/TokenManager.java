package com;

import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
