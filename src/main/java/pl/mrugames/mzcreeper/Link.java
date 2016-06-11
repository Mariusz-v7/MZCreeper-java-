package pl.mrugames.mzcreeper;

public enum Link {
    MAIN_PAGE("http://www.managerzone.com/");

    private final String link;

    Link(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }
}
