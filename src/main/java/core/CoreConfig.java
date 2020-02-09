package core;

import org.aeonbits.owner.Config;

public interface CoreConfig extends Config {

    @DefaultValue("in")
    String inputSuffix();

    @DefaultValue("out")
    String outputSuffix();

    @DefaultValue(" ")
    String token();
}
