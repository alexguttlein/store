package dynamicRulesTest;

import java.io.File;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ar.uba.fi.ingsoft1.alternative.Alternative;
import ar.uba.fi.ingsoft1.dynamicRules.comparators.*;
import ar.uba.fi.ingsoft1.dynamicRules.objectClass.ObjectClassEvaluatorFactory;
import ar.uba.fi.ingsoft1.dynamicRules.operators.ArithmeticOperator;
import ar.uba.fi.ingsoft1.dynamicRules.operators.ArithmeticOperatorFactory;
import ar.uba.fi.ingsoft1.dynamicRules.relations.RelationFactory;
import com.fasterxml.jackson.core.JsonParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import ar.uba.fi.ingsoft1.attribute.Attribute;
import ar.uba.fi.ingsoft1.cart.Cart;
import ar.uba.fi.ingsoft1.dynamicRules.Rule;
import ar.uba.fi.ingsoft1.dynamicRules.RuleEngine;
import ar.uba.fi.ingsoft1.dynamicRules.RuleLoader;
import ar.uba.fi.ingsoft1.product.Product;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DynamicRulesTest {

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void checkOrderRulesForWeightLowerThan10 () throws Exception {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new Attribute("peso", "3"));
        Set<Attribute> attributes2 = new HashSet<>();
        attributes2.add(new Attribute("peso", "3"));

        Product product1 = new Product(1L, "Product 1","Product 1", 1, attributes, null);
        Product product2 = new Product(2L, "Product 2","Product 2", 1, attributes2, null);

        Cart cart = new Cart();
        cart.addProduct(product1);
        cart.addProduct(product2);

        var rulePath = "weightLimit10Rule.json";
        var file = new File(rulePath);
        var contents = Files.readAllBytes(file.toPath());

        List<Rule> rules = RuleLoader.loadRulesFromJson(new String(contents));
        RuleEngine ruleEngine = new RuleEngine(rules);

        assertTrue(ruleEngine.validateOrder(cart).isEmpty());
    }

    @Test
    void checkOrderRulesForWeightOverThan10 () throws Exception {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new Attribute("peso", "6"));

        Product product1 = new Product(1L, "Product 1","Product 1", 1, attributes, null);
        Product product2 = new Product(2L, "Product 2","Product 2", 1, attributes, null);

        Cart cart = new Cart();
        cart.addProduct(product1);
        cart.addProduct(product2);

        var rulePath = "weightLimit10Rule.json";
        var file = new File(rulePath);
        var contents = Files.readAllBytes(file.toPath());

        List<Rule> rules = RuleLoader.loadRulesFromJson(new String(contents));
        RuleEngine ruleEngine = new RuleEngine(rules);

        assertFalse(ruleEngine.validateOrder(cart).isEmpty());
    }

    @Test
    void checkOrderRulesForWeightBetween2and10() throws Exception {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new Attribute("peso", "3"));

        Product product1 = new Product(1L, "Product 1","Product 1", 1, attributes, null);
        Product product2 = new Product(2L, "Product 2","Product 2", 1, attributes, null);

        Cart cart = new Cart();
        cart.addProduct(product1);
        cart.addProduct(product2);

        var rulePath = "weightLimitBetween2and10Rule.json";
        var file = new File(rulePath);
        var contents = Files.readAllBytes(file.toPath());

        List<Rule> rules = RuleLoader.loadRulesFromJson(new String(contents));
        RuleEngine ruleEngine = new RuleEngine(rules);

        assertTrue(ruleEngine.validateOrder(cart).isEmpty());
    }

    @Test
    void checkOrderRulesForWeightLowerThan2and10() throws Exception {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new Attribute("peso", "1"));

        Product product1 = new Product(1L, "Product 1","Product 1", 1, attributes, null);
        Product product2 = new Product(2L, "Product 2","Product 2", 1, attributes, null);

        Cart cart = new Cart();
        cart.addProduct(product1);
        cart.addProduct(product2);

        var rulePath = "weightLimitBetween2and10Rule.json";
        var file = new File(rulePath);
        var contents = Files.readAllBytes(file.toPath());

        List<Rule> rules = RuleLoader.loadRulesFromJson(new String(contents));
        RuleEngine ruleEngine = new RuleEngine(rules);

        assertTrue(ruleEngine.validateOrder(cart).contains("El pedido debe tener un peso entre 2kg y 10kg"));
    }

    @Test
    void checkOrderRulesForWeightHigherThan2and10() throws Exception {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new Attribute("peso", "6"));

        Product product1 = new Product(1L, "Product 1","Product 1", 1, attributes, null);
        Product product2 = new Product(2L, "Product 2","Product 2", 1, attributes, null);

        Cart cart = new Cart();
        cart.addProduct(product1);
        cart.addProduct(product2);

        var rulePath = "weightLimitBetween2and10Rule.json";
        var file = new File(rulePath);
        var contents = Files.readAllBytes(file.toPath());

        List<Rule> rules = RuleLoader.loadRulesFromJson(new String(contents));
        RuleEngine ruleEngine = new RuleEngine(rules);

        assertFalse(ruleEngine.validateOrder(cart).isEmpty());
    }

    @Test
    void checkRuleForHeightBetween10And50() throws Exception {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new Attribute("altura", "15"));

        Product product1 = new Product(1L, "Product 1","Product 1", 1, attributes, null);
        Product product2 = new Product(2L, "Product 2","Product 2", 1, attributes, null);

        Cart cart = new Cart();
        cart.addProduct(product1);
        cart.addProduct(product2);
        cart.addProduct(product2);

        var rulePath = "heightBetween10and50Rule.json";
        var file = new File(rulePath);
        var contents = Files.readAllBytes(file.toPath());

        List<Rule> rules = RuleLoader.loadRulesFromJson(new String(contents));
        RuleEngine ruleEngine = new RuleEngine(rules);

        assertTrue(ruleEngine.validateOrder(cart).isEmpty());
    }

    @Test
    void checkRuleForOverHeight50OrWeightLessThan10() throws Exception {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new Attribute("altura", "20"));
        attributes.add(new Attribute("peso", "1"));

        Product product1 = new Product(1L, "Product 1","Product 1", 1, attributes, null);
        Product product2 = new Product(2L, "Product 2","Product 2", 1, attributes, null);

        Cart cart = new Cart();
        cart.addProduct(product1);
        cart.addProduct(product2);
        cart.addProduct(product2);

        var rulePath = "heightLessThan50OrWeightLessThan10Rule.json";
        var file = new File(rulePath);
        var contents = Files.readAllBytes(file.toPath());

        List<Rule> rules = RuleLoader.loadRulesFromJson(new String(contents));
        RuleEngine ruleEngine = new RuleEngine(rules);

        assertTrue(ruleEngine.validateOrder(cart).isEmpty());
    }

    @Test
    void checkRuleForHeightLessThan50OrOverWeight10() throws Exception {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new Attribute("altura", "15"));
        attributes.add(new Attribute("peso", "5"));

        Product product1 = new Product(1L, "Product 1","Product 1", 1, attributes, null);
        Product product2 = new Product(2L, "Product 2","Product 2", 1, attributes, null);

        Cart cart = new Cart();
        cart.addProduct(product1);
        cart.addProduct(product2);
        cart.addProduct(product2);

        var rulePath = "heightLessThan50OrWeightLessThan10Rule.json";
        var file = new File(rulePath);
        var contents = Files.readAllBytes(file.toPath());

        List<Rule> rules = RuleLoader.loadRulesFromJson(new String(contents));
        RuleEngine ruleEngine = new RuleEngine(rules);

        assertTrue(ruleEngine.validateOrder(cart).isEmpty());
    }

    @Test
    void checkRuleFor10Or50() throws Exception {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new Attribute("altura", "15"));
        attributes.add(new Attribute("peso", "2"));

        Product product1 = new Product(1L, "Product 1","Product 1", 1, attributes, null);
        Product product2 = new Product(2L, "Product 2","Product 2", 1, attributes, null);

        Cart cart = new Cart();
        cart.addProduct(product1);
        cart.addProduct(product2);

        var rulePath = "heightLessThan50OrWeightLessThan10Rule.json";
        var file = new File(rulePath);
        var contents = Files.readAllBytes(file.toPath());

        List<Rule> rules = RuleLoader.loadRulesFromJson(new String(contents));
        RuleEngine ruleEngine = new RuleEngine(rules);

        assertTrue(ruleEngine.validateOrder(cart).isEmpty());
    }

    @Test
    void liquidCantBeWithGas() throws Exception {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new Attribute("estado", "liquido"));

        Set<Attribute> attributes2 = new HashSet<>();
        attributes2.add(new Attribute("estado", "gaseoso"));

        Product product1 = new Product(1L, "Product 1","Product 1", 1, attributes, null);
        Product product2 = new Product(2L, "Product 2","Product 2", 1, attributes, null);

        Cart cart = new Cart();
        cart.addProduct(product1);
        cart.addProduct(product2);

        var rulePath = "liquidCantBeWithGasRule.json";
        var file = new File(rulePath);
        var contents = Files.readAllBytes(file.toPath());

        List<Rule> rules = RuleLoader.loadRulesFromJson(new String(contents));
        RuleEngine ruleEngine = new RuleEngine(rules);

        assertTrue(ruleEngine.validateOrder(cart).contains("Un mismo pedido no puede combinar productos líquidos y gaseosos"));
    }

    @Test
    void liquidCanBeWithSolid() throws Exception {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new Attribute("estado", "liquido"));

        Set<Attribute> attributes2 = new HashSet<>();
        attributes2.add(new Attribute("estado", "solido"));

        Product product1 = new Product(1L, "Product 1","Product 1", 1, attributes, null);
        Product product2 = new Product(2L, "Product 2","Product 2", 1, attributes, null);

        Cart cart = new Cart();
        cart.addProduct(product1);
        cart.addProduct(product2);

        var rulePath = "liquidCantBeWithGasRule.json";
        var file = new File(rulePath);
        var contents = Files.readAllBytes(file.toPath());

        List<Rule> rules = RuleLoader.loadRulesFromJson(new String(contents));
        RuleEngine ruleEngine = new RuleEngine(rules);

        assertTrue(ruleEngine.validateOrder(cart).isEmpty());
    }

    @Test
    void illegalArithmeticSymbolJsonParsedException() throws Exception {

        JsonParseException exception = assertThrows(
                JsonParseException.class,
                () ->  RuleLoader.loadRulesFromJson("illegalArithmeticSymbol.json")
        );
    }

    @Test
    void illegalObjectClassJsonParsedException() throws Exception {

        JsonParseException exception = assertThrows(
                JsonParseException.class,
                () ->  RuleLoader.loadRulesFromJson("illegalObjectClass.json")
        );
    }

    @Test
    void illegalComparissonOperatorJsonParsedException() throws Exception {

        JsonParseException exception = assertThrows(
                JsonParseException.class,
                () ->  RuleLoader.loadRulesFromJson("illegalComparissonOperator.json")
        );
    }

    @Test
    void illegalRelationTypeException() {

        String invalidRelationType = "&";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> RelationFactory.create(invalidRelationType),
                "Unknown relation type: " + invalidRelationType
        );

        assertEquals("Unknown relation type: " + invalidRelationType, exception.getMessage());
    }

    @Test
    void illegalObjectClassException() throws Exception{

        String invalidObjectClass = "invalidClass";
        ArithmeticOperator mockArithmeticOperator = ArithmeticOperatorFactory.create("+");
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ObjectClassEvaluatorFactory.create(invalidObjectClass, mockArithmeticOperator),
                "Expected IllegalArgumentException to be thrown"
        );

        assertEquals("Invalid objectClass: " + invalidObjectClass, exception.getMessage());
    }

    @Test
    void illegalComparissonOperatorException() {

        String invalidComparissonOperator = "&";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ComparisonOperatorFactory.create(invalidComparissonOperator),
                "Invalid comparison operator: " + invalidComparissonOperator
        );

        assertEquals("Invalid comparison operator: " + invalidComparissonOperator, exception.getMessage());
    }

    @Test
    void emptyComparissonOperatorException() {

        String invalidComparissonOperator = "";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ComparisonOperatorFactory.create(invalidComparissonOperator),
                "Comparison operator cannot be null or empty"
        );

        assertEquals("Comparison operator cannot be null or empty", exception.getMessage());
    }

    @Test
    void comparissonOperatorFactoryTests(){
        String equal = "==";
        String greaterEqual = ">=";
        String notEqual = "!=";
        String notContain = "not Contain";

        assertEquals(ComparisonOperatorFactory.create(equal).getClass(), Equal.class);
        assertEquals(ComparisonOperatorFactory.create(greaterEqual).getClass(), GreaterEqual.class);
        assertEquals(ComparisonOperatorFactory.create(notEqual).getClass(), NotEqual.class);
        assertEquals(ComparisonOperatorFactory.create(notContain).getClass(), NotContain.class);
    }

    @Test
    void comparissonOperatorCompareTest() {
        Object equal = ComparisonOperatorFactory.create("==");
        Object greaterEqual = ComparisonOperatorFactory.create(">=");
        Object notEqual = ComparisonOperatorFactory.create("!=");
        Object notContain = ComparisonOperatorFactory.create("notcontain");

        assertTrue(((ComparisonOperator) equal).compare(5,5));
        assertTrue(((ComparisonOperator) greaterEqual).compare(5,4));
        assertTrue(((ComparisonOperator) notEqual).compare(5,4));
        assertTrue(((ComparisonOperator) notContain).compare("hola","x"));
        assertFalse(((ComparisonOperator) notContain).compare("hola","a"));
    }

    @Test
    void categoryElectronicDontGoWithHome() throws Exception {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new Attribute("estado", "solido"));

        Set<Attribute> attributes2 = new HashSet<>();
        attributes2.add(new Attribute("estado", "solido"));

        Alternative alternative = new Alternative("Electronic", "Electronic");
        Alternative alternative2 = new Alternative("Home", "Home");
        Product product1 = new Product(1L, "Product 1","Product 1", 1, attributes, null);
        Product product2 = new Product(2L, "Product 2","Product 2", 1, attributes, null);

        Cart cart = new Cart();
        cart.addProduct(product1);
        cart.addProduct(product2);

        var rulePath = "categoryElectronicDontGoWithHomeRule.json";
        var file = new File(rulePath);
        var contents = Files.readAllBytes(file.toPath());

        List<Rule> rules = RuleLoader.loadRulesFromJson(new String(contents));
        RuleEngine ruleEngine = new RuleEngine(rules);

        assertFalse(ruleEngine.validateOrder(cart).isEmpty());
        assertTrue(ruleEngine.validateOrder(cart).contains("Un mismo pedido no puede combinar productos de electronica y hogar"));
    }

    @Test
    void categoryElectronicDontGoWithSport() throws Exception {
        Alternative alternative = new Alternative("Electronic", "Electronic");
        Alternative alternative2 = new Alternative("Sport", "Sport");
        Product product1 = new Product(1L, "Product 1","Product 1", 1, null, alternative);
        Product product2 = new Product(2L, "Product 2","Product 2", 1, null, alternative2);

        Cart cart = new Cart();
        cart.addProduct(product1);
        cart.addProduct(product2);

        var rulePath = "categoryElectronicDontGoWithHomeRule.json";
        var file = new File(rulePath);
        var contents = Files.readAllBytes(file.toPath());

        List<Rule> rules = RuleLoader.loadRulesFromJson(new String(contents));
        RuleEngine ruleEngine = new RuleEngine(rules);

        assertTrue(ruleEngine.validateOrder(cart).isEmpty());
    }

    @Test
    void checkMax10weightLiquidGasAndElectronicHomeRule() throws Exception {
        Set<Attribute> attributeLiquid6kg = new HashSet<>();
        attributeLiquid6kg.add(new Attribute("estado", "liquido"));
        attributeLiquid6kg.add(new Attribute("peso", "6"));

        Set<Attribute> attributeGas = new HashSet<>();
        attributeGas.add(new Attribute("estado", "gaseoso"));

        Set<Attribute> attributeWeight5kg = new HashSet<>();
        attributeWeight5kg.add(new Attribute("peso", "5"));

        Alternative alternativeElectronic = new Alternative("Electronic", "Electronic");
        Alternative alternativeHome = new Alternative("Home", "Home");
        Alternative alternativeSport = new Alternative("Sport", "Sport");

        Product product1 = new Product(1L, "Product 1","Product 1", 1, attributeGas, alternativeElectronic);
        Product product2 = new Product(2L, "Product 2","Product 2", 1, attributeLiquid6kg, alternativeSport);
        Product product3 = new Product(2L, "Product 2","Product 2", 1, attributeWeight5kg, alternativeHome);

        Cart cart = new Cart();
        cart.addProduct(product1);
        cart.addProduct(product2);
        cart.addProduct(product3);

        var rulePath = "max10weightLiquidGasAndElectronicHomeRule.json";
        var file = new File(rulePath);
        var contents = Files.readAllBytes(file.toPath());

        List<Rule> rules = RuleLoader.loadRulesFromJson(new String(contents));
        RuleEngine ruleEngine = new RuleEngine(rules);

        assertFalse(ruleEngine.validateOrder(cart).isEmpty());
        assertTrue(ruleEngine.validateOrder(cart).contains("Un mismo pedido no puede combinar productos de electronica y hogar"));
        assertTrue(ruleEngine.validateOrder(cart).contains("Un mismo pedido no puede combinar productos líquidos y gaseosos"));
        assertTrue(ruleEngine.validateOrder(cart).contains("El pedido debe tener un peso menor a 10kg"));
    }

    @Test
    void max3ProductRule() throws Exception {

        Product product1 = new Product(1L, "Product 1","Product 1", 1, null, null);
        Product product2 = new Product(2L, "Product 2","Product 2", 1, null, null);
        Product product3 = new Product(2L, "Product 3","Product 3", 1, null, null);

        Cart cart = new Cart();
        cart.addProduct(product1);
        cart.addProduct(product2);
        cart.addProduct(product3);
        cart.addProduct(product3);

        var rulePath = "max3ProductsRule.json";
        var file = new File(rulePath);
        var contents = Files.readAllBytes(file.toPath());

        List<Rule> rules = RuleLoader.loadRulesFromJson(new String(contents));
        RuleEngine ruleEngine = new RuleEngine(rules);

        assertFalse(ruleEngine.validateOrder(cart).isEmpty());
//        assertTrue(ruleEngine.validateOrder(cart).contains("No se pueden incluir más de 3 productos en un mismo pedido"));
    }

    @Test
    void sameProductMax2Rule() throws Exception {

        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new Attribute("color", "azul"));
        Set<Attribute> attributes2 = new HashSet<>();
        attributes.add(new Attribute("color", "verde"));

        Product product1 = new Product(1L, "Product 1","Product 1", 1, attributes, null);
        Product product2 = new Product(2L, "Product 2","Product 2", 1, attributes2, null);

        Cart cart = new Cart();
        cart.addProduct(product1);
        cart.addProduct(product1);
        cart.addProduct(product2);

        var rulePath = "sameProductMax2Rule.json";
        var file = new File(rulePath);
        var contents = Files.readAllBytes(file.toPath());

        List<Rule> rules = RuleLoader.loadRulesFromJson(new String(contents));
        RuleEngine ruleEngine = new RuleEngine(rules);

        assertTrue(ruleEngine.validateOrder(cart).isEmpty());
    }

    @Test
    void sameProductMaxOver2Rule() throws Exception {

        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new Attribute("color", "azul"));
        Set<Attribute> attributes2 = new HashSet<>();
        attributes.add(new Attribute("color", "verde"));

        Product product1 = new Product(1L, "Product 1","Product 1", 1, attributes, null);
        Product product2 = new Product(2L, "Product 2","Product 2", 1, attributes2, null);

        Cart cart = new Cart();
        cart.addProduct(product1);
        cart.addProduct(product1);
        cart.addProduct(product2);
        cart.addProduct(product1);

        var rulePath = "sameProductMax2Rule.json";
        var file = new File(rulePath);
        var contents = Files.readAllBytes(file.toPath());

        List<Rule> rules = RuleLoader.loadRulesFromJson(new String(contents));
        RuleEngine ruleEngine = new RuleEngine(rules);

        assertFalse(ruleEngine.validateOrder(cart).isEmpty());
        assertTrue(ruleEngine.validateOrder(cart).contains("No se pueden incluir más de 2 productos iguales pedido"));
    }

    @Test
    void checkTestWeb() throws Exception {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new Attribute("peso", "5"));
        attributes.add(new Attribute("estado", "gaseoso"));

        Set<Attribute> attributes2 = new HashSet<>();
        attributes2.add(new Attribute("peso", "5"));
        attributes2.add(new Attribute("estado", "liquido"));

        Set<Attribute> attributes3 = new HashSet<>();
        attributes3.add(new Attribute("peso", "1"));
        Set<Attribute> attributes4 = new HashSet<>();
        attributes4.add(new Attribute("peso", "1"));

        Product product1 = new Product(1L, "Product 1","Product 1", 1, attributes, null);
        Product product2 = new Product(2L, "Product 2","Product 2", 1, attributes2, null);
        Product product3 = new Product(3L, "Product 3","Product 3", 1, attributes3, null);
        Product product4 = new Product(4L, "Product 4","Product 4", 1, attributes4, null);

        Cart cart = new Cart();
        cart.addProduct(product1);
        cart.addProduct(product2);
        cart.addProduct(product3);
        cart.addProduct(product4);

        var rulePath = "backend/src/test/dynamicRulesTest/testWeb.json";
        var file = new File(rulePath);
        var contents = Files.readAllBytes(file.toPath());

        List<Rule> rules = RuleLoader.loadRulesFromJson(new String(contents));
        RuleEngine ruleEngine = new RuleEngine(rules);

