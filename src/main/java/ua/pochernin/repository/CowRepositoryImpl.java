package ua.pochernin.repository;

import org.springframework.stereotype.Component;
import ua.pochernin.collection.LinkedTreeNode;
import ua.pochernin.collection.LinkedTreeNodeImpl;
import ua.pochernin.model.Cow;

import java.util.Optional;

@Component
public class CowRepositoryImpl implements CowRepository {

    private final LinkedTreeNode<Integer, Cow> cows;

    public CowRepositoryImpl() {
        final Cow cow = new Cow();
        cow.setCowId(0);
        cow.setNickName("Root Cow");
        cows = new LinkedTreeNodeImpl<>(0, cow);
    }

    @Override
    public Cow findById(Integer id) {
        return Optional.ofNullable(cows.findNode(id))
                .map(LinkedTreeNode::getValue)
                .orElse(null);
    }

    @Override
    public void save(Cow cow, Integer parentId) {
        cows.findNode(parentId).add(cow.getCowId(), cow);
    }

    @Override
    public Cow findChild(Integer id) {
        return Optional.ofNullable(cows.findNode(id))
                .map(LinkedTreeNode::getFirstChild)
                .map(LinkedTreeNode::getValue)
                .orElse(null);
    }

    @Override
    public Cow findNext(Integer id) {
        return Optional.ofNullable(cows.findNode(id))
                .map(LinkedTreeNode::getNext)
                .map(LinkedTreeNode::getValue)
                .orElse(null);
    }

}
