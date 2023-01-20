package ua.pochernin.service;

public interface CowService {

    void giveBirth(Integer parentCowId, Integer childCowId, String childNickName);

    void endLifeSpan(Integer cowId);

    String getFarmDataAsString();

}
