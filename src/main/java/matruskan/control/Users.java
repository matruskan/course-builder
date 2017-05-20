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
package matruskan.control;

import java.util.Arrays;
import java.util.HashSet;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import matruskan.entity.Role;
import matruskan.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author matruskan
 */
@Stateless
public class Users {
    private static final Logger LOGGER = LoggerFactory.getLogger(Users.class);
    
    @PersistenceContext(unitName = "coursebuilder-PU")
    EntityManager em;
    
    public User getUser() {
        User user = em.find(User.class, 1l);
        if (user == null) {
            Role adminRole = new Role("admin");
            adminRole = em.merge(adminRole);
            LOGGER.info("Persisted adminRole: {}", adminRole);
            user = new User("admin");
            user.setId(1l);
            user.setRoles(new HashSet<>(Arrays.asList(adminRole)));
            user = em.merge(user);
            LOGGER.info("Persisted user: {}", user);
            em.flush();
        }
        return user;
    }
}
