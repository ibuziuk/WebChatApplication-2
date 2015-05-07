package chat.app.model;

import java.util.UUID;

public class Message
{
    private String username;
    private String text;
    private String id;
    public Message()
    {
    }
    public Message(String username, String text)
    {
        this.username=username;
        this.text=text;
        this.id=CreateId();
    }
    public Message(String username, String text, String id)
    {

        this.username=username;
        this.text=text;
        this.id=id;
    }
    private String CreateId()
    {

        String ID = UUID.randomUUID().toString();
        return ID;
    }
    public String getUsername()
    {
        return username;
    }
    public String getText()
    {
        return text;
    }
    public String getId()
    {
        return id;
    }
    public void setUsername(String username)
    {
        this.username=username;
    }
    public void setText(String text)
    {
        this.text=text;
    }
    public void setId(String id)
    {
        this.id=id;
    }
}
