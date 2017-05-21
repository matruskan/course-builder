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

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import matruskan.control.Courses;
import matruskan.control.exceptions.AccessDeniedException;
import matruskan.entity.Course;
import matruskan.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author matruskan
 */
@RequestScoped
@Path("/v1/courses")
public class CourseResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(CourseResource.class);

    @EJB
    private Courses courses;
    @Inject
    private User creator;
    @Context
    private HttpServletRequest servletRequest;

    @GET
    @Path("/all")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Course> getCourses(
            @QueryParam("name") String name,
            @QueryParam("orderBy") String orderBy,
            @QueryParam("page") int page,
            @QueryParam("pageSize") int pageSize) {
        if (orderBy == null || orderBy.isEmpty()) {
            orderBy = "id";
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }
        if (name == null) {
            name = "";
        }
        List<Course> coursesPage = courses.list(creator, name, orderBy, page, pageSize);
        return coursesPage;
    }

    @POST
    @Path("/new")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response createNew(
            @FormParam("name") String name,
            @FormParam("subtitle") String subtitle,
            @FormParam("description") String description,
            @FormParam("duration") BigDecimal duration,
            @FormParam("price") BigDecimal price) {
        try {
            Course course = new Course();
            course.setName(name);
            course.setSubtitle(subtitle);
            course.setDescription(description);
            course.setPrice(price);
            course.setDuration(duration);
            Long courseSavedId = courses.save(creator, course);
            URI uriRedirect = getSavedURI(servletRequest, courseSavedId);
            return Response.seeOther(uriRedirect).build();
        } catch (MalformedURLException | URISyntaxException ex) {
            LOGGER.error("Error building URL", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error building URL").build();
        }
    }

    
    @POST
    @Path("/edit/{courseId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response edit(
            @PathParam("courseId") Long id,
            @FormParam("name") String name,
            @FormParam("subtitle") String subtitle,
            @FormParam("description") String description,
            @FormParam("duration") BigDecimal duration,
            @FormParam("price") BigDecimal price) {
        try {
            Course course = courses.getCourseById(creator, id);
            course.setName(name);
            course.setSubtitle(subtitle);
            course.setDescription(description);
            course.setPrice(price);
            course.setDuration(duration);
            Long courseSavedId = courses.save(creator, course);
            URI uriRedirect = getSavedURI(servletRequest, courseSavedId);
            return Response.seeOther(uriRedirect).build();
        } catch (AccessDeniedException ex) {
            return Response.status(Response.Status.FORBIDDEN).entity("Course belongs to another creator.").build();
        } catch (MalformedURLException | URISyntaxException ex) {
            LOGGER.error("Error building URL", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error building URL").build();
        }
    }
    
    @GET
    @Path("{courseId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getCourse(@PathParam("courseId") Long id) {
        try {
            return Response.ok(courses.getCourseById(creator, id)).build();
        } catch (AccessDeniedException ex) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
    
    private URI getSavedURI(HttpServletRequest httpServletRequest, Long courseId) throws MalformedURLException, URISyntaxException {
        URL requestURL = new URL(httpServletRequest.getRequestURL().toString());
        URI courseSavedUri = new URI(
                requestURL.getProtocol(),
                null,
                requestURL.getHost(),
                requestURL.getPort(),
                servletRequest.getContextPath() + "/saved.html",
                "id=" + courseId.toString(),
                null);

        return courseSavedUri;
    }
}
