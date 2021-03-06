package serviscepde.com.tr.Models;

public class MarkaModel {
    private String id;
    private String name;
    private String parentId;

    public MarkaModel(String id, String name, String parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getParentId() {
        return parentId;
    }
}
