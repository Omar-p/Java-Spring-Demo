package com.appdeveloperblog.aws.photoapp.users;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.mock;

class GetUserHandlerTest {

  GetUserHandler getUserHandler;
  Context context = mock(Context.class);

  @BeforeEach
  void setup() {
    getUserHandler = new GetUserHandler();

  }

  @Test
  void itShouldHandleRequest() {

    String name = "omar";
    final APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent = new APIGatewayProxyRequestEvent();
    apiGatewayProxyRequestEvent.setPathParameters(Map.of("name", name));

    var actual = getUserHandler.handleRequest(apiGatewayProxyRequestEvent, context);

    then(actual.getStatusCode())
      .isEqualTo(200);

    then(actual.getHeaders())
        .contains(Map.entry("Content-Type", "application/json"));


    assertThatJson(actual.getBody())
        .node("username")
        .isEqualTo(name);
  }

}