package com.github.nl4.money.config;

import lombok.Getter;
import net.jmob.guice.conf.core.BindConfig;
import net.jmob.guice.conf.core.InjectConfig;
import net.jmob.guice.conf.core.Syntax;

@BindConfig(value = "application", syntax = Syntax.PROPERTIES)
@Getter
public class Properties {

    @InjectConfig("db.url")
    private String dbUrl;
    @InjectConfig("db.user")
    private String dbUser;
    @InjectConfig("db.password")
    private String dbPassword;

}