package techcourse.myblog.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import techcourse.myblog.controller.dto.LoginDto;
import techcourse.myblog.controller.dto.UserDto;
import techcourse.myblog.utils.Utils;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    private static final String USER_NAME = "test";
    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "passWord!1";

    private static final String USER_NAME_2 = "newTest";
    private static final String EMAIL_2 = "test2@test.com";
    private static final String PASSWORD_2 = "passWord!2";

    @Autowired
    private WebTestClient webTestClient;

    private String cookie;

    @BeforeEach
    void setUp() {
        Utils.createUser(webTestClient, new UserDto(USER_NAME, EMAIL, PASSWORD));
        cookie = Utils.getLoginCookie(webTestClient, new LoginDto(EMAIL, PASSWORD));
    }

    @Test
    @DisplayName("회원 가입 페이지를 보여준다.")
    void showSignUpPage() {
        webTestClient.get().uri("/users/sign-up")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("새로운 유저를 저장한다.")
    void save() {
        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("userName", USER_NAME_2)
                        .with("email", EMAIL_2)
                        .with("password", PASSWORD_2))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    URI location = response.getResponseHeaders().getLocation();
                    assertThat(location != null ? location.getPath() : "/error").contains("/login");
                });
    }

    @Test
    @DisplayName("유저를 삭제한다.")
    void delete() {
        webTestClient.delete().uri("/users")
                .header("Cookie", cookie)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    URI location = response.getResponseHeaders().getLocation();
                    webTestClient.get().uri(location)
                            .exchange()
                            .expectBody()
                            .consumeWith(redirectResponse -> {
                                String body = Utils.getResponseBody(redirectResponse.getResponseBody());
                                assertThat(body).contains("Login");
                            });
                });
    }

    @Test
    @DisplayName("로그인되어있지 않은 경우 유저를 삭제하지 못한다.")
    void deleteFailWhenLoggedOut() {
    }
}