package websocket.commands;

public class ConnectCommand extends UserGameCommand{
    public ConnectCommand(CommandType type, String token, Integer id) {
        super(type, token, id);
    }
}
