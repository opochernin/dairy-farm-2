package ua.pochernin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.pochernin.collection.LinkedTreeNode;
import ua.pochernin.collection.LinkedTreeNodeImpl;
import ua.pochernin.model.Cow;

@Configuration
public class DairyFarmConfig {

    @Bean
    public LinkedTreeNode<Integer, Cow> rootCow() {
        final var cow = new Cow();
        cow.setCowId(0);
        cow.setNickName("Root Cow");
        return new LinkedTreeNodeImpl<>(0, cow);
    }
}
