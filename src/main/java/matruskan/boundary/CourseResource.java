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

import java.net.URI;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import matruskan.control.Courses;
import matruskan.control.exceptions.AccessDeniedException;
import matruskan.entity.Course;
import matruskan.entity.User;

/**
 *
 * @author matruskan
 */
@Path("/v1/course")
public class CourseResource {

    @Inject
    private Courses courses;
    @Inject
    private User creator;

    @GET
    @Path("/all")
    public List<Course> getCourses(@QueryParam("name") String name, @QueryParam("orderBy") String orderBy, @QueryParam("page") int page, @QueryParam("pageSize") int pageSize) {
        if (orderBy == null || orderBy.isEmpty()) {
            orderBy = "id";
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }
        return courses.list(creator, name, orderBy, page, pageSize);
    }

    @POST
    @Path("/new")
    public Response createNew(Course course) {
        course.setCreator(creator);
        return Response.created(URI.create(courses.create(course).toString())).build();
    }
    
    @GET
    @Path("{courseId}")
    public Response getCourse(@PathParam("courseId") Long id) {
        try {
            return Response.ok(courses.getCourseById(creator, id)).build();
        } catch (AccessDeniedException ex) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
}