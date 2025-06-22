package com.prepify.be.constants;

import java.util.Map;

public class Constant {
    public static final Map<String, Integer> QUESTION_TYPE = Map.of(
            "image", 1,
            "audio", 2,
            "paragraph", 3
    );

    public static final Map<String, Integer> PART_TYPE = Map.of(
            "listen", 1,
            "read", 2
    );
}
