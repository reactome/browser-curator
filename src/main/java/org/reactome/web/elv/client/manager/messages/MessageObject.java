package org.reactome.web.elv.client.manager.messages;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public class MessageObject{
    private String message;
    private Class clazz;
    private MessageType msgType;

    public MessageObject(String msg, Class clazz, MessageType msgType){
        this.message = msg;
        this.clazz = clazz;
        this.msgType = msgType;
    }

    public String getMessage(){
        return message;
    }

    public Class getClazz(){
        return clazz;
    }

    public MessageType getMsgType(){
        return msgType;
    }

}
