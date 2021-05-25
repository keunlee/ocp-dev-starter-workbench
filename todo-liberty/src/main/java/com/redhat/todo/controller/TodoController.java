package com.redhat.todo.controller;

import com.redhat.todo.dao.TodoDao;
import com.redhat.todo.domain.Todo;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/todo")
@ApplicationScoped
public class TodoController {
    @Inject
    private TodoDao todoDao;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces( MediaType.APPLICATION_JSON )
    @Transactional
    public Response addOne(Todo todo) {
        todo = todoDao.createUpdateTodo(todo);
        return Response.status(Response.Status.OK).entity(todo).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Todo> todos = todoDao.findAll();
        return Response.status(Response.Status.OK).entity(todos).build();
    }

    @DELETE
    @Path("{id}")
    @Produces( MediaType.APPLICATION_JSON )
    @Transactional
    public Response deleteOne( @PathParam("id") int id ) {
        Todo todo = todoDao.findById(id);
        if ( todo != null) {
            todoDao.delete(todo);
        }
        return Response.status(Response.Status.OK).entity("entry deleted").build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces( MediaType.APPLICATION_JSON )
    @Transactional
    public Response updateOne(Todo item) {
        Todo entity = todoDao.findById(item.getId());
        if ( entity != null) {
            entity.setCompleted(item.isCompleted());
            entity.setId(item.getId());
            entity.setOrder(item.getOrder());
            entity.setTitle(item.getTitle());
            entity = todoDao.createUpdateTodo(entity);
        }

        return Response.status(Response.Status.OK).entity(entity).build();
    }

}
