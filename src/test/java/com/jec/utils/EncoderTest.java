package com.jec.utils;

import com.jec.module.business.record.RecordEncode;
import org.junit.Test;

import javax.sound.sampled.spi.AudioFileReader;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Created by jeremyliu on 6/22/16.
 */
public class EncoderTest {

    @Test
    public void test(){
        RecordEncode.encodeByLame("audio/2016-07-22/263/data-0.pcm","audio/2016-07-22/263/data-0.mp3");
    }
}
