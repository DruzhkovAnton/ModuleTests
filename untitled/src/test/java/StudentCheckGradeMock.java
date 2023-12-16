import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class StudentCheckGradeMock {
    private static WireMockServer wireMockServer;

    public void setupServer() {
        wireMockServer = new WireMockServer(5352);
        wireMockServer.start();

        configureFor("localhost", wireMockServer.port());
    }
    public void setupStubs() {
        stubFor(get(urlPathEqualTo("/checkGrade"))
                .withQueryParam("grade", equalTo("100"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody("true")));

        stubFor(get(urlPathEqualTo("/checkGrade"))
                .withQueryParam("grade", notMatching("100"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody("false")));
    }
    public void stopServer() {
        wireMockServer.stop();
    }
}
