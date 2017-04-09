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

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import matruskan.control.exceptions.AccessDeniedException;
import matruskan.entity.Course;
import matruskan.entity.User;

/**
 *
 * @author matruskan
 */
@Stateless
public class Courses {

    @PersistenceContext(unitName = "coursebuilder-PU")
    private EntityManager em;
    
    @EJB
    private Permissions permissions;

    public List<Course> list(User user, String nameFilter, String orderBy, int page, int pageSize) {
        return em.createNamedQuery("list", Course.class)
                .setParameter("creator", user)
                .setParameter("name", "%" + nameFilter + "%")
                .setParameter("orderColumn", orderBy)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }
    
    public Long create(Course course) {
        em.persist(course);
        return course.getId();
    }
    
    public Course getCourseById(User creator, Long id) throws AccessDeniedException {
        Course course = em.find(Course.class, id);
        boolean hasAccess = permissions.checkCreatorAccess(creator, course);
        if (hasAccess) {
            return course;
        } else {
            throw new AccessDeniedException(AccessDeniedException.REASON_USER_IS_NOT_CREATOR);
        }
    }

}
