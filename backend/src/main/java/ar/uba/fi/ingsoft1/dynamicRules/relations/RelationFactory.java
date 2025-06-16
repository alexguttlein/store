package ar.uba.fi.ingsoft1.dynamicRules.relations;

public class RelationFactory {
    public static PredicateRelation create(String relationType) {
        switch (relationType.toLowerCase()) {
            case "and":
                return new AndRelation();
            case "or":
                return new OrRelation();
            case "notand":
            case "not and":
                return new NotAndRelation();
            default:
                throw new IllegalArgumentException("Unknown relation type: " + relationType);
        }
    }
}
