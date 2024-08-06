package websocket.commands;

public class LeaveCommand extends UserGameCommand{
    public LeaveCommand(CommandType type, String token, Integer id) {
        super(type, token, id);
    }
}
