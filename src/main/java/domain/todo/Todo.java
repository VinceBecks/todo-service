package domain.todo;

import org.apache.commons.lang3.Validate;

import javax.persistence.*;

@Entity
@Table(name = "TAB_TODO")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TODO_ID")
    private Integer todoId;

    @Embedded
    @AttributeOverride(name="value", column = @Column(name = "TITLE", nullable = false))
    private Title title;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "DESCRIPTION"))
    private Description description;

    @Enumerated
    @Column(name = "STATE")
    private TodoState state;

    private Todo(){

    }

    public Integer getTodoId() {
        return todoId;
    }

    public void setTodoId(Integer todoId) {
        this.todoId = todoId;
    }

    public Title getTitle() {
        return new Title(this.title.getValue());
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public Description getDescription() {
        return new Description(this.description.getValue());
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public TodoState getState() {
        return state;
    }

    public void setState(TodoState state) {
        this.state = state;
    }

    public static Builder newTodo() {
        return new Builder();
    }

    public static class Builder {
        private Todo todo;

        public Builder() {
            this.todo = new Todo();
        }

        public Builder withId(Integer id) {
            todo.setTodoId(id);
            return this;
        }

        public Builder withTitle(Title title) {
            todo.setTitle(title);
            return this;
        }

        public Builder withDescription(Description description) {
            todo.setDescription(description);
            return this;
        }

        public Builder withState(TodoState state) {
            todo.setState(state);
            return this;
        }

        public Todo build() {
            Validate.notNull(todo.getTitle());
            Validate.notNull(todo.getDescription());
            Validate.notNull(todo.getState());
            Todo returnTodo = todo;
            todo = new Todo();
            return returnTodo;
        }


    }
}
