package com.jec.utils;

import com.jec.base.entity.MessageHead;
import com.jec.base.entity.XmlMessage;
import com.jec.protocol.command.Result;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.serializer.Serializer;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jeremyliu on 5/23/16.
 */
public class XmlUtils {

    public static Map<String, String> parseXml(HttpServletRequest request)
            throws Exception {
        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<String, String>();

        // 从request中取得输入流
        InputStream inputStream = request.getInputStream();
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();

        // 遍历所有子节点
        for (Element e : elementList)
            map.put(e.getName(), e.getText());

        // 释放资源
        inputStream.close();

        return map;
    }

    private static XStream xstream = new XStream(new XppDriver() {
        @Override
        public HierarchicalStreamWriter createWriter(Writer out) {
            return new PrettyPrintWriter(out) {
                // 对所有xml节点的转换都增加CDATA标记
                boolean cdata = true;

                @Override
                @SuppressWarnings("rawtypes")
                public void startNode(String name, Class clazz) {
                    super.startNode(name, clazz);
                }

                @Override
                protected void writeText(QuickWriter writer, String text) {
                    if (cdata) {
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    } else {
                        writer.write(text);
                    }
                }
            };
        }
    });

    public static String objToXml(Object obj, Class clazz){
        xstream.alias("xml",clazz);
        return xstream.toXML(obj);
    }

    public static <T> String serviceMsg(String msgType,T content, Class<?>[] clazz){
        MessageHead messageHead = new MessageHead(msgType);
        XmlMessage<T> xmlMessage = new XmlMessage<T>();
        xmlMessage.setContent(content);
        xmlMessage.setMessageHead(messageHead);
        Class<?>[] clazzes = Arrays.copyOf(clazz,clazz.length+2);
        clazzes[clazz.length] = MessageHead.class;
        clazzes[clazz.length+1] = XmlMessage.class;
        xstream.processAnnotations(clazzes);
        return xstream.toXML(xmlMessage);
    }

    public static <T>String serviceMsg(String msgType,T content, Class<?>[] clazz, String listalias){
        MessageHead messageHead = new MessageHead(msgType);
        XmlMessage<T> xmlMessage = new XmlMessage<>();
        xmlMessage.setContent(content);
        xmlMessage.setMessageHead(messageHead);
        Class<?>[] clazzes = Arrays.copyOf(clazz,clazz.length+2);
        clazzes[clazz.length] = MessageHead.class;
        clazzes[clazz.length+1] = XmlMessage.class;
        xstream.alias(listalias,List.class);
        xstream.processAnnotations(clazzes);
        return xstream.toXML(xmlMessage);
    }

    public static Object toEntity(String xml, Class<?>[] clazz){
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.processAnnotations(clazz);
        return xstream.fromXML(xml);
    }

}
