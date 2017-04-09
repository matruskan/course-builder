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

import java.util.Arrays;
import java.util.HashSet;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import matruskan.entity.Role;
import matruskan.entity.User;

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
    
    // Oh, this guy will bring us some cookies! :9
    @Context
    private HttpServletRequest httpServletRequest;
    
    @Produces
    @RequestScoped
    public User getUserFromJwt() {
        User user = new User("Admin");
        user.setId(1L);
        user.setRoles(new HashSet<>(Arrays.asList(new Role("admin"))));
        return user;
    }
    
}
