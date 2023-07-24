package com.service.commonservice.utils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;

import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import com.service.commonservice.common.ValidateException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class CommonFunction {

	  @SneakyThrows
	    public static void jsonValidate(InputStream inputStream, String json)  {
	        JsonSchema schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(inputStream);
	        ObjectMapper om = new ObjectMapper();
	        JsonNode jsonNode = om.readTree(json);
	        Set<ValidationMessage> errors = schema.validate(jsonNode);
	        Map<String,String> stringSetMap = new HashMap<>();
	        for(ValidationMessage error: errors){
	            if(stringSetMap.containsKey(formatString(error.getPath()))){
	                String message = stringSetMap.get(formatString(error.getPath()));
	                stringSetMap.put(formatString(error.getPath()),message + ", "+formatString(error.getMessage()));
	            }else{
	                stringSetMap.put(formatString(error.getPath()),formatString(error.getMessage()));
	            }
	        }
	        if(!errors.isEmpty()){
	            throw new ValidateException("RQ01",stringSetMap, HttpStatus.BAD_REQUEST);
	        }
	    }

	public static String formatString(String message) {
		return message.replaceAll("\\$.", "");
	}

}
