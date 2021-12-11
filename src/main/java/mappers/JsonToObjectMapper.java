package mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.UnrecoverableException;
import models.Order;
import models.Request;
import utils.Helper;
import com.google.inject.Inject;

import java.util.List;

public class JsonToObjectMapper {

    @Inject
    ObjectMapper objectMapper;

    public Request convertRequest(String filepath) throws UnrecoverableException {
        try {

            String json = Helper.readJsonString(filepath);
            List<Order> request = objectMapper.readValue(json, new TypeReference<List<Order>>(){});
            return Request.builder().orderList(request).build();
        } catch (JsonProcessingException jpe) {
            jpe.getStackTrace();
            throw new UnrecoverableException(jpe.getMessage());
        }
    }
}
