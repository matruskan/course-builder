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

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import matruskan.control.exceptions.AccessDeniedException;
import matruskan.entity.Course;
import matruskan.entity.User;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author matruskan
 */
public class CoursesTest {
    
    public CoursesTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of list method, of class Courses.
     */
    @Test
    public void testList() throws Exception {
        System.out.println("list");
        User user = new User();
        user.setId(1l);
        String nameFilter = "";
        String orderBy = "id";
        int page = 0;
        int pageSize = 10;
        List<Course> expResult = new ArrayList<>();
        Courses instance = new Courses();
        instance.em = mock(EntityManager.class);
        TypedQuery tq = mock(TypedQuery.class);
        doReturn(tq)
                .when(instance.em).createNamedQuery(any(), any());
        doReturn(tq)
                .when(tq).setParameter(anyString(), anyString());
        doReturn(tq)
                .when(tq).setParameter(anyString(), eq(user));
        doReturn(tq)
                .when(tq).setFirstResult(anyInt());
        doReturn(tq)
                .when(tq).setMaxResults(anyInt());
        doReturn(expResult)
                .when(tq).getResultList();
        
        List<Course> result = instance.list(user, nameFilter, orderBy, page, pageSize);
        assertEquals(expResult, result);
    }

    /**
     * Test of create method, of class Courses.
     */
    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
        Course course = new Course();
        User creator = new User();
        creator.setId(1l);
        Courses instance = new Courses();
        instance.em = mock(EntityManager.class);
        doAnswer(invocation -> {
            Course courseToMerge = ((Course)invocation.getArguments()[0]);
            courseToMerge.setId(1l);
            return courseToMerge;
        }).when(instance.em).merge(eq(course));
        doReturn(creator).when(instance.em).find(eq(User.class), eq(creator.getId()));
        Long expResult = 1l;
        Long result = instance.save(creator, course);
        assertEquals(expResult, result);
    }

    /**
     * Test of getCourseById method, of class Courses.
     */
    @Test
    public void testGetCourseById() throws Exception {
        System.out.println("getCourseById");
        User creator = new User();
        Course course = new Course();
        Long id = 1l;
        Courses instance = new Courses();
        instance.em = mock(EntityManager.class);
        instance.permissions = mock(Permissions.class);
        doReturn(course)
                .when(instance.em).find(eq(Course.class), eq(id));
        doReturn(true)
                .when(instance.permissions).checkCreatorAccess(eq(creator), eq(course));
        Course expResult = course;
        Course result = instance.getCourseById(creator, id);
        assertEquals(expResult, result);
    }

    /**
     * Test of getCourseById method, of class Courses.
     */
    @Test
    public void testGetCourseByIdNullWhenAccessDenied() throws Exception {
        System.out.println("getCourseByIdNullWhenAccessDenied");
        User creator = new User();
        Course course = new Course();
        Long id = 1l;
        Courses instance = new Courses();
        instance.em = mock(EntityManager.class);
        instance.permissions = mock(Permissions.class);
        doReturn(course)
                .when(instance.em).find(eq(Course.class), eq(id));
        doReturn(false)
                .when(instance.permissions).checkCreatorAccess(eq(creator), eq(course));
        try {
            Course result = instance.getCourseById(creator, id);
            fail("Should have thrown an AccessDeniedException");
        } catch (AccessDeniedException ex) {
            assertEquals(AccessDeniedException.REASON_USER_IS_NOT_CREATOR, ex.getMessage());
        }
        
    }
    
}
