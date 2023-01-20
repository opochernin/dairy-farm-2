package ua.pochernin.repository;

import ua.pochernin.model.Cow;

public interface CowRepository {

    Cow findById(Integer id);

    void save(Cow cow, Integer parentId);

    Cow findChild(Integer id);

    Cow findNext(Integer id);

}
