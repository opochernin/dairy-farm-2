package ua.pochernin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.pochernin.collection.LinkedTreeNode;
import ua.pochernin.exception.CowServiceException;
import ua.pochernin.model.Cow;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class CowServiceImpl implements CowService {

    private final LinkedTreeNode<Integer, Cow> rootCow;

    @Override
    public void giveBirth(Integer parentCowId, Integer childCowId, String childNickName) {
        if (rootCow.findNode(childCowId) != null) {
            throw new CowServiceException(format("A cow with id=%s already exists.", childCowId));
        }

        final LinkedTreeNode<Integer, Cow> parentCow = rootCow.findNode(parentCowId);

        if (parentCow == null) {
            throw new CowServiceException(format("A parent cow with id=%s does not exist.", parentCowId));
        }

        if (!parentCow.getValue().isAlive()) {
            throw new CowServiceException(format("A parent cow with id=%s is not alive.", parentCowId));
        }

        final var cow = new Cow();
        cow.setCowId(childCowId);
        cow.setNickName(childNickName);
        cow.setParentCowId(parentCowId);
        parentCow.add(cow.getCowId(), cow);
    }

    @Override
    public void endLifeSpan(Integer cowId) {
        if (rootCow.getValue().getCowId().equals(cowId)) {
            throw new CowServiceException("Deleting root cow is not allowed.");
        }

        final var cow = rootCow.findNode(cowId);

        if (cow == null) {
            throw new CowServiceException(format("A cow with id=%s does not exist.", cowId));
        }

        if (!cow.getValue().isAlive()) {
            throw new CowServiceException(format("A cow with id=%s is not alive.", cowId));
        }

        cow.getValue().setAlive(false);
    }

    @Override
    public String getFarmDataAsString() {
        return getCowAsString(rootCow, 0).toString();
    }

    private StringBuilder getCowAsString(LinkedTreeNode<Integer, Cow> cowNode, int depth) {
        final var sb = new StringBuilder();

        var prefix = IntStream.range(0, depth)
                .mapToObj(i -> "==")
                .collect(Collectors.joining());
        if (!prefix.isEmpty()) {
            prefix = prefix + ">";
        }

        sb.append(prefix).append(cowNode.getValue()).append(System.lineSeparator());

        final var child = cowNode.getFirstChild();

        if (child == null) {
            return sb;
        }

        var next = child;
        while (next != null) {
            sb.append(getCowAsString(next, depth + 1));
            next = next.getNext();
        }

        return sb;
    }

}
