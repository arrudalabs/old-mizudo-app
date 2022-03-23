package io.github.arrudalabs.mizudo;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;


@QuarkusMain
public class Main {
    public static void main(String[] args) {
        Quarkus.run(args);
    }
}
