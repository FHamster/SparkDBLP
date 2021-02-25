/*
 *
 *  *  com.github.rutledgepaulv.qbuilders.visitors.MongoVisitor
 *  *  *
 *  *  * Copyright (C) 2016 Paul Rutledge <paul.v.rutledge@gmail.com>
 *  *  *
 *  *  * This software may be modified and distributed under the terms
 *  *  * of the MIT license.  See the LICENSE file for details.
 *  *
 *
 */

package cn.jmu.spark_dblp.server.util;

import com.github.rutledgepaulv.qbuilders.nodes.ComparisonNode;
import com.github.rutledgepaulv.qbuilders.operators.ComparisonOperator;
import com.github.rutledgepaulv.qbuilders.visitors.PredicateVisitor;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class InsensitivePredicateVisitor<T> extends PredicateVisitor<T> {


    @Override
    protected Predicate<T> visit(ComparisonNode node) {

        ComparisonOperator operator = node.getOperator();

        if(ComparisonOperator.EQ.equals(operator)) {
            return single(node, this::equality);
        } else if(ComparisonOperator.NE.equals(operator)) {
            return single(node, this::inequality);
        } else if (ComparisonOperator.EX.equals(operator)) {
            return ((Boolean)node.getValues().iterator().next()) ? exists(node) : doesNotExist(node);
        } else if (ComparisonOperator.GT.equals(operator)) {
            return single(node, this::greaterThan);
        } else if (ComparisonOperator.LT.equals(operator)) {
            return single(node, this::lessThan);
        } else if (ComparisonOperator.GTE.equals(operator)) {
            return single(node, this::greaterThanOrEqualTo);
        } else if (ComparisonOperator.LTE.equals(operator)) {
            return single(node, this::lessThanOrEqualTo);
        } else if (ComparisonOperator.IN.equals(operator)) {
            return multi(node, this::in);
        } else if (ComparisonOperator.NIN.equals(operator)) {
            return multi(node, this::nin);
        } else if (ComparisonOperator.RE.equals(operator)) {
            return single(node, this::regex);
        } else if (ComparisonOperator.SUB_CONDITION_ANY.equals(operator)) {
            Predicate test = condition(node);
            // subquery condition is ignored because a predicate has already been built.
            return single(node, (fieldValue, subQueryCondition) -> this.subquery(fieldValue, test));
        }

        throw new UnsupportedOperationException("This visitor does not support the operator " + operator + ".");
    }

    protected boolean regex(Object actual, Object query) {
        Predicate<String> test;

        if (query instanceof String) {
            String queryRegex = (String) query;
            test = Pattern.compile(queryRegex,Pattern.CASE_INSENSITIVE).asPredicate();
        } else {
            return false;
        }

        if (actual.getClass().isArray()) {
            String[] values = (String[]) actual;
            return Arrays.stream(values).anyMatch(test);
        } else if (Collection.class.isAssignableFrom(actual.getClass())) {
            Collection<String> values = (Collection<String>) actual;
            return values.stream().anyMatch(test);
        } else if (actual instanceof String) {
            return test.test((String) actual);
        }

        return false;
    }


    private Predicate<T> doesNotExist(ComparisonNode node) {
        return t -> resolveSingleField(t, node.getField().asKey(), node, (one, two) -> Objects.isNull(one));
    }

    private Predicate<T> exists(ComparisonNode node) {
        return t -> resolveSingleField(t, node.getField().asKey(), node, (one, two) -> Objects.nonNull(one));
    }

    private Predicate<T> single(ComparisonNode node, BiPredicate<Object, Object> func) {
        return t -> resolveSingleField(t, node.getField().asKey(), node, func);
    }

    private Predicate<T> multi(ComparisonNode node, BiPredicate<Object, Collection<?>> func) {
        return t -> resolveMultiField(t, node.getField().asKey(), node, func);
    }

    private boolean resolveSingleField(Object root, String field, ComparisonNode node, BiPredicate<Object, Object> func) {
        if(root == null || node.getField() == null) {
            return func.test(null, node.getValues().iterator().next());
        } else {
            String[] splitField = field.split("\\.", 2);
            Object currentField = getFieldValueFromString(root, splitField[0]);
            if(splitField.length == 1) {
                return func.test(currentField, node.getValues().iterator().next());
            } else {
                return recurseSingle(currentField, splitField[1], node, func);
            }
        }
    }

    private boolean recurseSingle(Object root, String field, ComparisonNode node, BiPredicate<Object, Object> func) {

        if(root.getClass().isArray()) {
            return Arrays.stream((Object[])root).anyMatch(t -> resolveSingleField(t, field, node, func));
        }

        if(root instanceof Collection) {
            return ((Collection<Object>)root).stream().anyMatch(t -> resolveSingleField(t, field, node, func));
        }

        return resolveSingleField(root, field, node, func);
    }

    private boolean resolveMultiField(Object root, String field, ComparisonNode node, BiPredicate<Object, Collection<?>> func) {
        if(root == null || node.getField() == null) {
            return func.test(null, node.getValues());
        } else {
            String[] splitField = field.split("\\.", 2);
            Object currentField = getFieldValueFromString(root, splitField[0]);
            if(splitField.length == 1) {
                return func.test(currentField, node.getValues());
            } else {
                return recurseMulti(currentField, splitField[1], node, func);
            }
        }
    }

    private boolean recurseMulti(Object root, String field, ComparisonNode node, BiPredicate<Object, Collection<?>> func) {

        if(root.getClass().isArray()) {
            return Arrays.stream((Object[])root).anyMatch(t -> resolveMultiField(t, field, node, func));
        }

        if(root instanceof Collection) {
            return ((Collection<Object>)root).stream().anyMatch(t -> resolveMultiField(t, field, node, func));
        }

        return resolveMultiField(root, field, node, func);
    }

    private Object getFieldValueFromString(Object o, String s) {
        if(o == null) {
            return null;
        }
        try {
            return FieldUtils.readField(o, s, true);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

}
