package marten.aoe.data.units;

public enum Units {
    Worker("Worker"), BASE("Base");

    private String title;

    private Units(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }
}
