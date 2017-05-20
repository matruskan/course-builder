/*
 * The MIT License
 *
 * Copyright 2017 matruskan.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package matruskan.boundary;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import matruskan.control.Users;
import matruskan.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * I won't bother with sessions, the current user will be taken from a JWT token
 * or something else (later).
 * 
 * For now, it will only return a custom admin user.
 * 
 * @author matruskan
 */
@RequestScoped
public class RequestUser {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestUser.class);
    
    // Oh, this guy will bring us some cookies! :9
    @Context
    private HttpServletRequest httpServletRequest;
    
    @Inject
    private Users users;
    
    @Produces
    @RequestScoped
    public User getUserFromRequest() {
        // we should've gotten the user ID from the request, from a token,
        // from the single sign-on server, but we'll leave that for later.
        User user = users.getUser();
        LOGGER.info("User that made the request: {}", user);
        return user;
    }
    
}
