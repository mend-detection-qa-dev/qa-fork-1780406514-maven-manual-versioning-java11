package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;

/**
 * Minimal application that imports one symbol from each compile-scope dependency.
 * This ensures the three jars are genuinely on the compile classpath and are not
 * pruned by any "unused dependency" analysis.
 */
public class App {

    public static void main(String[] args) throws Exception {
        // commons-lang3
        String trimmed = StringUtils.trimToEmpty("  hello  ");

        // guava
        ImmutableList<String> items = ImmutableList.of(trimmed, "world");

        // jackson-databind
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("items", items.toString());

        System.out.println(mapper.writeValueAsString(node));
    }
}