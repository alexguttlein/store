package ar.uba.fi.ingsoft1.dynamicRules;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.uba.fi.ingsoft1.exception.InvalidRuleException;

public class RuleLoader {

    /**
     * Método que carga una lista de reglas compuestas desde un archivo JSON
     * @param content contenido del archivo JSON que contiene las reglas a cargar
     * @return lista de objetos `CompositeRule` cargados desde el JSON
     * @throws JsonProcessingException si ocurre un error durante el parseo del JSON
     */
    public static List<Rule> loadRulesFromJson(String content) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        var root = mapper.readTree(content);
        List<Rule> rules = new ArrayList<>();

        try {
        for (JsonNode ruleNode : root) {
            List<Rule> predicates = new ArrayList<>();

            for (JsonNode predicateNode : ruleNode.get("predicates")) {
                String key = predicateNode.get("key").asText();
                Object value = predicateNode.get("value").asText();
                String arithmeticOperator = predicateNode.get("arithmeticOperator").asText();
                String compareOperator = predicateNode.get("compareOperator").asText();
                String objectClass = predicateNode.get("objectClass").asText();

                predicates.add(new PredicateRule(key, value, arithmeticOperator, compareOperator, objectClass));
            }
            String predicatesRelation = ruleNode.get("predicatesRelation").asText();
            String scope = ruleNode.get("scope").asText();
            String messageError = ruleNode.get("messageError").asText();

            rules.add(new CompositeRule(predicates, predicatesRelation, scope, messageError));
        }
    } catch (NullPointerException e) {
        throw new InvalidRuleException("rule is missing some required fields");
    }
        return rules;
    }
}
