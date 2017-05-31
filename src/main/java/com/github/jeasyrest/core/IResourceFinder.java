package com.github.jeasyrest.core;

import com.github.jeasyrest.ioc.Injections;

public interface IResourceFinder {

    IResourceFinder INSTANCE = Injections.INSTANCE.singleton("resourceFinder");

    <T extends IResource> T find(String resourcePath);

}
