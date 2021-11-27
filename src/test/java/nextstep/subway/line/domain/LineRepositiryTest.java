package nextstep.subway.line.domain;

import nextstep.subway.section.domain.Section;
import nextstep.subway.section.domain.SectionRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class LineRepositiryTest {

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @DisplayName("section 연관관계 검증")
    @Test
    void findByLineId(){
        Station upStation = stationRepository.save(new Station("건대역"));
        Station downStation = stationRepository.save(new Station("용마산역"));
        Line line = lineRepository.save(new Line("bg-red-600", "7호선"));
        Section section = sectionRepository.save(Section.of(line, upStation, downStation, 10));
        line.addSection(section);
        Line then = lineRepository.findById(line.getId()).get();

        assertThat(then.getSections()).isNotNull();
        assertThat(then.getSections().size()).isEqualTo(1);

    }

}
