package ru.eshoprzd.config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:auth.properties"
})
public interface AuthConfig extends Config {

    @Key("name")
    String name();

    @Key("name2")
    String name2();

    @Key("email")
    String email();

    @Key("email2")
    String email2();

    @Key("login")
    String login();

    @Key("password")
    String password();
}