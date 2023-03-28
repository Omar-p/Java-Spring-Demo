package com.example.demor2dbc;

import com.example.demor2dbc.model.BeerDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
class DemoR2dbcApplicationTests {

  @Autowired
  WebTestClient webTestClient;


  @Order(1)
  @Test
  void testListBeer() {
    webTestClient.get()
        .uri("/api/v2/beers")
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(BeerDTO.class)
        .hasSize(3);
  }

  @Order(2)
  @Test
  void testGetById() {
    webTestClient.get()
        .uri("/api/v2/beers/1")
        .exchange()
        .expectStatus().isOk()
        .expectBody(BeerDTO.class)
        .value(BeerDTO::getId, Matchers.equalTo(1));
  }

  @Order(3)
  @Test
  void testCreate() {
    webTestClient.post()
        .uri("/api/v2/beers")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(BeerDTO.builder()
            .beerStyle("style")
            .beerName("name")
            .build()
        )
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().value("Location", Matchers.containsString("/api/v2/beers/"));


  }

  @Order(999)
  @Test
  void testDelete() {
    webTestClient.delete()
        .uri("/api/v2/beers/1")
        .exchange()
        .expectStatus().isNoContent();
  }

  @Order(4)
  @Test
  void testUpdate() {
    webTestClient.put()
        .uri("/api/v2/beers/1")
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(BeerDTO.builder()
            .beerStyle("newStyle")
            .beerName("newName")
            .upc("1223455")
            .build()), BeerDTO.class
        )
        .exchange()
        .expectStatus().isOk();

    webTestClient.get()
        .uri("/api/v2/beers/1")
        .exchange()
        .expectStatus().isOk()
        .expectBody(BeerDTO.class)
        .value(BeerDTO::getBeerName, Matchers.equalTo("newName"))
        .value(BeerDTO::getBeerStyle, Matchers.equalTo("newStyle"))
        .value(BeerDTO::getUpc, Matchers.equalTo("1223455"));
  }

  @Order(5)
  @Test
  void testPatch() {
    webTestClient.patch()
        .uri("/api/v2/beers/1")
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(BeerDTO.builder()
            .beerStyle("patchedStyle")
            .beerName("patchedName")
            .build()), BeerDTO.class
        )
        .exchange()
        .expectStatus().isOk();

    webTestClient.get()
        .uri("/api/v2/beers/1")
        .exchange()
        .expectStatus().isOk()
        .expectBody(BeerDTO.class)
        .value(BeerDTO::getBeerName, Matchers.equalTo("patchedName"))
        .value(BeerDTO::getBeerStyle, Matchers.equalTo("patchedStyle"));
  }

  @Order(6)
  @Test
  void testUpdateWithInvalidBody() {
    webTestClient.put()
        .uri("/api/v2/beers/1")
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(BeerDTO.builder()
            .beerStyle("newStyle")
            .beerName("")
            .upc("1223455")
            .build()), BeerDTO.class
        )
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void testGetByIdNotFound() {
    webTestClient.get()
        .uri("/api/v2/beers/999")
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  void testDeleteNotFound() {
    webTestClient.delete()
        .uri("/api/v2/beers/999")
        .exchange()
        .expectStatus().isNotFound();
  }


}
