package domain.todo;

import javax.persistence.Embeddable;

@Embeddable
public class Description {

    private String value;

    public Description() {

    }

    public Description(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
