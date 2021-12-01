package nextstep.subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.LineMap;
import nextstep.subway.line.LineTestHelper;
import nextstep.subway.station.StationTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 구간 등록 관련 인수 테스트")
public class SectionAcceptanceTest extends AcceptanceTest {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        StationTestHelper.지하철_역_생성_요청("건대역");
        StationTestHelper.지하철_역_생성_요청("용마산역");
        Map<String, String> params = LineMap.of("bg-red-600", "신분당선", "1", "2", "10");
        LineTestHelper.지하철_노선_생성_요청(params);
        StationTestHelper.지하철_역_생성_요청("중곡역");
    }

    @DisplayName("역 사이에 새로운 역을 등록한다.")
    @Test
    void addSection() {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", "1");
        params.put("downStationId", "3");
        params.put("distance", "4");

        // when 지하철_노선에_지하철역_등록_요청
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .pathParam("lineId", "1")
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/{lineId}/sections")
                .then().log().all()
                .extract();

        // then 지하철_노선에_지하철역_등록됨
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank()
        );
    }

    @DisplayName("새로운 역을 상행 종점으로 등록한다.")
    @Test
    void addAscendingStation() {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", "3");
        params.put("downStationId", "1");
        params.put("distance", "4");

        // when 지하철_노선에_지하철역_등록_요청
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .pathParam("lineId", "1")
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/{lineId}/sections")
                .then().log().all()
                .extract();

        // then 지하철_노선에_지하철역_등록됨
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank()
        );
    }

    @DisplayName("새로운 역을 하행 종점으로 등록할 경우")
    @Test
    void addDescendingStation() {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", "2");
        params.put("downStationId", "3");
        params.put("distance", "4");

        // when 지하철_노선에_지하철역_등록_요청
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .pathParam("lineId", "1")
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/{lineId}/sections")
                .then().log().all()
                .extract();

        // then 지하철_노선에_지하철역_등록됨
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank()
        );
    }

    @DisplayName("기존 역 사이 길이보다 크거나 같으면 등록 할 수 없음")
    @Test
    void distanceErrorValid() {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", "1");
        params.put("downStationId", "3");
        params.put("distance", "20");

        // when 지하철_노선에_지하철역_등록_요청
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .pathParam("lineId", "1")
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/{lineId}/sections")
                .then().log().all()
                .extract();

        // then 지하철_노선에_지하철역_등록됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("이미 등록되어 있어서 등록 할 수 없음")
    @Test
    void alreadyAddedValid() {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", "1");
        params.put("downStationId", "2");
        params.put("distance", "5");

        // when 지하철_노선에_지하철역_등록_요청
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .pathParam("lineId", "1")
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/{lineId}/sections")
                .then().log().all()
                .extract();

        // then 지하철_노선에_지하철역_등록됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
