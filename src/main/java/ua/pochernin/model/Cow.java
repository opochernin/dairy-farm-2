package ua.pochernin.model;

import lombok.Data;

@Data
public class Cow {
    private Integer cowId;
    private Integer parentCowId;
    private String nickName;
    private boolean isAlive = true;
}
