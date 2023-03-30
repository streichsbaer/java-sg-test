package com.example.demo;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateAwareExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    private static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();
    private static final SpelExpressionParser SPEL_EXPRESSION_PARSER = new SpelExpressionParser();
    private static final TemplateAwareExpressionParser TEMPLATE_AWARE_EXPRESSION_PARSER = new SpelExpressionParser();
    @GetMapping("/test01")
    public void test01(@RequestParam String pr1) {
        ExpressionParser expressionParser = new SpelExpressionParser();
        // ruleid: taint-backend-spel-injection
        Expression ex1 = expressionParser.parseExpression(pr1, new TemplateParserContext());
        ex1.getValue();

        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        // ruleid: taint-backend-spel-injection
        spelExpressionParser.parseExpression(pr1, new TemplateParserContext()).getValue(new StandardEvaluationContext());

        TemplateAwareExpressionParser templateAwareExpressionParser = new SpelExpressionParser();
        // ruleid: taint-backend-spel-injection
        Expression ex2 = templateAwareExpressionParser.parseExpression(pr1, new TemplateParserContext());
        ex2.getValue(new Object());

        // ruleid: taint-backend-spel-injection
        Expression ex3 = EXPRESSION_PARSER.parseExpression(pr1);
        ex3.getValue(new StandardEvaluationContext());
        // ruleid: taint-backend-spel-injection
        SpelExpression spelEx = SPEL_EXPRESSION_PARSER.parseExpression(pr1);
        spelEx.getValue(new StandardEvaluationContext(), new Ojbect());
        // ok: taint-backend-spel-injection
        TEMPLATE_AWARE_EXPRESSION_PARSER.parseExpression(pr1).getValue(new SimpleEvaluationContext());
        // ruleid: taint-backend-spel-injection
        SpelExpression spelEx = spelExpressionParser.parseRaw(pr1);
        spelEx.getValue(new Object());

    }

    @GetMapping("/test02")
    public void test02(@RequestParam String pr1) {
        // ok: taint-backend-spel-injection
        Expression ex1 = EXPRESSION_PARSER.parseExpression("'Any String'.length()");
        ex1.getValue();
        // ok: taint-backend-spel-injection
        SPEL_EXPRESSION_PARSER.parseRaw("'Any String'.length()").getValue(new StandardEvaluationContext());
        // ok: taint-backend-spel-injection
        Expression ex2 = SPEL_EXPRESSION_PARSER.parseExpression("'" + pr1 + "'.length()");
        ex2.getValue(new SimpleEvaluationContext());
        // ruleid: taint-backend-spel-injection
        Expression ex3 = SPEL_EXPRESSION_PARSER.parseExpression("'" + pr1 + "'.length()");
        ex3.getValue(new Object());
        // ruleid: taint-backend-spel-injection
        SPEL_EXPRESSION_PARSER.parseRaw("'" + pr1 + "'.length()").getValue();
        pr1 = "'Any String'.length()";
        // ok: taint-backend-spel-injection
        TEMPLATE_AWARE_EXPRESSION_PARSER.parseExpression(pr1).getValue(new Object());
        // ok: taint-backend-spel-injection
        Expression ex4 = SPEL_EXPRESSION_PARSER.parseExpression(pr1);
        ex4.getValue();
        // ok: taint-backend-spel-injection
        SPEL_EXPRESSION_PARSER.parseRaw(pr1).getValue();
        // ok: taint-backend-spel-injection
        SpelExpression ex5 = SPEL_EXPRESSION_PARSER.parseRaw(pr1);
        ex5.getValue(new Object());
    }
}
