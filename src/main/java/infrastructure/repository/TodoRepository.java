package infrastructure.repository;

import domain.todo.Todo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@ApplicationScoped
public class TodoRepository {

    @PersistenceContext(name = "todos")
    private EntityManager em;

    private static final Logger LOG = LoggerFactory.getLogger("TodoRepository.class");

    public List<Todo> getAllTodos() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Todo> cq = cb.createQuery(Todo.class);
        Root<Todo> root = cq.from(Todo.class);
        cq.select(root);
        TypedQuery<Todo> query = em.createQuery(cq);
        List<Todo> todos = query.getResultList();

        if (todos == null) {
            LOG.warn("Found no todos");
        }else{
            LOG.info("Found {} todos", todos.size());
        }

        return todos;
    }

    public void persistTodo(final Todo todo) {
        em.persist(todo);
        LOG.info("Persisted todo with id {}", todo.getTodoId());
    }

    public Todo getTodo(final Integer todoId) {
        Todo todo = em.find(Todo.class, todoId);
        if (todo == null) {
            LOG.warn("Found no todo with Id", todoId);
        }else{
            LOG.info("Found todo with id {}", todoId);
        }
        return todo;
    }

    public void deleteTodo(final Todo todo) {
        em.remove(todo);
        LOG.info("Deleted todo with id {}", todo.getTodoId());
    }
}
