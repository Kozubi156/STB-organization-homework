package utils;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class OrganizationArguments {

    public static Stream<Arguments> createOrganizationInvalidData() {

        return Stream.of(
                Arguments.of("This is dispaly name", "Akademia QA is awesom!", "akademia_qa", "http://akademia.pl"),
                Arguments.of("This is dispaly name", "Akademia QA is awesom!", "ak", "akademia.pl"));
    }

    private static Stream<Arguments> createOrganizationValidData() {

        return Stream.of(
                Arguments.of("This is dispaly name 1", "Akademia QA is awesom!", "aka12345", "http://akademia.pl"),
                Arguments.of("This is dispaly name 2", "Akademia QA is awesom!", "akademiaaqt67", "http://akademia.pl"),
                Arguments.of("This is dispaly name 4", "Akademia QA is awesom!", "akademiaqa4", "http://akademia.pl"),
                Arguments.of("This is dispaly name 5", "Akademia QA is awesom!", "akademia_qa", "https://akademia .pl"),
                Arguments.of("This is dispaly name 6", "Akademia QA is awesom!", "akademia_qa", "https://akademia.pl"));
    }
}
