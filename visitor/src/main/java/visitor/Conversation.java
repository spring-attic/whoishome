package visitor;

class Conversation {

    private String content;

    Conversation(String initialContent) {
        this.content = initialContent;
    }

    void addContent(String text) {
        content += "<br/>" + text;
    }

    public String getContent() {
        return "<h1>Conversation:<br/><small>" + content + "</small></h1>";
    }

}