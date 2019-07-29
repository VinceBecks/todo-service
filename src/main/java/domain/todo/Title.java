package domain.todo;

import javax.persistence.Embeddable;

@Embeddable
public class Title {

    private String value;

    public Title() {

    }

    public Title(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
