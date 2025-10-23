package controller;

public class CommandExecutor {
    public void execute(Command command) {
        command.execute();
    }

    public interface Command {
        void execute();
    }
}
