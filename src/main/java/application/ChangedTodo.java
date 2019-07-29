package application;

import domain.todo.TodoState;

import java.io.Serializable;

public class ChangedTodo implements Serializable{

    private String title;
    private String description;
    private TodoState state;

    public ChangedTodo(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TodoState getState() {
        return state;
    }

    public void setState(TodoState state) {
        this.state = state;
    }
}
