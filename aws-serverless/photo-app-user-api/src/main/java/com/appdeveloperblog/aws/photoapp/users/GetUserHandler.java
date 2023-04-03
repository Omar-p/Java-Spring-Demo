package com.appdeveloperblog.aws.photoapp.users;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.util.Map;

public class GetUserHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
  @Override
  public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
    var username = request.getPathParameters().get("name");

    var response = new APIGatewayProxyResponseEvent();
    response.setStatusCode(200);
    response.setHeaders(Map.of("Content-Type", "application/json"));
    response.setBody("""
        {
          "username": "%s",
          "age": 5,
          "lastName": "shabaan"
        }
        """.formatted(username));

    return response;
  }
}
