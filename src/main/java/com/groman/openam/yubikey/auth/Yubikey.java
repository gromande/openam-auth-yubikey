package com.groman.openam.yubikey.auth;

import java.security.Principal;
import java.util.Map;
import java.util.ResourceBundle;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.login.LoginException;

import com.iplanet.dpro.session.service.InternalSession;
import com.iplanet.sso.SSOToken;
import com.iplanet.sso.SSOTokenManager;
import com.sun.identity.authentication.spi.AMLoginModule;
import com.sun.identity.authentication.spi.AuthLoginException;
import com.sun.identity.authentication.spi.InvalidPasswordException;
import com.sun.identity.shared.debug.Debug;

public class Yubikey extends AMLoginModule {
    private static final String BUNDLE_NAME = "amAuthYubikey";
    private static final String MODULE_NAME = "Yubikey";
    private static Debug debug = Debug.getInstance(MODULE_NAME);
    
    //Auth states
    private static final int LOGIN_START = 1;
    private static final int INTERNAL_ERROR = 2;
    
    private Map sharedState;
    private ResourceBundle bundle;
    private String UUID = null;
    private String userName = null;

    @Override
    public Principal getPrincipal() {
        if (UUID != null) {
            return new YubikeyPrincipal(UUID);
        }
        if (userName != null) {
            return new YubikeyPrincipal(userName);
        }
        return null;
    }

    @Override
    public void init(Subject subject, Map sharedState, Map options) {
        //get username from previous authentication
        try {
            userName = (String) sharedState.get(getUserKey());
            if (debug.messageEnabled()) {
                debug.message("init() : Got username from shared state: " + userName);
            }
            java.util.Locale locale = getLoginLocale();
            bundle = amCache.getResBundle(BUNDLE_NAME, locale);

            userName = (String) sharedState.get(getUserKey());
            if (userName == null || userName.isEmpty()) {
                //Session upgrade case. Need to find the user ID from the old session.
                SSOTokenManager mgr = SSOTokenManager.getInstance();
                InternalSession oldSession = getLoginState(MODULE_NAME).getOldSession();
                if (oldSession == null) {
                    throw new AuthLoginException(BUNDLE_NAME, "noOldSession", null);
                }
                SSOToken token = mgr.createSSOToken(oldSession.getID().toString());
                UUID = token.getPrincipal().getName();
                userName = token.getProperty("UserToken");
                if (debug.messageEnabled()) {
                    debug.message("init() : UserName from SSOToken=" + userName + "; UUID=" + UUID);
                }
            }
            this.sharedState = sharedState;
        } catch (Exception e) {
            debug.error("init() : Unable to get username", e);
        }
    }

    @Override
    public int process(Callback[] cbs, int currentState) throws LoginException {
        if (debug.messageEnabled()) {
            debug.message("process(): current state: " + currentState);
        }
        if (userName == null || userName.length() == 0) {
            throw new AuthLoginException(BUNDLE_NAME, "missingUserId", null);
        }
        debug.message("process(): got username: " + userName);
        throw new InvalidPasswordException(BUNDLE_NAME, "invalidOTP", null);
    }

}
