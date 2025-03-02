package br.com.picpay.backend;

import br.com.picpay.backend.config.TestContainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestContainersConfiguration.class)
@SpringBootTest
class PicpayChallengeBackendApplicationTests {

    @Test
    void contextLoads() {
    }

}
