package com.jec.utils;

import com.jec.module.business.entity.Record;
import com.jec.module.business.entity.RecordSegment;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeremyliu on 6/20/16.
 */

public class XmlTest {

    @Test
    public void test(){
        List<RecordSegment> segments = new ArrayList<>();
        for (int i=0 ;i< 3;i++) {
            RecordSegment segment = new RecordSegment(Constants.recordPath, i);
            segment.setPeriod((long)(Math.random()*1000));
            segments.add(segment);
        }
        System.out.println(XmlUtils.objToXml(segments,RecordSegment.class));
    }

    @Test
    public void test2(){
        String xml="<list>\n" +
                "  <RecordSegment index=\"0\" period=\"96\" targetFile=\"audio/0.mp3\"/>\n" +
                "  <RecordSegment index=\"1\" period=\"297\" targetFile=\"audio/1.mp3\"/>\n" +
                "  <RecordSegment index=\"2\" period=\"755\" targetFile=\"audio/2.mp3\"/>\n" +
                "</list>";
        List<RecordSegment> segments = ( List<RecordSegment>)XmlUtils.toEntity(xml,RecordSegment.class);
        System.out.println(XmlUtils.objToXml(segments,RecordSegment.class));
    }
}
