package com.jec.base.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by jeremyliu on 5/23/16.
 */
@XStreamAlias("Message")
public class XmlMessage<T> {
    @XStreamAlias("MessageHead")
    private MessageHead messageHead;
    @XStreamAlias("Content")
    private T content;

    public MessageHead getMessageHead() {
        return messageHead;
    }

    public void setMessageHead(MessageHead messageHead) {
        this.messageHead = messageHead;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}

