package marten.game;

public class Return {
    private ParserState state = ParserState.SUCCESS;
    private String message = "";
    public Return() {        
    }
    public Return(ParserState newState) {
        this.state = newState;
    }
    public Return(ParserState newState, String newMessage) {
        this.message = newMessage;
    }
    public ParserState getState() {
        return this.state;
    }
    public String getMessage() {
        return this.message;
    }
}
