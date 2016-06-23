package com.jec.utils;

import com.jec.module.business.record.RecordEncode;
import org.junit.Test;

/**
 * Created by jeremyliu on 6/22/16.
 */
public class EncoderTest {

    @Test
    public void test(){
        RecordEncode.encodePcmFile("audio/2016-06-21/269/data-2.pcm","audio/2016-06-21/data-0.mp3");
    }
}
