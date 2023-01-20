package ua.pochernin.repository;

import org.junit.jupiter.api.Test;
import ua.pochernin.model.Cow;

import static org.assertj.core.api.Assertions.assertThat;

class CowRepositoryImplTest {

    private final CowRepositoryImpl cowRepository = new CowRepositoryImpl();

    @Test
    void testSaveAndFindById() {
        final Cow calve1 = new Cow();
        calve1.setCowId(1);
        calve1.setNickName("calve1");
        calve1.setParentCowId(0);
        cowRepository.save(calve1, 0);

        final Cow calve2 = new Cow();
        calve2.setCowId(2);
        calve2.setNickName("calve2");
        calve2.setParentCowId(0);
        cowRepository.save(calve2, 0);

        final Cow calve3 = new Cow();
        calve3.setCowId(3);
        calve3.setNickName("calve3");
        calve3.setParentCowId(1);
        cowRepository.save(calve3, 1);

        assertThat(cowRepository.findById(1)).isEqualTo(calve1);
        assertThat(cowRepository.findById(2)).isEqualTo(calve2);
        assertThat(cowRepository.findById(3)).isEqualTo(calve3);
    }

    @Test
    void testFindNext() {
        final Cow calve1 = new Cow();
        calve1.setCowId(1);
        calve1.setNickName("calve1");
        calve1.setParentCowId(0);
        cowRepository.save(calve1, 0);

        final Cow calve2 = new Cow();
        calve2.setCowId(2);
        calve2.setNickName("calve2");
        calve2.setParentCowId(0);
        cowRepository.save(calve2, 0);

        assertThat(cowRepository.findNext(calve1.getCowId())).isEqualTo(calve2);
    }

    @Test
    void testFindChild() {
        final Cow calve1 = new Cow();
        calve1.setCowId(1);
        calve1.setNickName("calve1");
        calve1.setParentCowId(0);
        cowRepository.save(calve1, 0);

        final Cow calve2 = new Cow();
        calve2.setCowId(2);
        calve2.setNickName("calve2");
        calve2.setParentCowId(1);
        cowRepository.save(calve2, 1);

        assertThat(cowRepository.findChild(calve1.getCowId())).isEqualTo(calve2);
    }
}
