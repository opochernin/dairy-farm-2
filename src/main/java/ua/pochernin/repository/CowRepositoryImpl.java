package ua.pochernin.repository;

import org.springframework.stereotype.Component;
import ua.pochernin.collection.LinkedTreeNode;
import ua.pochernin.model.Cow;

import java.util.Optional;

@Component
public class CowRepositoryImpl implements CowRepository {

    private final LinkedTreeNode<Integer, Cow> linkedTreeNode;

    public CowRepositoryImpl() {
        final Cow cow = new Cow();
        cow.setCowId(0);
        cow.setNickName("Root Cow");
        linkedTreeNode = new LinkedTreeNode<>(0, cow);
    }

    @Override
    public Cow findById(Integer id) {
        return Optional.ofNullable(linkedTreeNode.findById(id))
                .map(LinkedTreeNode::getValue)
                .orElse(null);
    }

    @Override
    public void save(Cow cow, Integer parentId) {
        linkedTreeNode.add(new LinkedTreeNode<>(cow.getCowId(), cow), parentId);
    }

    @Override
    public Cow findChild(Integer id) {
        return Optional.ofNullable(linkedTreeNode.findById(id))
                .map(LinkedTreeNode::getChild)
                .map(LinkedTreeNode::getValue)
                .orElse(null);
    }

    @Override
    public Cow findNext(Integer id) {
        return Optional.ofNullable(linkedTreeNode.findById(id))
                .map(LinkedTreeNode::getNext)
                .map(LinkedTreeNode::getValue)
                .orElse(null);
    }

}
