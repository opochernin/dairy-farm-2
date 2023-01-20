package ua.pochernin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.pochernin.collection.LinkedTreeNode;
import ua.pochernin.collection.LinkedTreeNodeImpl;
import ua.pochernin.exception.CowServiceException;
import ua.pochernin.model.Cow;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CowServiceImplTest {

    private LinkedTreeNode<Integer, Cow> rootCow;
    private CowServiceImpl cowService;

    @BeforeEach
    void beforeEach() {
        final var cow = new Cow();
        cow.setCowId(0);
        cow.setNickName("Root Cow");
        rootCow = new LinkedTreeNodeImpl<>(0, cow);

        cowService = new CowServiceImpl(rootCow);
    }

    @Test
    void testGiveBirth() {
        cowService.giveBirth(0, 1, "calve1");
        cowService.giveBirth(0, 2, "calve2");
        cowService.giveBirth(1, 3, "calve3");
        cowService.giveBirth(1, 4, "calve4");

        assertThat(rootCow.findNode(1).getValue().getCowId()).isEqualTo(1);
        assertThat(rootCow.findNode(1).getValue().getParentCowId()).isEqualTo(0);
        assertThat(rootCow.findNode(1).getValue().getNickName()).isEqualTo("calve1");

        assertThat(rootCow.findNode(2).getValue().getCowId()).isEqualTo(2);
        assertThat(rootCow.findNode(2).getValue().getParentCowId()).isEqualTo(0);
        assertThat(rootCow.findNode(2).getValue().getNickName()).isEqualTo("calve2");

        assertThat(rootCow.findNode(3).getValue().getCowId()).isEqualTo(3);
        assertThat(rootCow.findNode(3).getValue().getParentCowId()).isEqualTo(1);
        assertThat(rootCow.findNode(3).getValue().getNickName()).isEqualTo("calve3");

        assertThat(rootCow.findNode(4).getValue().getCowId()).isEqualTo(4);
        assertThat(rootCow.findNode(4).getValue().getParentCowId()).isEqualTo(1);
        assertThat(rootCow.findNode(4).getValue().getNickName()).isEqualTo("calve4");
    }

    @Test
    void testGiveBirthWithDuplicateChildId() {
        cowService.giveBirth(0, 1, "calve1");
        assertThatThrownBy(() -> cowService.giveBirth(0, 1, "calve1"))
                .isInstanceOf(CowServiceException.class)
                .hasMessage("A cow with id=1 already exists.");
    }

    @Test
    void testGiveBirthWhenParentCowDoesNotExist() {
        assertThatThrownBy(() -> cowService.giveBirth(99, 1, "calve1"))
                .isInstanceOf(CowServiceException.class)
                .hasMessage("A parent cow with id=99 does not exist.");
    }

    @Test
    void testGiveBirthWhenParentCowIsNotAlive() {
        cowService.giveBirth(0, 1, "calve1");
        cowService.endLifeSpan(1);

        assertThatThrownBy(() -> cowService.giveBirth(1, 2, "calve2"))
                .isInstanceOf(CowServiceException.class)
                .hasMessage("A parent cow with id=1 is not alive.");
    }

    @Test
    void testEndLifeSpan() {
        cowService.giveBirth(0, 1, "calve1");
        cowService.endLifeSpan(1);

        assertThat(rootCow.findNode(1).getValue().isAlive()).isFalse();
    }

    @Test
    void testEndLifeSpanWhenCowIsRoot() {
        assertThatThrownBy(() -> cowService.endLifeSpan(0))
                .isInstanceOf(CowServiceException.class)
                .hasMessage("Deleting root cow is not allowed.");
    }

    @Test
    void testEndLifeSpanWhenCowDoesNotExist() {
        assertThatThrownBy(() -> cowService.endLifeSpan(1))
                .isInstanceOf(CowServiceException.class)
                .hasMessage("A cow with id=1 does not exist.");
    }

    @Test
    void testEndLifeSpanWhenCowIsAlreadyDead() {
        cowService.giveBirth(0, 1, "calve1");
        cowService.endLifeSpan(1);

        assertThatThrownBy(() -> cowService.endLifeSpan(1))
                .isInstanceOf(CowServiceException.class)
                .hasMessage("A cow with id=1 is not alive.");
    }

    @Test
    void testGetFarmDataAsString() {
        final var calve1 = new Cow();
        calve1.setCowId(1);
        calve1.setParentCowId(0);
        calve1.setNickName("calve1");

        final var calve2 = new Cow();
        calve2.setCowId(2);
        calve2.setParentCowId(0);
        calve2.setNickName("calve2");

        final var calve3 = new Cow();
        calve3.setCowId(3);
        calve3.setParentCowId(1);
        calve3.setNickName("calve3");

        final var calve4 = new Cow();
        calve4.setCowId(4);
        calve4.setParentCowId(1);
        calve4.setNickName("calve4");

        giveBirth(calve1);
        giveBirth(calve2);
        giveBirth(calve3);
        giveBirth(calve4);

        assertThat(cowService.getFarmDataAsString())
                .isEqualTo(format("%s%n" +
                                "==>%s%n" +
                                "====>%s%n" +
                                "====>%s%n" +
                                "==>%s%n",
                        rootCow.getValue(), calve1, calve3, calve4, calve2));
    }

    private void giveBirth(Cow cow) {
        cowService.giveBirth(cow.getParentCowId(), cow.getCowId(), cow.getNickName());
    }
}
