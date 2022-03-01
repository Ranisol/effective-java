package com.effectivejava.example.item_10_equals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;


class TestCaseInsensitiveString {
    @Test
    public void violateSymmetry() {
        CaseInsensitiveStringViolationSymmetry cis = new CaseInsensitiveStringViolationSymmetry("Polish");
        String s = "polish";
        Assertions.assertTrue(cis.equals(s));
        Assertions.assertFalse(s.equals(cis)); // 반대방향으로는 동작하지 않음 => 대칭성 위배
    }

    @Test
    public void satisfySymmetry() {
        CaseInsensitiveStringSatisfySymmetry cis = new CaseInsensitiveStringSatisfySymmetry("Polish");
        String s = "polish";
        Assertions.assertFalse(cis.equals(s)); // 지원하기를 포기함
        Assertions.assertFalse(s.equals(cis));
    }

}

class CaseInsensitiveStringViolationSymmetry {
    private final String s;
    public CaseInsensitiveStringViolationSymmetry(String s) {
        this.s = Objects.requireNonNull(s);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof CaseInsensitiveStringViolationSymmetry)
            return s.equalsIgnoreCase(
                    ((CaseInsensitiveStringViolationSymmetry) o).s
            );
        if(o instanceof String)
            return s.equalsIgnoreCase((String) o);
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(s);
    }
}

class CaseInsensitiveStringSatisfySymmetry {
    private final String s;
    public CaseInsensitiveStringSatisfySymmetry(String s) {
        this.s = Objects.requireNonNull(s);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof CaseInsensitiveStringSatisfySymmetry &&
                ((CaseInsensitiveStringSatisfySymmetry) o).s.equalsIgnoreCase(s); // String 까지 연동하겠단 꿈을 버릴것
    }

    @Override
    public int hashCode() {
        return Objects.hash(s);
    }
}