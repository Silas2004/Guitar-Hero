package controller;

import java.util.LinkedList;
import java.util.Queue;


public class CommandExecutor {
    private final Queue<Command> commandQueue;
    private final boolean enableQueue;
    
    public CommandExecutor(boolean enableQueue) {
        this.enableQueue = enableQueue;
        this.commandQueue = enableQueue ? new LinkedList<>() : null;
    }
    
    public CommandExecutor() {
        this(false);
    }
    
    // Start Command or add it on top of the Queue
    public void execute(Command command) {
        if (command == null) {
            return;
        }
        
        if (enableQueue) {
            commandQueue.offer(command);
        } else {
            command.execute();
        }
    }
    
    public void processQueue() {
        if (!enableQueue || commandQueue == null) {
            return;
        }
        
        while (!commandQueue.isEmpty()) {
            Command cmd = commandQueue.poll();
            if (cmd != null) {
                cmd.execute();
            }
        }
    }
    
    // Clear Queue
    public void clearQueue() {
        if (commandQueue != null) {
            commandQueue.clear();
        }
    }
    
    // Queue Size
    public int getQueueSize() {
        return (commandQueue != null) ? commandQueue.size() : 0;
    }
}
