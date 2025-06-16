package ar.uba.fi.ingsoft1.dynamicRules;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class RuleLoaderService {

    private static final Logger logger = LoggerFactory.getLogger(RuleLoaderService.class);

    @Autowired
    private ResourcePatternResolver resourcePatternResolver;

    private RuleEngine engine;

    public RuleEngine createEngineFromSavedRules() {
        this.engine = new RuleEngine();
        logger.info("loading business rules...");
        var count = 0;
        try {

            Resource[] resources = resourcePatternResolver.getResources("classpath:rules/*.json");
            List<Rule> rules;
            
            for (Resource resource : resources) {
                try {
                    String content = new String(Files.readAllBytes(Paths.get(resource.getURI())));
                    rules = RuleLoader.loadRulesFromJson(content);
                    logger.debug("loaded rule \"" + resource.getFilename() + "\"");
                    this.engine.addRules(rules);
                    count += 1;
                }
                catch (JsonProcessingException e){
                    logger.error("couldn't parse json rule from \"" + resource.getFilename() + "\" :" + e.getMessage());
                }
                catch (IOException e) {
                    logger.error("couldn't load rule \"" + resource.getFilename() + "\": " + e.getMessage());
                }
                catch (IllegalArgumentException e) {
                    logger.error(e.getMessage() + ": rule \"" + resource.getFilename() + "\" incorrectly formed. skipping...");
                }
            }

            if (count > 0){
                logger.info("succesfully loaded " + count + " rules.");
            }
        } 
        catch (IOException e) {
            logger.error("couldn't read or find any file rules from rules/ directory: " + e.getMessage());
        }
        return engine;
    }

    public void loadRulesFromJSON(String jsonContents) 
                    throws JsonProcessingException {
        var rules = RuleLoader.loadRulesFromJson(jsonContents);
        logger.debug("number of parsed rules in json: " + rules.size());
        this.engine.addRules(rules);
    }

    public RuleEngine getRuleEngine() {
        return this.engine;
    }

}
