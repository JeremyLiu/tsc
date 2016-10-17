package com.jec.protocol.unit;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Created by jeremyliu on 6/20/16.
 */

public class BCDSerializer extends JsonSerializer<BCD> {
    @Override
    public void serialize(BCD bcd, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeString(bcd.toString());
    }
}
