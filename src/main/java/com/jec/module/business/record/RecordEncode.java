package com.jec.module.business.record;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;

import org.tritonus.sampled.file.mpeg.MpegAudioFileReader;
import	org.tritonus.share.sampled.AudioFileTypes;
import	org.tritonus.share.sampled.Encodings;
/**
 * Created by jeremyliu on 6/19/16.
 */
public class RecordEncode {

    private static final AudioFormat.Encoding	MPEG1L3 = Encodings.getEncoding("MPEG1L3");
    private static final AudioFileFormat.Type	MP3 = AudioFileTypes.getType("MP3", "mp3");
    private static final int SampleRate = 8000;

    private static String lameBinary = "lame";

    public static AudioInputStream getConvertedStream2(
            AudioInputStream sourceStream,
            AudioFormat.Encoding targetEncoding)
            throws Exception {
        AudioFormat sourceFormat = sourceStream.getFormat();

        // build the output format
        AudioFormat targetFormat = new AudioFormat(
                targetEncoding,
                sourceFormat.getSampleRate(),
                AudioSystem.NOT_SPECIFIED,
                sourceFormat.getChannels(),
                AudioSystem.NOT_SPECIFIED,
                AudioSystem.NOT_SPECIFIED,
                false); // endianness doesn't matter
        // construct a converted stream
        AudioInputStream targetStream = null;
        if (!AudioSystem.isConversionSupported(targetFormat, sourceFormat)) {
            System.out.println("Direct conversion not possible.");
            System.out.println("Trying with intermediate PCM format.");
            AudioFormat intermediateFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    sourceFormat.getSampleRate(),
                    16,
                    sourceFormat.getChannels(),
                    2 * sourceFormat.getChannels(), // frameSize
                    sourceFormat.getSampleRate(),
                    true);
            if (AudioSystem.isConversionSupported(intermediateFormat, sourceFormat)) {
                // intermediate conversion is supported
                sourceStream = AudioSystem.getAudioInputStream(intermediateFormat, sourceStream);
            }
        }
        targetStream = AudioSystem.getAudioInputStream(targetFormat, sourceStream);
        if (targetStream == null) {
            throw new Exception("conversion not supported");
        }

        System.out.println("Got converted AudioInputStream: " + targetStream.getClass().getName());
        System.out.println("Output format: " + targetStream.getFormat());

        return targetStream;
    }



    public static AudioInputStream getConvertedStream(
            AudioInputStream sourceStream,
            AudioFormat.Encoding targetEncoding)
            throws Exception {
        AudioFormat sourceFormat = sourceStream.getFormat();
        System.out.println("Input format: " + sourceFormat);

        // construct a converted stream
        AudioInputStream targetStream = null;
        if (!AudioSystem.isConversionSupported(targetEncoding, sourceFormat)) {
            System.out.println("Direct conversion not possible.");
            System.out.println("Trying with intermediate PCM format.");
            AudioFormat intermediateFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    sourceFormat.getSampleRate(),
                    16,
                    sourceFormat.getChannels(),
                    2 * sourceFormat.getChannels(), // frameSize
                    sourceFormat.getSampleRate(),
                    true);
            if (AudioSystem.isConversionSupported(intermediateFormat, sourceFormat)) {
                // intermediate conversion is supported
                sourceStream = AudioSystem.getAudioInputStream(intermediateFormat, sourceStream);
            }
        }
        targetStream = AudioSystem.getAudioInputStream(targetEncoding, sourceStream);
        if (targetStream == null) {
            throw new Exception("conversion not supported");
        }

        System.out.println("Got converted AudioInputStream: " + targetStream.getClass().getName());
        System.out.println("Output format: " + targetStream.getFormat());

        return targetStream;
    }

    private static AudioInputStream getInStream(String filename)
            throws IOException {
        AudioInputStream ais = null;
        try {
            ais = AudioSystem.getAudioInputStream(new File(filename));
//            AudioFormat af = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000, 16, 1, 2, 8000, true);
//            FileInputStream fis = new FileInputStream(filename);
//            ais = new AudioInputStream(fis, af, fis.available());
        } catch (Exception e) {
                e.printStackTrace();
        }
        if (ais == null) {
            throw new IOException("Cannot open \"" + filename + "\"");
        }
        return ais;
    }

    public static int encode(String filename ,String outFilename){
        int writtenBytes = -1;
        try{
            AudioFileFormat.Type targetType = MP3;
            AudioInputStream ais = getInStream(filename);
            ais = getConvertedStream(ais, MPEG1L3);
            writtenBytes = AudioSystem.write(ais, targetType, new File(outFilename));
        }catch (Exception e){
            e.printStackTrace();
        }
        return writtenBytes;
    }

    public static boolean encodeByLame(String filename, String outFilename){
        Runtime runtime=Runtime.getRuntime();

        filename =  new File(filename).getAbsolutePath();
        outFilename = new File(outFilename).getAbsolutePath();

        String cmd = lameBinary + " -r --big-endian -m m -s "
                + SampleRate + " "
                + filename + " " + outFilename;
        try {
            final Process process = runtime.exec(cmd);
            new Thread(){
                @Override
                public void run(){
                    try{
                        System.out.println(loadStream(process.getInputStream()));
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }.start();

            new Thread(){
                @Override
                public void run(){
                    try{
                        System.out.println(loadStream(process.getErrorStream()));
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }.start();
            if(process.waitFor()!=0){
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static String loadStream(InputStream in) throws IOException {
        int ptr = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder buffer = new StringBuilder();
        while ((ptr = reader.read()) != -1) {
            buffer.append((char) ptr);
        }
        return buffer.toString();
    }
//    public static boolean encodePcmFile(String pcmFile, String gsmFile) {
//
//
//        File pcm = new File(pcmFile);
//        File gsm = new File(gsmFile);
//
//        FileInputStream fis = null;
//        AudioInputStream ais = null;
//
//        try
//        {
//            AudioFormat af = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000, 16, 1, 2, 8000, true);
//            fis = new FileInputStream(pcm);
//            ais = new AudioInputStream(fis, af, fis.available());
//        } catch (FileNotFoundException e) {
//
//            e.printStackTrace();
//
//            return false;
//
//        } catch (Exception e) {
//
//            return false;
//
//        }
//
//        AudioInputStream gsmAIS = null;
//
//        try
//        {
//            /** We check if the input file has a format that can be converted
//             to GSM.
//             */
//            AudioFormat sourceFormat = ais.getFormat();
//            if (! sourceFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED) ||
//                    Float.compare(sourceFormat.getSampleRate(), 8000.0F) != 0 ||
//                    sourceFormat.getSampleSizeInBits() != 16 ||
//                    sourceFormat.getChannels() != 1)
//            {
//                System.out.println("The format of the input data has to be PCM 8 kHz 16 bit mono");
//                //System.exit(1);
//                return false;
//            }
//            AudioFormat.Encoding targetEncoding = new AudioFormat.Encoding("GSM0610");
//            gsmAIS = new GSMFormatConversionProvider().getAudioInputStream(targetEncoding, ais);
//            AudioFileFormat.Type fileType = new AudioFileFormat.Type("GSM", ".gsm");
//
//            new GSMAudioFileWriter().write(gsmAIS, fileType, gsm);
//            gsmAIS.close();
//            ais.close();
//            fis.close();
//
//        } catch (IOException e) {
//
//            e.printStackTrace();
//            return false;
//
//        }
//
//        return true;
//    }
}
