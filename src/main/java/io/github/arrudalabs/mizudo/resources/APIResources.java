package io.github.arrudalabs.mizudo.resources;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@OpenAPIDefinition(
        info = @Info(
                title = "Mizu-do API",
                version = "0.0.1")
)
@ApplicationPath("/resources")
public class APIResources extends Application {
}
