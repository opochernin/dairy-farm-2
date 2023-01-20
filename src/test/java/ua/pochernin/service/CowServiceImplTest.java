package ua.pochernin.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.pochernin.exception.CowServiceException;
import ua.pochernin.model.Cow;
import ua.pochernin.repository.CowRepository;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CowServiceImplTest {

    @Mock
    private CowRepository cowRepository;

    @Captor
    private ArgumentCaptor<Cow> cowCaptor;

    @Captor
    private ArgumentCaptor<Integer> parentIdCaptor;

    @InjectMocks
    private CowServiceImpl cowService;

    @Test
    void testGiveBirth() {
        when(cowRepository.findById(eq(1))).thenReturn(null);
        when(cowRepository.findById(eq(0))).thenReturn(new Cow());

        cowService.giveBirth(0, 1, "calve1");
        verify(cowRepository).save(cowCaptor.capture(), parentIdCaptor.capture());

        assertThat(cowCaptor.getValue().getCowId()).isEqualTo(1);
        assertThat(cowCaptor.getValue().getNickName()).isEqualTo("calve1");
        assertThat(parentIdCaptor.getValue()).isEqualTo(0);
    }

    @Test
    void testGiveBirthWithDuplicateChildId() {
        when(cowRepository.findById(eq(1))).thenReturn(new Cow());

        assertThatThrownBy(() -> cowService.giveBirth(0, 1, "calve1"))
                .isInstanceOf(CowServiceException.class)
                .hasMessage("A cow with id=1 already exists.");
    }

    @Test
    void testGiveBirthWhenParentCowDoesNotExist() {
        when(cowRepository.findById(eq(1))).thenReturn(null);
        when(cowRepository.findById(eq(0))).thenReturn(null);

        assertThatThrownBy(() -> cowService.giveBirth(0, 1, "calve1"))
                .isInstanceOf(CowServiceException.class)
                .hasMessage("A parent cow with id=0 does not exist.");
    }

    @Test
    void testGiveBirthWhenParentCowIsNotAlive() {
        final Cow parent = new Cow();
        parent.setAlive(false);
        when(cowRepository.findById(eq(1))).thenReturn(null);
        when(cowRepository.findById(eq(0))).thenReturn(parent);

        assertThatThrownBy(() -> cowService.giveBirth(0, 1, "calve1"))
                .isInstanceOf(CowServiceException.class)
                .hasMessage("A parent cow with id=0 is not alive.");
    }

    @Test
    void testEndLifeSpan() {
        final Cow cow = new Cow();
        when(cowRepository.findById(eq(1))).thenReturn(cow);

        cowService.endLifeSpan(1);

        assertThat(cow.isAlive()).isFalse();
    }

    @Test
    void testEndLifeSpanWhenCowIsAlreadyDead() {
        final Cow cow = new Cow();
        cow.setAlive(false);
        when(cowRepository.findById(eq(1))).thenReturn(cow);

        assertThatThrownBy(() -> cowService.endLifeSpan(1))
                .isInstanceOf(CowServiceException.class)
                .hasMessage("A cow with id=1 is not alive.");
    }

    @Test
    void testGetFarmDataAsString() {
        final Cow root = new Cow();
        root.setCowId(0);
        root.setNickName("Root Cow");
        final Cow calve1 = new Cow();
        calve1.setCowId(1);
        calve1.setNickName("calve1");
        calve1.setParentCowId(0);
        final Cow calve2 = new Cow();
        calve2.setCowId(2);
        calve2.setNickName("calve2");
        calve2.setParentCowId(1);

        when(cowRepository.findById(eq(0))).thenReturn(root);
        when(cowRepository.findChild(eq(0))).thenReturn(calve1);
        when(cowRepository.findChild(eq(1))).thenReturn(calve2);

        assertThat(cowService.getFarmDataAsString())
                .isEqualTo(format("%s%n" +
                                "==>%s%n" +
                                "====>%s%n",
                        root, calve1, calve2));
    }
}
