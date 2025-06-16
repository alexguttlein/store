package ar.uba.fi.ingsoft1.dynamicRules;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RulesLoaderRunner implements CommandLineRunner {

    @Autowired
    private RuleLoaderService ruleLoaderService;

    @Getter
    private RuleEngine ruleEngine;

    @Override
    public void run(String... args) throws Exception { 
        this.ruleEngine = ruleLoaderService.createEngineFromSavedRules();
        
    }

}
