package application;

import domain.todo.Todo;
import domain.todo.TodoState;

import java.io.Serializable;

public class TodoDTO implements Serializable {
    private Integer todoId;
    private String title;
    private String description;
    private TodoState state;

    public TodoDTO(Todo todo){
        this.todoId = todo.getTodoId();
        this.title = todo.getTitle().getValue();
        this.description = todo.getDescription().getValue();
        this.state = todo.getState();
    }

    public Integer getTodoId() {
        return todoId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TodoState getState() {
        return state;
    }
}
