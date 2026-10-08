package auxiliar.Filters;

import java.io.Serializable;


public class Filter implements Serializable {
    public static final String[] opStrings = {"EQ", "LT", "LTE", "GT", "GTE", "ILIKE"};
    // public static enum Op = {EQ, LT, LTE, GT, GTE, ILIKE};
    
    private String field;
    private String operator;
    private String value;

    public Filter(String field, String opString, String value){
        this.field = field;
        this.value = value;
        this.operator = this.opConverter(opString);
    }

    // public Filter(String field, Op operator, String value){
    //     this.field = field;
    //     this.operator = this.opConverter(operator.toString());
    //     this.value = value;
    // }

    private String opConverter(String op) {
        switch (op) {
            case "EQ":
                return "=";
            case "LT":
                return "<";
            case "LTE":
                return "<=";
            case "GT":
                return ">";
            case "GTE":
                return ">=";
            default:
                this.value = "%" + this.value + "%";
                return "ILIKE";
        }
    }


    public String getField() {
        return this.field;
    }
    

    public String getOperator() {
        return this.operator;
    }

    
    public String getValue() {
        return this.value;
    }
}
