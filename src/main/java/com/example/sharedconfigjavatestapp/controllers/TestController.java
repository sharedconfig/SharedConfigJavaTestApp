package com.example.sharedconfigjavatestapp.controllers;

import com.example.sharedconfigjavatestapp.configuration.MyConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sharedconfig.core.exceptions.StoreNotLoadedException;
import sharedconfig.core.interfaces.IScopedConfigurationService;
import sharedconfig.core.interfaces.ISharedConfigMonitor;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class TestController {
    @Autowired
    ISharedConfigMonitor<MyConfiguration> configuration;

    @Autowired
    IScopedConfigurationService<MyConfiguration> configurationService;

    @RequestMapping(method = { RequestMethod.GET }, value="/")
    public String index() {
        return "index.html";
    }

    @RequestMapping(value = "/latest", method = { RequestMethod.GET })
    @ResponseBody
    public MyConfiguration latest() {
        return configuration.latest();
    }

    @RequestMapping(value = "/all", method = { RequestMethod.GET })
    @ResponseBody
    public List<MyConfiguration> all() throws StoreNotLoadedException {
        var allIds = configurationService.getVersionIds();
        var result = new ArrayList<MyConfiguration>();
        for (var id : allIds) {
            var v = configurationService.getVersion(id);
            v.ifPresent(result::add);
        }
        return result;
    }

    @RequestMapping(value = "/isstoreloaded", method = { RequestMethod.GET })
    @ResponseBody
    public boolean isstoreloaded() throws StoreNotLoadedException {
        var allIds = configurationService.getVersionIds();
        return allIds.size() > 0;
    }
}
