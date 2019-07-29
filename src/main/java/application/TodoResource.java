package application;

import domain.todo.Description;
import domain.todo.Title;
import domain.todo.Todo;
import domain.todo.TodoState;
import infrastructure.error.ErrorDTO;
import infrastructure.repository.TodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

@Path("todos")
public class TodoResource {

    //todo: Add Tests

    private static final Logger LOG = LoggerFactory.getLogger("TodoResource.class");

    @Inject
    private TodoRepository repository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTodos(@DefaultValue("all")@QueryParam("state") final String state) {
        LOG.info("Request to get a list of todos");
        List<TodoDTO> todoDTOS = new LinkedList<>();

        List<Todo> todos = repository.getAllTodos();
        if (todos == null) {
            LOG.warn("No todos persisted");
        }else {
            if (state.equals("all")){
                todos.forEach(todo -> todoDTOS.add(new TodoDTO(todo)));
            }else if (state.equals("open")){
                todos.forEach(todo -> {
                    if (todo.getState() == TodoState.OPEN){
                        todoDTOS.add(new TodoDTO(todo));
                    }
                });
            }else if (state.equals("inProgress")){
                todos.forEach(todo -> {
                    if (todo.getState() == TodoState.IN_PROGRESS){
                        todoDTOS.add(new TodoDTO(todo));
                    }
                });
            }else if (state.equals("done")){
                todos.forEach(todo -> {
                    if (todo.getState() == TodoState.DONE){
                        todoDTOS.add(new TodoDTO(todo));
                    }
                });
            }else {
                LOG.warn("Request contains wrong value for state");
                ErrorDTO dto = new ErrorDTO("Wrong value for Query Param " + state);
                return Response.status(Response.Status.BAD_REQUEST).entity(dto).build();
            }
        }

        LOG.info("Response with {} todos", todoDTOS.size());
        return Response.ok().entity(todoDTOS).build();
    }


    //todo: Add bean validation
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createTodo(final NewTodo newTodo) {
        LOG.info("Request to craete a new todo");
        Todo todo = Todo.newTodo()
                .withTitle(new Title(newTodo.getTitle()))
                .withDescription(new Description(newTodo.getDescription()))
                .withState(TodoState.OPEN)
                .build();

        repository.persistTodo(todo);

        TodoDTO dto = new TodoDTO(todo);
        LOG.info("Response with new created todo");
        return Response.status(Response.Status.CREATED).entity(dto).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTodo(@PathParam("id") final Integer todoId) {
        Todo todo = repository.getTodo(todoId);
        if (todo == null) {
            LOG.warn("Found no todo with id {}", todoId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }else {
            LOG.info("Response with todo");
            TodoDTO dto = new TodoDTO(todo);
            return Response.ok().entity(dto).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response changeTodo(@PathParam("id") final Integer todoId, final ChangedTodo change) {
        Todo todoToChange = repository.getTodo(todoId);
        if (todoToChange == null) {
            LOG.warn("Found no todo with id {}", todoId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }else {
            boolean changed = false;

            if (change.getTitle() != null && !change.getTitle().equals("") && !change.getTitle().equals(todoToChange.getTitle().getValue())) {
                todoToChange.setTitle(new Title(change.getTitle()));
                changed = true;
            }

            if (change.getDescription() != null && !change.getDescription().equals("") && !change.getDescription().equals(todoToChange.getDescription().getValue())) {
                todoToChange.setDescription(new Description(change.getDescription()));
                changed = true;
            }

            if (change.getState() != null && change.getState() != todoToChange.getState()) {
                todoToChange.setState(change.getState());
                changed = true;
            }

            if (changed) {
                return Response.status(Response.Status.NO_CONTENT).build();
            } else {
                ErrorDTO dto = new ErrorDTO("Nothing has changed");
                return Response.status(Response.Status.BAD_REQUEST).entity(dto).build();
            }
        }
    }


    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteTodo(@PathParam("id") final Integer todoId) {
        LOG.info("Request to delete the todo with id {}", todoId);
        Todo todoToDelete= repository.getTodo(todoId);
        if (todoToDelete == null) {
            LOG.warn("Found no todo with id {}", todoId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }else {
            repository.deleteTodo(todoToDelete);
            LOG.info("Deleted todo with id {}", todoId);
            return Response.status(Response.Status.NO_CONTENT).build();
        }
    }
}
