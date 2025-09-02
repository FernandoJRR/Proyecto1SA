package com.sa.finances_service.promotions.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum PromotionType {
    ROOM_MOST_POPULAR("ROOM_MOST_POPULAR", "Most Popular Room"),
    DISH_MOST_POPULAR("DISH_MOST_POPULAR", "Most Popular Dish"),
    CLIENT_MOST_FREQUENT("CLIENT_MOST_FREQUENT", "Frequent Client");

    private final String code;
    private final String name;

    PromotionType(String code, String displayName) {
        this.code = code;
        this.name = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static List<String> getAllCodes() {
        return Arrays.stream(PromotionType.values())
                     .map(PromotionType::getCode)
                     .collect(Collectors.toList());
    }

    public static List<PromotionTypeInfo> getAll() {
        return Arrays.stream(PromotionType.values())
                     .map(pt -> new PromotionTypeInfo(pt.code, pt.name))
                     .collect(Collectors.toList());
    }

    public static PromotionType fromCode(String code) {
        return Arrays.stream(PromotionType.values())
                     .filter(pt -> pt.code.equals(code))
                     .findFirst()
                     .orElseThrow(() ->
                         new IllegalArgumentException("Unknown PromotionType code: " + code));
    }

    public static class PromotionTypeInfo {
        private final String code;
        private final String name;

        public PromotionTypeInfo(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() { return code; }
        public String getName() { return name; }
    }
}