//        assertTrue(ruleEngine.validateOrder(cart).isEmpty());
        String s = (ruleEngine.validateOrder(cart).toString());
        assertTrue(ruleEngine.validateOrder(cart).contains("El pedido debe tener un peso menor a 10kg"));
        assertTrue(ruleEngine.validateOrder(cart).contains("No se pueden incluir más de 3 productos en un mismo pedido"));
    }

    @Test
    void orderCantHaveTwoElectrodomesticProducts() throws Exception {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new Attribute("azul", "color"));

        Set<Attribute> attributes2 = new HashSet<>();
        attributes2.add(new Attribute("color", "verde"));

        Alternative alternative = new Alternative("electrodomestic", "electrodomestic");
        Alternative alternative2 = new Alternative("electrodomestic", "electrodomestic");

        Product product1 = new Product(1L, "Product 1","Product 1", 1, attributes, alternative);
        Product product2 = new Product(2L, "Product 2","Product 2", 1, attributes2, alternative2);

        Cart cart = new Cart();
        cart.addProduct(product1);
        cart.addProduct(product2);

        var rulePath = "backend/src/test/dynamicRulesTest/orderCantHave2Electrodomestic.json";
        var file = new File(rulePath);
        var contents = Files.readAllBytes(file.toPath());

        List<Rule> rules = RuleLoader.loadRulesFromJson(new String(contents));
        RuleEngine ruleEngine = new RuleEngine(rules);

        assertFalse(ruleEngine.validateOrder(cart).isEmpty());
        assertTrue(ruleEngine.validateOrder(cart).contains("El pedido no puede incluir más de un electrodomestico"));
    }

    @Test
    void liquidCantBeWithGasIfAreMoreThan3EqualProducts() throws Exception {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new Attribute("estado", "liquido"));

        Set<Attribute> attributes2 = new HashSet<>();
        attributes2.add(new Attribute("estado", "gaseoso"));

        Set<Attribute> attributes3 = new HashSet<>();
        attributes3.add(new Attribute("estado", "solido"));

        Product product1 = new Product(1L, "Product 1","Product 1", 1, attributes, null);
        Product product2 = new Product(2L, "Product 2","Product 2", 1, attributes2, null);
        Product product3 = new Product(3L, "Product 3","Product 3", 1, attributes3, null);

        Cart cart = new Cart();
        cart.addProduct(product1);
        cart.addProduct(product2);
        cart.addProduct(product3);
        cart.addProduct(product3);
        cart.addProduct(product3);
        cart.addProduct(product3);

        var rulePath = "backend/src/test/dynamicRulesTest/liquidCantBeWithGasIfAreMoreThan3EqualProducts.json";
        var file = new File(rulePath);
        var contents = Files.readAllBytes(file.toPath());

        List<Rule> rules = RuleLoader.loadRulesFromJson(new String(contents));
        RuleEngine ruleEngine = new RuleEngine(rules);

        assertFalse(ruleEngine.validateOrder(cart).isEmpty());
        assertTrue(ruleEngine.validateOrder(cart).contains("Un mismo pedido no puede combinar productos líquidos y gaseosos si tiene más de 3 productos iguales"));
    }

    @Test
    void definiteRule() throws Exception {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new Attribute("estado", "liquido"));
        attributes.add(new Attribute("peso", "10"));

        Set<Attribute> attributes2 = new HashSet<>();
        attributes2.add(new Attribute("estado", "gaseoso"));
        attributes2.add(new Attribute("peso", "6"));

        Set<Attribute> attributes3 = new HashSet<>();
        attributes3.add(new Attribute("estado", "solido"));

        Product product1 = new Product(1L, "Product 1","Product 1", 1, attributes, new Alternative("sport", "sport"));
        Product product2 = new Product(2L, "Product 2","Product 2", 1, attributes2, new Alternative("electrodomestic", "electrodomestic"));
        Product product3 = new Product(3L, "Product 3","Product 3", 1, attributes3, new Alternative("electrodomestic", "electrodomestic"));

        Cart cart = new Cart();
        cart.addProduct(product1);
        cart.addProduct(product2);
        cart.addProduct(product3);
        cart.addProduct(product3);
        cart.addProduct(product3);
        cart.addProduct(product3);

        var rulePath = "backend/src/test/dynamicRulesTest/definitiveRules.json";
        var file = new File(rulePath);
        var contents = Files.readAllBytes(file.toPath());

        List<Rule> rules = RuleLoader.loadRulesFromJson(new String(contents));
        RuleEngine ruleEngine = new RuleEngine(rules);

        assertFalse(ruleEngine.validateOrder(cart).isEmpty());
        assertTrue(ruleEngine.validateOrder(cart).contains("Un mismo pedido no puede combinar productos líquidos y gaseosos si tiene más de 3 productos iguales"));
        assertTrue(ruleEngine.validateOrder(cart).contains("El pedido no puede incluir más de un electrodomestico"));
        assertTrue(ruleEngine.validateOrder(cart).contains("El peso del pedido no puede superar los 15kg"));
    }
}
