package alekssey7227.mygpt.openai;

/**
 * @author Aleksey Shulikov
 */
public enum ROLES {
    USER ("user"),
    ASSISTANT ("assistant"),
    SYSTEM ("system");

    private final String name;

    ROLES(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ROLE{" +
                "code='" + name + '\'' +
                '}';
    }
}
