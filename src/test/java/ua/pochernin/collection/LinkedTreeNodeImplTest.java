package ua.pochernin.collection;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LinkedTreeNodeImplTest {

    @Test
    void testCollectionOperations() {
        final LinkedTreeNodeImpl<Integer, String> collection = new LinkedTreeNodeImpl<>(0, "root");
        collection.findNode(0).add(1, "child_1");
        collection.findNode(1).add(12, "child_1_2");
        collection.findNode(1).add(13, "child_1_3");
        collection.findNode(1).add(14, "child_1_4");
        collection.findNode(12).add(121, "child_1_2_1");

        assertThat(collection.findNode(0).getValue()).isEqualTo("root");
        assertThat(collection.findNode(0).getNext()).isNull();
        assertThat(collection.findNode(0).getFirstChild().getValue()).isEqualTo("child_1");

        assertThat(collection.findNode(1).getValue()).isEqualTo("child_1");
        assertThat(collection.findNode(1).getNext()).isNull();
        assertThat(collection.findNode(1).getFirstChild().getValue()).isEqualTo("child_1_2");

        assertThat(collection.findNode(12).getValue()).isEqualTo("child_1_2");
        assertThat(collection.findNode(12).getNext().getValue()).isEqualTo("child_1_3");
        assertThat(collection.findNode(12).getFirstChild().getValue()).isEqualTo("child_1_2_1");

        assertThat(collection.findNode(13).getNext().getValue()).isEqualTo("child_1_4");
        assertThat(collection.findNode(13).getFirstChild()).isNull();

        assertThat(collection.findNode(14).getNext()).isNull();
        assertThat(collection.findNode(14).getFirstChild()).isNull();

    }
}
